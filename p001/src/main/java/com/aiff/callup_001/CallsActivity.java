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

public class CallsActivity extends Activity implements View.OnClickListener {

    int[] colors = new int[2];


    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls);

        UserData g = UserData.getInstance();
        List<List<String>> data = g.getCalls();
        Log.d("ContactsLogs", String.valueOf(data));
        Log.d("ContactsLogs", String.valueOf(data.size()));

        colors[0] = Color.parseColor("#559966CC");
        colors[1] = Color.parseColor("#55336699");

        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);

        LayoutInflater ltInflater = getLayoutInflater();

        for (int i = data.size() - 1; i >= 0; i--) {

            final List<String> d = data.get(i);

            View item = ltInflater.inflate(R.layout.item_call, linLayout, false);
            item.setOnClickListener(this);

            TextView tvName = (TextView) item.findViewById(R.id.tvName);
            tvName.setText(d.get(1));
            if (!g.findContact(d.get(1)).equals("-1")) tvName.setText(g.findContact(d.get(1)));

            TextView tvPosition = (TextView) item.findViewById(R.id.tvPosition);
            tvPosition.setText(d.get(2));
            TextView tvSalary = (TextView) item.findViewById(R.id.tvSalary);
            tvSalary.setText(d.get(3));

            if (d.get(3).equals("3")){
                tvSalary.setText("missing");
                item.setBackgroundColor(colors[0]);
            }  else {

                if (d.get(3).equals("1"))
                    tvSalary.setText("outcoming");
                else tvSalary.setText("incoming");

                item.setBackgroundColor(colors[1]);
            }


            item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

            Button btnMsgs = (Button) item.findViewById(R.id.button1);
            btnMsgs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentSendMessage = new Intent(CallsActivity.this, SendMessageActivity.class);
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

        if (ll.getVisibility() == View.GONE)
            ll.setVisibility(View.VISIBLE);
        else ll.setVisibility(View.GONE);

    }
}
