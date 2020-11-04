package com.example.jiangzuotong;

import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;

public class Fragment_Settings extends Fragment implements Runnable {

    Handler handler;
    TextView tv;
    Switch sw;
    MottoManager mottoManager;
    mottoItem myItem;
    Integer data_1;
    Integer data_2;

    private String logDate = "";
    private final String DATE_SP_KEY = "lastDate";

    public Fragment_Settings() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_settings, container, false);

        Thread t = new Thread(this);
        t.start();

        SharedPreferences sp = getActivity().getSharedPreferences("myMotto", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 6) {
                    Bundle bdl = (Bundle) msg.obj;
                    tv = getView().findViewById(R.id.txt_content);
                    tv.setTypeface(Typeface.createFromAsset(Fragment_Settings.this.getActivity().getAssets(), "fonts/华康简仿宋.ttf"));
                }
                super.handleMessage(msg);
            }
        };

        sw = view.findViewById(R.id.switch1);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("state", Context.MODE_PRIVATE);
        Boolean state = sharedPreferences.getBoolean("checked?", false);
        sw.setChecked(state);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startRemind();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("checked?", true);
                    editor.apply();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("提醒")
                            .setMessage("为保证提醒功能正常运行，请在系统设置中开启通知权限和后台自运行权限。")
                            .setPositiveButton("好的", null);
                    builder.create().show();
                } else {
                    stopRemind();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("checked?", false);
                    editor.apply();
                }
            }
        });
        return view;
    }

    @Override
    public void run() {
        Bundle bundle = new Bundle();
        mottoManager = new MottoManager(getActivity());
        myItem = mottoManager.getNewest();
        String content;

        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());

        if (curDateStr.equals(logDate)) {
            tv = getView().findViewById(R.id.txt_content);
            tv.setText(myItem.getContent());
        } else {
            Document doc;
            try {
                if (String.valueOf(myItem.getDate_1()).length() == 0) {
                    data_1 = -1;
                    data_2 = 1;
                } else {
                    data_1 = myItem.getDate_1();
                    data_2 = myItem.getDate_2();
                }

                if (data_1+1>9) {
                    data_1 = 0;
                    data_2++;
                } else {
                    data_1++;
                }

                doc = Jsoup.connect("https://www.juzikong.com/collections/f4c0f5b6-d335-48f4-96e3-6433459b0f8f?page="+String.valueOf(data_2)).get();
                Elements div = doc.getElementsByClass("content_2hYZM");
                content = div.get(data_1).text();
                if (content.contains(" ")) {
                    int index = content.indexOf(" ");
                    content = content.substring(0, index);
                }
                tv = getView().findViewById(R.id.txt_content);
                tv.setText(content);

                myItem = new mottoItem(data_1, data_2, content);
                mottoManager.add(myItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SharedPreferences sp = getActivity().getSharedPreferences("myMotto", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(DATE_SP_KEY, curDateStr);
            edit.apply();
        }
        Message msg = handler.obtainMessage(6);
        msg.obj = bundle;
        handler.sendMessage(msg);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button button = (Button) getActivity().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myCollection = new Intent(getActivity(), MyCollection.class);
                getActivity().startActivityForResult(myCollection,1);
            }
        });
    }

    private void startRemind(){
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        long systemTime = System.currentTimeMillis();
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mCalendar.set(Calendar.HOUR_OF_DAY, 23);
        mCalendar.set(Calendar.MINUTE, 30);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        long selectTime = mCalendar.getTimeInMillis();

        if(systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        AlarmManager am = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);

        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24 * 3), pi);
    }

    private void stopRemind(){
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        AlarmManager am = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);

        am.cancel(pi);
        Toast.makeText(getActivity(), "提醒已关闭", Toast.LENGTH_SHORT).show();
    }
}