package com.example.jiangzuotong;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MyCollection extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    String[] title;
    String[] date;
    ContentManager contentManager;
    ArrayList<HashMap<String, String>> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection);

        contentManager = new ContentManager(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        if (contentManager.getCount() != 0) {
            title = contentManager.listColumnValues("TITLE");
            date = contentManager.listColumnValues("DATE");

            ListView listView = findViewById(R.id.list_view2);
            listItems = new ArrayList<HashMap<String, String>>();
            for (int i=0; i<title.length; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", title[i]);
                map.put("ItemDate", date[i]);
                listItems.add(map);
            }
            Collections.reverse(listItems);

            MyListAdapter2 myAdapter = new MyListAdapter2(MyCollection.this, R.layout.mylist2, listItems);
            listView.setAdapter(myAdapter);
            listView.setOnItemClickListener(MyCollection.this);
            listView.setOnItemLongClickListener(MyCollection.this);
            listView.setEmptyView(findViewById(R.id.noData));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView title = view.findViewById(R.id.listTitle2);
        Intent myItem = new Intent(this, MyCollectionItem.class);
        myItem.putExtra("item", title.getText());
        startActivity(myItem);
        MyCollection.this.finish();
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请确认是否取消该收藏")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contentManager = new ContentManager(MyCollection.this);
                        TextView title = view.findViewById(R.id.listTitle);
                        ArrayAdapter adapter = (ArrayAdapter) parent.getAdapter();
                        adapter.remove(parent.getItemAtPosition(position));
                        contentManager.onDelete(title.getText().toString());
                    }
                }).setNegativeButton("否", null);
        builder.create().show();
        return true;
    }

    public void back2(View v) {
        MyCollection.this.setResult(2);
        MyCollection.this.finish();
    }
}