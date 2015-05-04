package com.aiff.callup_001;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends Activity implements View.OnClickListener {

    int[] colors = new int[2];

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        UserData g = UserData.getInstance();
        List<List<String>> data = g.getMessages();
        Log.d("ContactsLogs", String.valueOf(data));
        Log.d("ContactsLogs", String.valueOf(data.size()));

        colors[0] = Color.parseColor("#CCFF99");
        colors[1] = Color.parseColor("#FFFFCC");

        Button sendMsg = (Button) findViewById(R.id.button5);
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSendMessage = new Intent(MessagesActivity.this, SendMessageActivity.class);
                intentSendMessage.putExtra("contact", "-1");
                startActivity(intentSendMessage);
            }
        });

        /*ImageButton refresh = (ImageButton) findViewById(R.id.imageButton);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSendMessage = new Intent(MessagesActivity.this, MessagesActivity.class);
                startActivity(intentSendMessage);
                finish();
            }
        });*/

        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);

        LayoutInflater ltInflater = getLayoutInflater();

        List <String> contacts = new ArrayList<>();

        for (int i = data.size() - 1; i >= 0; i--) {

            final List<String> d = data.get(i);

            if (!contacts.contains(d.get(1))) {

                contacts.add(d.get(1));

                View item = ltInflater.inflate(R.layout.item_message, linLayout, false);
                item.setOnClickListener(this);

                TextView tvName = (TextView) item.findViewById(R.id.tvName);

                if (d.get(2).length() > 30)
                    tvName.setText(d.get(2).substring(0, 30) + "...");
                else tvName.setText(d.get(2));

                TextView tvPosition = (TextView) item.findViewById(R.id.tvPosition);
                tvPosition.setText(d.get(1));

                if (!g.findContact(d.get(1)).equals("-1")) tvPosition.setText(g.findContact(d.get(1)));

                TextView tvSalary = (TextView) item.findViewById(R.id.tvSalary);

                if (d.get(3).equals("2")){
                    tvSalary.setText("new");
                    item.setBackgroundColor(colors[0]);
                }  else {

                    if (d.get(3).equals("1"))
                        tvSalary.setText("outcoming");
                    else tvSalary.setText("incoming");

                    item.setBackgroundColor(colors[1]);
                }

                item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

                Button btnMsgs = (Button) item.findViewById(R.id.button);
                btnMsgs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentSendMessage = new Intent(MessagesActivity.this, SendMessageActivity.class);
                        intentSendMessage.putExtra("contact", d.get(1));
                        startActivity(intentSendMessage);
                    }
                });

                linLayout.addView(item);
            }
        }
    }

    @Override
    public void onClick(View v) {
        LinearLayout ll = (LinearLayout) v.findViewById(R.id.linearLayout2);
        //ll.setBackgroundColor(Color.parseColor("#00000000"));

        if (ll.getVisibility() == View.GONE)
            ll.setVisibility(View.VISIBLE);

        else ll.setVisibility(View.GONE);

    }

}
