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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ContactsActivity extends Activity implements View.OnClickListener {

    int[] colors = new int[2];

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;

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

        colors[0] = Color.parseColor("#559966CC");
        colors[1] = Color.parseColor("#55336699");

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
            item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
            item.setBackgroundColor(colors[i % 2]);

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

                    Toast.makeText(ContactsActivity.this, "Hello!", Toast.LENGTH_SHORT).show();

                    try {
                        res = new ConnectServer().execute(args).get();
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
