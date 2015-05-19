package com.aiff.callup_001;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MessagesActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        UserData g = UserData.getInstance();
        List<List<String>> data = g.getMessages();
        Log.d("ContactsLogs", String.valueOf(data));
        Log.d("ContactsLogs", String.valueOf(data.size()));

        Button sendMsg = (Button) findViewById(R.id.btnNewMsg);
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSendMessage = new Intent(MessagesActivity.this, SendMessageActivity.class);
                intentSendMessage.putExtra("contact", "-1");
                startActivity(intentSendMessage);
            }
        });

        final LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);

        LayoutInflater ltInflater = getLayoutInflater();

        List<String> contacts = new ArrayList<>();

        for (int i = data.size() - 1; i >= 0; i--) {

            final List<String> d = data.get(i);

            if (!contacts.contains(d.get(1))) {

                contacts.add(d.get(1));

                final View item = ltInflater.inflate(R.layout.item_message, linLayout, false);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentSendMessage = new Intent(MessagesActivity.this, SendMessageActivity.class);
                        intentSendMessage.putExtra("contact", d.get(1));
                        startActivity(intentSendMessage);
                    }
                });

                linLayout.addView(item);


                TextView contactName = (TextView) item.findViewById(R.id.tvName);
                TextView textMessage = (TextView) item.findViewById(R.id.tvPosition);

                if (d.get(2).length() > 26)
                    textMessage.setText(d.get(2).substring(0, 26) + "...");
                else textMessage.setText(d.get(2));

                contactName.setText(d.get(1));
                ImageView imageView = (ImageView) findViewById(R.id.msgStatus);

                if (!g.findContact(d.get(1)).equals("-1"))
                    contactName.setText(g.findContact(d.get(1)));

                if (d.get(3).equals("2"))
                    imageView.setImageResource(R.drawable.new_message);

                ImageView contactStatus = (ImageView) item.findViewById(R.id.contactStatus);
                Integer status = g.findFriendStatus(d.get(1));
                if (status == 1)
                    contactStatus.setImageResource(getResources().getIdentifier("@android:drawable/presence_online", null, null));
                if (status == 0)
                    contactStatus.setImageResource(getResources().getIdentifier("@android:drawable/presence_invisible", null, null));

                item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

                ImageButton btnDelete = (ImageButton) item.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delMsg(d.get(1), d.get(3));
                        linLayout.removeView(item);
                        linLayout.refreshDrawableState();
                    }
                });
            }
        }

    }

    private void delMsg(String phone, String type) {

        final String userLoginKey = "com.aiff.callup_001.app.login";
        final String userPassKey = "com.aiff.callup_001.app.pass";

        SharedPreferences prefs = MessagesActivity.this.getSharedPreferences(
                "com.aiff.callup_001.app", Context.MODE_PRIVATE);

        String storedLogin = prefs.getString(userLoginKey, new String());
        String storedPass = prefs.getString(userPassKey, new String());

        final String[] args = new String[6];
        args[0] = "delmsg";
        args[1] = storedLogin;
        args[2] = storedPass;
        args[3] = phone;
        args[4] = type;
        final UserData g = UserData.getInstance();
        g.delMsg(phone, type);

        try {
            String res = new ConnectServer().execute(args).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
