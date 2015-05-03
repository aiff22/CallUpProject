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
import android.widget.LinearLayout;
import android.widget.TextView;

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

        colors[0] = Color.parseColor("#559966CC");
        colors[1] = Color.parseColor("#55336699");

        Button sendMsg = (Button) findViewById(R.id.button5);
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSendMessage = new Intent(MessagesActivity.this, SendMessageActivity.class);
                intentSendMessage.putExtra("contact", "-1");
                startActivity(intentSendMessage);
            }
        });

        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);

        LayoutInflater ltInflater = getLayoutInflater();

        for (int i = data.size() - 1; i >= 0; i--) {

            List<String> d = data.get(i);

            View item = ltInflater.inflate(R.layout.item_message, linLayout, false);
            item.setOnClickListener(this);

            TextView tvName = (TextView) item.findViewById(R.id.tvName);

            if (d.get(2).length() > 30)
                tvName.setText(d.get(2).substring(0, 30) + "...");
            else tvName.setText(d.get(2));

            TextView tvPosition = (TextView) item.findViewById(R.id.tvPosition);
            tvPosition.setText(d.get(1));
            TextView tvSalary = (TextView) item.findViewById(R.id.tvSalary);
            tvSalary.setText(d.get(3));
            item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            item.setBackgroundColor(colors[i % 2]);
            linLayout.addView(item);
        }
    }



    @Override
    public void onClick(View v) {
        LinearLayout ll = (LinearLayout) v.findViewById(R.id.linearLayout2);
        ll.setBackgroundColor(Color.parseColor("#00000000"));

        if (ll.getVisibility() == View.GONE)
            ll.setVisibility(View.VISIBLE);
        else ll.setVisibility(View.GONE);

    }

}
