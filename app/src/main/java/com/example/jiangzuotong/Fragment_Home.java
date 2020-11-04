package com.example.jiangzuotong;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Fragment_Home extends Fragment implements Runnable, AdapterView.OnItemClickListener {

    Handler handler;
    String[] title_a;
    String[] date_a;
    ArrayList<String> title_b;
    ArrayList<String> date_b;
    ArrayList<Integer> id_a;
    ArrayList<Integer> id_b;
    String[] data;
    int num;
    ArrayList<HashMap<String, String>> listItems;

    public Fragment_Home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_home, container, false);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 5) {
                    Bundle bdl = (Bundle) msg.obj;
                    title_a = bdl.getStringArray("title");
                    date_a = bdl.getStringArray("date");
                    id_a = bdl.getIntegerArrayList("idList");
                    setId_b(id_a);
                    num = bdl.getInt("num", 1);

                    ListView listView = (ListView)getView().findViewById(R.id.list_view_home);
                    listItems = new ArrayList<HashMap<String, String>>();
                    for (int i=0; i<num; i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("ItemTitle", title_a[i]);
                        map.put("ItemDate", date_a[i]);
                        listItems.add(map);
                    }

                    MyListAdapter myAdapter = new MyListAdapter(Fragment_Home.this.getActivity(), R.layout.mylist, listItems);
                    listView.setAdapter(myAdapter);
                    listView.setOnItemClickListener(Fragment_Home.this);
                    listView.setEmptyView(getView().findViewById(R.id.progressBar));
                    listView.setEmptyView(getView().findViewById(R.id.textView3));
                }
                super.handleMessage(msg);
            }
        };
        return view;
    }

    @Override
    public void run() {
        Bundle bundle = new Bundle();
        title_b = new ArrayList<String>();
        date_b = new ArrayList<String>();
        id_a = new ArrayList<Integer>();

        Document doc1;
        Document doc2;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Integer index = simpleDateFormat.format(System.currentTimeMillis()).indexOf(" ");
            String systemDate_1 = simpleDateFormat.format(System.currentTimeMillis()).substring(0, index);
            Date systemDate_2 = sdf.parse(systemDate_1);

            doc1 = Jsoup.connect("https://www.swufe.edu.cn/index/xsjz.htm").get();
            Elements u = doc1.select("a[href]");
            for (int i=0; i<15; i++) {
                String url = u.get(54 + i).attr("abs:href");
                doc2 = Jsoup.connect(url).get();
                Elements e = doc2.select("p");

                ArrayList<String> strList = new ArrayList<String>();
                for (int j=0; j<e.size()-5; j++) {
                    strList.add(e.get(j).text());
                }

                for (int j=0; j<strList.size(); j++) {
                    if (strList.get(j).equals("")) {
                        strList.remove(j);
                        j--;
                    }
                }

                data = strList.toArray(new String[strList.size()]);

                if (data[1].equals("（线上讲座）")) {
                    String selectDate = data[5].substring(data[5].indexOf("2"), data[5].indexOf("日"));
                    selectDate = selectDate.replace("年", "-");
                    selectDate = selectDate.replace("月", "-");
                    selectDate = selectDate.replace("日", "");
                    Date selectDate_2 = sdf.parse(selectDate);

                    if (selectDate_2.getTime() >= systemDate_2.getTime()) {
                        date_b.add(data[5].replace("时间：", ""));
                        String title = data[2].replace("主题：", "");
                        title = title.replace("主 题：", "");
                        if (title.length()<45) {
                            title_b.add(title);
                        } else {
                            title_b.add(title.substring(0,45));
                        }
                        id_a.add(i);
                    }
                } else {
                    String selectDate = data[4].substring(data[4].indexOf("2"), data[4].indexOf("日"));
                    selectDate = selectDate.replace("年", "-");
                    selectDate = selectDate.replace("月", "-");
                    selectDate = selectDate.replace("日", "");
                    Date selectDate_2 = sdf.parse(selectDate);

                    if (selectDate_2.getTime() >= systemDate_2.getTime()) {
                        date_b.add(data[4].replace("时间：", ""));
                        String title = data[1].replace("主题：", "");
                        title = title.replace("主 题：", "");
                        if (title.length()<45) {
                            title_b.add(title);
                        } else {
                            title_b.add(title.substring(0,45));
                        }
                        id_a.add(i);
                    }
                }
            }
            String[] title_c = title_b.toArray(new String[title_b.size()]);
            String[] date_c = date_b.toArray(new String[date_b.size()]);
            num = title_c.length;
            bundle.putStringArray("title", title_c);
            bundle.putStringArray("date", date_c);
            bundle.putIntegerArrayList("idList", id_a);
            bundle.putInt("num", num);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(5);
        msg.obj = bundle;
        handler.sendMessage(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent url = new Intent(view.getContext(), Main2Activity.class);
        id_b = getId_b();
        Integer[] id_c = id_b.toArray(new Integer[id_b.size()]);
        url.putExtra("id", id_c[position]);
        view.getContext().startActivity(url);
    }

    public ArrayList<Integer> getId_b() {
        return id_b;
    }

    public void setId_b(ArrayList<Integer> id_b) {
        this.id_b = id_b;
    }
}