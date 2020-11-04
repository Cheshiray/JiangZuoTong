package com.example.jiangzuotong;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyCollectionItem extends AppCompatActivity {

    ContentManager contentManager;
    contentItem myItem;

    String[] data;
    String title;
    TextView tv;
    ImageButton btn1;
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        contentManager = new ContentManager(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent = getIntent();
        title = intent.getStringExtra("item");
        myItem = contentManager.getRow(title);

        tv = findViewById(R.id.textView);
        tv.setText(title);
        ListView listView = (ListView) findViewById(R.id.list_view);
        data = myItem.getContent().split("\\|");
        ListAdapter adapter = new ArrayAdapter<String>(MyCollectionItem.this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.progressBar2));

        btn1 = findViewById(R.id.btn_operation);
        if (contentManager.isExist(title)) {
            btn1.setSelected(true);
        } else {
            btn1.setSelected(false);
        }
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentManager.isExist(title)) {
                    btn1.setSelected(false);
                    contentManager.onDelete(title);
                    Toast.makeText(MyCollectionItem.this, "取消成功", Toast.LENGTH_SHORT).show();
                } else {
                    btn1.setSelected(true);
                    contentManager.add(myItem);
                    Toast.makeText(MyCollectionItem.this, "收藏成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn2 = findViewById(R.id.btn_back1);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCollectionItem.this, MyCollection.class);
                startActivity(intent);
                MyCollectionItem.this.finish();
            }
        });
    }
}