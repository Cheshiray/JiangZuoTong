package com.example.jiangzuotong;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements Runnable {

    Handler handler;
    ContentManager contentManager;
    contentItem myItem;

    String[] data;
    String[] data2;
    String[] str;
    Integer index;
    String title;
    String date;
    String content;
    TextView tv;
    ImageButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        contentManager = new ContentManager(this);
        btn = findViewById(R.id.btn_operation);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 7) {
                    ListView listView = (ListView) findViewById(R.id.list_view);
                    Bundle bdl = (Bundle) msg.obj;
                    myItem = (contentItem)bdl.getSerializable("item");
                    data = bdl.getStringArray("data");
                    title = myItem.getTitle();

                    ListAdapter adapter = new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_list_item_1, data);
                    listView.setAdapter(adapter);
                    listView.setEmptyView(findViewById(R.id.progressBar2));

                    if (contentManager.isExist(title)) {
                        btn.setSelected(true);
                    } else {
                        btn.setSelected(false);
                    }

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (contentManager.isExist(title)) {
                                btn.setSelected(false);
                                contentManager.onDelete(title);
                                Toast.makeText(Main2Activity.this, "取消成功", Toast.LENGTH_SHORT).show();

                            } else {
                                btn.setSelected(true);
                                contentManager.add(myItem);
                                Toast.makeText(Main2Activity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void run() {
        contentManager = new ContentManager(this);
        Bundle bundle = new Bundle();

        index = getIntent().getIntExtra("id", 0);

        Document doc1;
        Document doc2;
        try {
            doc1 = Jsoup.connect("https://www.swufe.edu.cn/index/xsjz.htm").get();
            Elements u = doc1.select("a[href]");
            String url = u.get(54+index).attr("abs:href");
            doc2 = Jsoup.connect(url).get();
            Elements e = doc2.select("p");

            ArrayList<String> strList = new ArrayList<String>();
            for (int i=0; i<e.size()-5; i++) {
                strList.add(e.get(i).text());
            }

            for (int i=0; i<strList.size(); i++) {
                if (strList.get(i).equals("")) {
                    strList.remove(i);
                    i--;
                }
            }

            data2 = strList.toArray(new String[strList.size()]);

            if (data2[1].equals("（线上讲座）")) {
                str = new String[data2.length-3];
                for (int i=0; i<str.length; i++) {
                    str[i] = data2[i+3];
                }
                tv = findViewById(R.id.textView);
                title = data2[2].replace("主题：", "");
                title = title.replace("主 题：", "");
                tv.setText(title);

            } else {
                str = new String[data2.length-2];
                for(int i=0; i<str.length; i++) {
                    str[i] = data2[i+2];
                }
                tv = findViewById(R.id.textView);
                title = data2[1].replace("主题：", "");
                title = title.replace("主 题：", "");
                tv.setText(title);
            }
            bundle.putStringArray("data", str);
            date = str[2].replace("时间：", "");

            StringBuffer sb = new StringBuffer();
            for (int i=0; i<str.length; i++) {
                sb.append(str[i]+"|");
            }
            sb.deleteCharAt(sb.length()-1);
            content = sb.toString();

            myItem = new contentItem(title, date, content);
            bundle.putSerializable("item", myItem);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage(7);
        msg.obj = bundle;
        handler.sendMessage(msg);
    }

    public void back1(View v) {
        finish();
    }
}