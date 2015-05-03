package com.aiff.callup_001;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class SendMessageActivity extends Activity implements View.OnClickListener {

    int[] colors = new int[2];
    String contact_number;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;


    Integer smsNum = 0;

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Intent intent = getIntent();
        contact_number = intent.getStringExtra("contact");
        //Log.d("SendMessageActivity", contact_number);

        if (contact_number.equals("-1")) {
            ((LinearLayout) findViewById(R.id.linLayoutNew)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.ContactName)).setVisibility(View.GONE);
        }

        UserData g = UserData.getInstance();
        List<List<String>> data = g.getMessages();

        colors[0] = Color.parseColor("#559966CC");
        colors[1] = Color.parseColor("#55336699");

        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
        TextView contact_name = (TextView) findViewById(R.id.ContactName);
        contact_name.setText(contact_number);

        List<List<String>> contacts = g.getContacts();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).get(1).equals(contact_number)) {
                contact_name.setText(contacts.get(i).get(2));
                break;
            }
        }

        Button btnSend = (Button) findViewById(R.id.button4);
        btnSend.setOnClickListener(this);

        LayoutInflater ltInflater = getLayoutInflater();

        for (int i = 0; i < data.size(); i++) {

            List<String> d = data.get(i);

            if (d.get(1).equals(contact_number)) {

                smsNum++;
                View item;

                if (d.get(3).equals("1")) {
                    item = ltInflater.inflate(R.layout.item_message_left, linLayout, false);
                    ((TextView) item.findViewById(R.id.textView8)).setBackgroundColor(colors[0]);
                } else {
                    item = ltInflater.inflate(R.layout.item_message_right, linLayout, false);
                    ((TextView) item.findViewById(R.id.textView8)).setBackgroundColor(colors[0]);
                }

                TextView msgText = (TextView) item.findViewById(R.id.textView8);
                msgText.setText(d.get(2));
                item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                linLayout.addView(item);

            }
        }

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();

        mTimer.schedule(mMyTimerTask, 2000, 2000);

    }

    @Override
    public void onClick(View v) {
        if (contact_number.equals("-1")) {
            contact_number = String.valueOf(((EditText) findViewById(R.id.editText6)).getText());
        }

        // *** Prepare request ***

        SharedPreferences prefs = this.getSharedPreferences(
                "com.aiff.callup_001.app", Context.MODE_PRIVATE);

        final String userLoginKey = "com.aiff.callup_001.app.login";
        final String userPassKey = "com.aiff.callup_001.app.pass";

        String storedLogin = prefs.getString(userLoginKey, new String());
        String storedPass = prefs.getString(userPassKey, new String());
        String text = String.valueOf(((EditText) findViewById(R.id.editText5)).getText());
        ;

        String[] args = new String[6];
        args[0] = "msg";
        args[1] = storedLogin;
        args[2] = storedPass;
        args[3] = contact_number;
        args[4] = text;

        try {
            String res = new ConnectServer().execute(args).get();
            switch (res) {
                case "-3":
                    Toast.makeText(this, "System error occurred. Please, try again later.", Toast.LENGTH_SHORT).show();
                    break;

                case "-1":
                    Toast.makeText(this, "Authentication failure. Please, try again later.", Toast.LENGTH_SHORT).show();
                    break;

                case "-2":
                    Toast.makeText(this, "Contact " + contact_number + " not found.", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    List<List<String>> newMsg = new ArrayList<>();
                    List<String> item = new ArrayList<>();
                    item.add("messages");
                    item.add(contact_number);
                    item.add(text);
                    item.add("1");
                    item.add(res);
                    newMsg.add(item);

                    UserData g = UserData.getInstance();
                    g.setData(newMsg);

                    Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();

                    Intent intentSendMessage = new Intent(SendMessageActivity.this, SendMessageActivity.class);
                    intentSendMessage.putExtra("contact", contact_number);
                    startActivity(intentSendMessage);
                    SendMessageActivity.this.finish();


            }

        } catch (InterruptedException e) {
            Toast.makeText(this, "An error occurred. Please, try again later.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            Toast.makeText(this, "An error occurred. Please, try again later.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    UserData g = UserData.getInstance();
                    List<List<String>> data = g.getMessages();

                    Integer sms = 0;

                    for (int i = 0; i < data.size(); i++)
                        if (data.get(i).get(1).equals(contact_number)) sms++;

                    if (sms != smsNum) {
                        Intent intentSendMessage = new Intent(SendMessageActivity.this, SendMessageActivity.class);
                        intentSendMessage.putExtra("contact", contact_number);
                        startActivity(intentSendMessage);
                        SendMessageActivity.this.finish();
                    }
                }
            });
        }
    }
}
