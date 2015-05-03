package com.aiff.callup_001;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ContactsActivity extends Activity implements View.OnClickListener {

    int[] colors = new int[2];


    /** Called when the activity is first created. */
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
    }



    @Override
    public void onClick(View v) {

        LinearLayout ll = (LinearLayout) v.findViewById(R.id.linearLayout2);
        ll.setBackgroundColor(Color.parseColor("#00000000"));

        //Log.d("ContactsLogs", String.valueOf(ll.getVisibility()));

        if (ll.getVisibility() == View.GONE) {
            ll.setVisibility(View.VISIBLE);
        }

        else ll.setVisibility(View.GONE);

        //Log.d("ContactsLogs", String.valueOf(ll.getVisibility()));
    }
}
