package com.aiff.callup_001;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CallsActivity extends Activity implements View.OnClickListener {

    final String userLoginKey = "com.aiff.callup_001.app.login";
    final String userPassKey = "com.aiff.callup_001.app.pass";

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls);
        final Context mContext = CallsActivity.this;

        // -- Get calls data -->

        UserData g = UserData.getInstance();
        List<List<String>> data = g.getCalls();

        final LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getLayoutInflater();

        Button btnNewCall = (Button) findViewById(R.id.btnNewCall);
        btnNewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewCall();
            }
        });

        // -- Fill the frames -->

        for (int i = data.size() - 1; i >= 0; i--) {

            final List<String> d = data.get(i);

            final View item = ltInflater.inflate(R.layout.item_call, linLayout, false);
            ImageView imageView = (ImageView) item.findViewById(R.id.callStatus);
            item.setOnClickListener(this);

            TextView tvName = (TextView) item.findViewById(R.id.tvName);
            tvName.setText(d.get(1));
            if (!g.findContact(d.get(1)).equals("-1")) tvName.setText(g.findContact(d.get(1)));

            TextView callDate = (TextView) item.findViewById(R.id.tvPosition);
            callDate.setText(d.get(2));

            ImageView contactStatus = (ImageView) item.findViewById(R.id.contactStatus);
            Integer status = g.findFriendStatus(d.get(1));
            if (status == 1) contactStatus.setImageResource(getResources().getIdentifier("@android:drawable/presence_online", null, null));
            if (status == 0) contactStatus.setImageResource(getResources().getIdentifier("@android:drawable/presence_invisible", null, null));

            if (d.get(3).equals("3"))
                imageView.setImageResource(getResources().getIdentifier("@android:drawable/sym_call_missed", null, null));
            else if (d.get(3).equals("2"))
                imageView.setImageResource(getResources().getIdentifier("@android:drawable/sym_call_incoming", null, null));

            item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

            ImageButton btnMsgs = (ImageButton) item.findViewById(R.id.buttonMsg);
            ImageButton btnCall = (ImageButton) item.findViewById(R.id.buttonCall);
            ImageButton btnAdd = (ImageButton) item.findViewById(R.id.buttonAdd);
            ImageButton btnDel = (ImageButton) item.findViewById(R.id.btnDelete);

            if (g.findContact(d.get(1)).equals("-1"))
                btnAdd.setVisibility(View.VISIBLE);

            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delCall(d.get(1), d.get(3));
                    linLayout.removeView(item);
                    linLayout.refreshDrawableState();
                }
            });

            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContactsActivity.makeCall(d.get(1), mContext);
                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContactsActivity.addNewContact(d.get(1), mContext);
                }
            });

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

    // -- Make options visible or invisible -->

    @Override
    public void onClick(View v) {
        LinearLayout ll = (LinearLayout) v.findViewById(R.id.linearLayout2);
        ll.setBackgroundColor(Color.parseColor("#00000000"));

        if (ll.getVisibility() == View.GONE)
            ll.setVisibility(View.VISIBLE);
        else ll.setVisibility(View.GONE);

    }

    private void delCall(String phone, String type) {
        SharedPreferences prefs = CallsActivity.this.getSharedPreferences(
                "com.aiff.callup_001.app", Context.MODE_PRIVATE);

        String storedLogin = prefs.getString(userLoginKey, new String());
        String storedPass = prefs.getString(userPassKey, new String());

        final String[] args = new String[6];
        args[0] = "delcall";
        args[1] = storedLogin;
        args[2] = storedPass;
        args[3] = phone;
        args[4] = type;
        final UserData g = UserData.getInstance();
        g.delCall(phone, type);

        try {
            String res = new ConnectServer().execute(args).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void makeNewCall() {
        AlertDialog.Builder alert = new AlertDialog.Builder(CallsActivity.this);

        final EditText textPhone = new EditText(CallsActivity.this);
        final Context mContext = CallsActivity.this;

        textPhone.setHint("Phone Number");
        textPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        alert.setTitle("New Call");

        LinearLayout ll = new LinearLayout(CallsActivity.this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(textPhone);
        alert.setView(ll);

        // -- Processing clicks on the buttons -->

        alert.setNegativeButton("Call", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                if (!String.valueOf(textPhone.getText()).equals(""))
                    ContactsActivity.makeCall(String.valueOf(textPhone.getText()), mContext);
                else {
                    Toast.makeText(mContext, "Enter phone number", Toast.LENGTH_SHORT).show();
                    makeNewCall();
                }
            }

        });

        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();

    }
}
