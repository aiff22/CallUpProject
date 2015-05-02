package com.aiff.callup_001;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        for (int i = 0; i < data.size(); i++) {

            List<String> d = data.get(i);

            View item = ltInflater.inflate(R.layout.item_call, linLayout, false);
            item.setOnClickListener(this);

            TextView tvName = (TextView) item.findViewById(R.id.tvName);
            tvName.setText(d.get(1));
            TextView tvPosition = (TextView) item.findViewById(R.id.tvPosition);
            tvPosition.setText(d.get(2));
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
