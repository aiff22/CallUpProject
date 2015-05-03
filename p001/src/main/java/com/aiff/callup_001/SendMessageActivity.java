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
import android.widget.Toast;

import java.util.List;

public class SendMessageActivity extends Activity implements View.OnClickListener {

    int[] colors = new int[2];

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Intent intent = getIntent();
        String contact_number = intent.getStringExtra("contact");
        Log.d("SendMessageActivity", contact_number);

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

    }



    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();

    }

}
