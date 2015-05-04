package com.aiff.callup_001;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ContactsActivity extends Activity implements View.OnClickListener {

    int[] colors = new int[2];

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    List <String> online = new ArrayList<>();

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        UserData g = UserData.getInstance();
        List<List<String>> data = g.getContacts();
        Log.d("ContactsLogs", String.valueOf(data));
        Log.d("ContactsLogs", String.valueOf(data.size()));

        colors[0] = Color.parseColor("#FFFFCC");
        //colors[1] = Color.parseColor("#55336699");

        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);

        LayoutInflater ltInflater = getLayoutInflater();

        for (int i = 0; i < data.size(); i++) {

            final List<String> d = data.get(i);

            View item = ltInflater.inflate(R.layout.item_contact, linLayout, false);
            item.setOnClickListener(this);

            TextView tvName = (TextView) item.findViewById(R.id.tvName);
            tvName.setText(d.get(2));
            TextView tvPosition = (TextView) item.findViewById(R.id.tvPosition);
            tvPosition.setText(d.get(1));
            TextView tvSalary = (TextView) item.findViewById(R.id.tvSalary);
            tvSalary.setText(d.get(3));

            if (d.get(3).equals("2")) online.add("false");
            tvSalary.setText("offline");

            item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
            item.setBackgroundColor(colors[0]);

            Button btnMsgs = (Button) item.findViewById(R.id.button1);
            btnMsgs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentSendMessage = new Intent(ContactsActivity.this, SendMessageActivity.class);
                    intentSendMessage.putExtra("contact", d.get(1));
                    startActivity(intentSendMessage);
                }
            });

            linLayout.addView(item);
        }

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();

        mTimer.schedule(mMyTimerTask, 5000, 5000);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    SharedPreferences prefs = ContactsActivity.this.getSharedPreferences(
                            "com.aiff.callup_001.app", Context.MODE_PRIVATE);

                    final String userLoginKey = "com.aiff.callup_001.app.login";
                    final String userPassKey = "com.aiff.callup_001.app.pass";

                    String storedLogin = prefs.getString(userLoginKey, new String());
                    String storedPass = prefs.getString(userPassKey, new String());

                    String[] args = new String[6];
                    args[0] = "hello";
                    args[1] = storedLogin;
                    args[2] = storedPass;
                    String res = null;

                    try {
                        res = new ConnectServer().execute(args).get();

                        UserData g = UserData.getInstance();
                        List<List<String>> data = g.getNewEvents();

                        for (int i = 0; i < data.size(); i++) {
                            List<String> d = data.get(i);
                            if (d.get(0).equals("events")) {
                                switch (d.get(3)) {
                                    case "1":   // new message
                                        d.set(0, "messages");
                                        d.set(3, "2");
                                        List<List<String>> addData1 = new ArrayList<>();
                                        addData1.add(d);
                                        g.setData(addData1);

                                        Toast.makeText(ContactsActivity.this, "New incoming message", Toast.LENGTH_SHORT).show();
                                        Log.d("ContactsLogs:events", String.valueOf(g.getEvents()));

                                        break;

                                    case "2":   // new call

                                        Toast.makeText(ContactsActivity.this, "Incoming call", Toast.LENGTH_SHORT).show();
                                        break;

                                    case "3":   // missing call
                                        d.set(0, "calls");
                                        d.set(2, d.get(4));
                                        d.set(3, "3");
                                        d.remove(4);
                                        List<List<String>> addData3 = new ArrayList<>();
                                        addData3.add(d);
                                        g.setData(addData3);

                                        Toast.makeText(ContactsActivity.this, "Missing call", Toast.LENGTH_SHORT).show();
                                        break;

                                    case "4":   // new contact

                                        Toast.makeText(ContactsActivity.this, "New contact request", Toast.LENGTH_SHORT).show();
                                        break;

                                }
                            } else {
                                /*for (int j = 1; j < d.size(); j++) {
                                    if (!online.get(j).equals(d.get(j))){
                                        online.set(j, d.get(j));
                                    }
                                }*/
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    @Override
    public void onClick(View v) {

        LinearLayout ll = (LinearLayout) v.findViewById(R.id.linearLayout2);
        ll.setBackgroundColor(Color.parseColor("#00000000"));

        //Log.d("ContactsLogs", String.valueOf(ll.getVisibility()));

        if (ll.getVisibility() == View.GONE) {
            ll.setVisibility(View.VISIBLE);
        } else ll.setVisibility(View.GONE);

        //Log.d("ContactsLogs", String.valueOf(ll.getVisibility()));
    }
}
