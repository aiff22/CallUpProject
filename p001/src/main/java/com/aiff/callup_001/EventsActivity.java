package com.aiff.callup_001;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        final UserData g = UserData.getInstance();
        List<List<String>> data = g.getEvents();
        Log.d("ContactsLogs", String.valueOf(data));
        Log.d("ContactsLogs", String.valueOf(data.size()));

        final LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);

        LayoutInflater ltInflater = getLayoutInflater();

        List<String> contacts_messages = new ArrayList<>();

        for (int i = data.size() - 1; i >= 0; i--) {

            final List<String> d = data.get(i);

            Log.d("EventsActivity; type", d.get(3));

            if (d.get(3).equals("1")) {

                if (!contacts_messages.contains(d.get(1))) {

                    contacts_messages.add(d.get(1));

                    final View item = ltInflater.inflate(R.layout.item_message, linLayout, false);
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentSendMessage = new Intent(EventsActivity.this, SendMessageActivity.class);
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
                            g.delEvent(d.get(1), d.get(3));
                            linLayout.removeView(item);
                            linLayout.refreshDrawableState();
                        }
                    });

                }

            } else {

                if (d.get(3).equals("3")){

                    final View item = ltInflater.inflate(R.layout.item_call, linLayout, false);
                    ImageView imageView = (ImageView) item.findViewById(R.id.callStatus);

                    TextView tvName = (TextView) item.findViewById(R.id.tvName);
                    tvName.setText(d.get(1));
                    if (!g.findContact(d.get(1)).equals("-1")) tvName.setText(g.findContact(d.get(1)));

                    TextView callDate = (TextView) item.findViewById(R.id.tvPosition);
                    callDate.setText(d.get(2));

                    ImageView contactStatus = (ImageView) item.findViewById(R.id.contactStatus);
                    Integer status = g.findFriendStatus(d.get(1));
                    if (status == 1) contactStatus.setImageResource(getResources().getIdentifier("@android:drawable/presence_online", null, null));
                    if (status == 0) contactStatus.setImageResource(getResources().getIdentifier("@android:drawable/presence_invisible", null, null));

                    imageView.setImageResource(getResources().getIdentifier("@android:drawable/sym_call_missed", null, null));

                    item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    ImageButton btnDel = (ImageButton) item.findViewById(R.id.btnDelete);

                    btnDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            g.delEvent(d.get(1), d.get(3));
                            linLayout.removeView(item);
                            linLayout.refreshDrawableState();
                        }
                    });

                    linLayout.addView(item);
                } else {

                    final View item = ltInflater.inflate(R.layout.event_item_contact, linLayout, false);
                    TextView contactName = (TextView) item.findViewById(R.id.tvName);
                    TextView contactPhone = (TextView) item.findViewById(R.id.tvPosition);

                    contactName.setText(d.get(2));
                    contactPhone.setText(d.get(1));

                    ImageButton btnDelete = (ImageButton) item.findViewById(R.id.btnDelete);
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            g.delEvent(d.get(1), d.get(3));
                            linLayout.removeView(item);
                            linLayout.refreshDrawableState();
                        }
                    });

                    item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    linLayout.addView(item);
                }
            }
        }
    }
}
