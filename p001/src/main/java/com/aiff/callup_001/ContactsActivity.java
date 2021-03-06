package com.aiff.callup_001;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ContactsActivity extends Activity implements View.OnClickListener {

    // -- The path where user login and password are located -->

    final String userLoginKey = "com.aiff.callup_001.app.login";
    final String userPassKey = "com.aiff.callup_001.app.pass";

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        final Activity mContext = ContactsActivity.this;

        // -- Get data about contacts -->

        final UserData g = UserData.getInstance();
        List<List<String>> data = g.getContacts();
        List<String> online = g.getFriendsOnline();

        Log.d("ContactsLogs", String.valueOf(data));
        Log.d("ContactsLogs", String.valueOf(data.size()));

        // -- Adding new contact -->

        Button addContact = (Button) findViewById(R.id.button5);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewContact("", mContext);
            }
        });

        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getLayoutInflater();

        int online_counter = 1;

        // -- Fill contacts frames -->

        for (int i = 0; i < data.size(); i++) {

            final List<String> d = data.get(i);

            View item = ltInflater.inflate(R.layout.item_contact, linLayout, false);
            TextView contactName = (TextView) item.findViewById(R.id.tvName);
            TextView contactPhone = (TextView) item.findViewById(R.id.tvPosition);
            TextView onlineText = (TextView) item.findViewById(R.id.onlineText);
            ImageView onlineImage = (ImageView) item.findViewById(R.id.onlineImage);
            ImageButton btnFriend = (ImageButton) item.findViewById(R.id.button2);

            contactName.setText(d.get(2));
            contactPhone.setText(d.get(1));
            item.setOnClickListener(this);

            if (d.get(3).equals("2")) {
                btnFriend.setImageResource(getResources().getIdentifier("@android:drawable/ic_delete", null, null));

                if (!Boolean.valueOf(online.get(online_counter))) {
                    onlineText.setText("Offline");
                    onlineImage.setImageResource(getResources().getIdentifier("@android:drawable/presence_invisible", null, null));
                } else {
                    onlineText.setText("Online");
                    onlineImage.setImageResource(getResources().getIdentifier("@android:drawable/presence_online", null, null));
                }
                online_counter++;
            }

            if (d.get(3).equals("0") || d.get(3).equals("3")) {
                onlineText.setText("");
                onlineImage.setImageResource(getResources().getIdentifier("@android:drawable/presence_offline", null, null));
            }

            // -- If friend request is received then show it -->

            if (d.get(3).equals("3")) {

                btnFriend.setVisibility(View.GONE);

                LinearLayout ll = (LinearLayout) item.findViewById(R.id.linearLayout3);
                ll.setVisibility(View.VISIBLE);

                final ImageButton contactAccept = (ImageButton) item.findViewById(R.id.buttonAccept);
                final ImageButton contactDecline = (ImageButton) item.findViewById(R.id.buttonDecline);

                contactAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contactAccept.setEnabled(false);
                        acceptContact(d.get(2), d.get(1));
                    }
                });

                contactDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contactDecline.setEnabled(false);
                        declineContact(d.get(1));
                    }
                });
            }

            item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
            //item.setBackgroundColor(colors[0]);

            // -- Make a call -->

            final ImageButton btnCall = (ImageButton) item.findViewById(R.id.button);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeCall(d.get(1), mContext);
                }
            });

            // -- Send message -->

            final ImageButton btnMsgs = (ImageButton) item.findViewById(R.id.button1);
            btnMsgs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnMsgs.setEnabled(false);
                    Intent intentSendMessage = new Intent(ContactsActivity.this, SendMessageActivity.class);
                    intentSendMessage.putExtra("contact", d.get(1));
                    startActivity(intentSendMessage);
                }
            });

            // -- Add/Delete friend -->

            btnFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToFriends(d.get(1), d.get(2), d.get(3));
                }
            });

            // -- Delete contact -->

            final ImageButton btnDelete = (ImageButton) item.findViewById(R.id.button3);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnDelete.setEnabled(false);
                    deleteContact(d.get(1));
                }
            });

            linLayout.addView(item);
        }

        // -- Add timer to check for new events -->

    }

    // -- Timer for checking new events -->



    // -- Change visibility of the options -->

    @Override
    public void onClick(View v) {

        LinearLayout ll = (LinearLayout) v.findViewById(R.id.linearLayout2);
        ll.setBackgroundColor(Color.parseColor("#00000000"));

        if (ll.getVisibility() == View.GONE) {
            ll.setVisibility(View.VISIBLE);
        } else ll.setVisibility(View.GONE);

    }

    // -- Adding new contact -->

    public static void addNewContact(String phone, final Context mContext) {

        // -- Creating new dialog -->

        final String userLoginKey = "com.aiff.callup_001.app.login";
        final String userPassKey = "com.aiff.callup_001.app.pass";

        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

        final EditText textPhone = new EditText(mContext);
        final EditText textName = new EditText(mContext);
        final CheckBox beFriends = new CheckBox(mContext);

        textPhone.setHint("Phone Number");
        textPhone.setText(phone);
        textPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        textName.setHint("Name");
        beFriends.setText("Add to friends");
        beFriends.setChecked(false);
        alert.setTitle("New Contact");

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(textPhone);
        ll.addView(textName);
        ll.addView(beFriends);
        alert.setView(ll);

        // -- Processing clicks on the buttons -->

        final long[] mLastClickTime = {0};

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                // -- Prevent double clicking -->

                if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 1000) return;
                mLastClickTime[0] = SystemClock.elapsedRealtime();

                // -- Get user authentication data -->

                SharedPreferences prefs = mContext.getSharedPreferences(
                        "com.aiff.callup_001.app", Context.MODE_PRIVATE);

                String storedLogin = prefs.getString(userLoginKey, new String());
                String storedPass = prefs.getString(userPassKey, new String());

                String[] args = new String[6];
                args[0] = "contact";
                args[1] = storedLogin;
                args[2] = storedPass;
                args[3] = String.valueOf(textName.getText());
                args[4] = String.valueOf(textPhone.getText());
                if (beFriends.isChecked()) args[5] = "1";
                else args[5] = "0";

                final UserData g = UserData.getInstance();

                // -- Send add contact request -->

                if (!args[4].equals(storedLogin) && !args[3].equals("") && !args[4].equals("")) {

                    try {

                        String res = new ConnectServer().execute(args).get();
                        switch (res) {

                            case "1":
                                List<List<String>> data = new ArrayList<List<String>>();
                                List<String> d = new ArrayList<String>();

                                d.add("contacts");
                                d.add(args[4]);
                                d.add(args[3]);
                                d.add(args[3].equals("1") ? "0" : "1");
                                data.add(d);
                                g.setData(data);

                                Toast.makeText(mContext, "Contact added", Toast.LENGTH_SHORT).show();
                                break;

                            case "2":
                                Toast.makeText(mContext, "The contact is already in your phone book", Toast.LENGTH_SHORT).show();
                                break;

                            case "3":
                                Toast.makeText(mContext, "The contact is already your friend", Toast.LENGTH_SHORT).show();
                                break;

                            case "-1":
                                Toast.makeText(mContext, "Authentication error. Please, try again later", Toast.LENGTH_LONG).show();
                                break;

                            case "-2":
                                Toast.makeText(mContext, "User does not exist", Toast.LENGTH_SHORT).show();
                                addNewContact("", mContext);
                        }

                    } catch (InterruptedException e) {
                        Toast.makeText(mContext, "System error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        Toast.makeText(mContext, "System error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    if (args[4].equals(storedLogin)) {
                        Toast.makeText(mContext, "You can not add yourself to contacts", Toast.LENGTH_SHORT).show();
                        args[4] = "";
                    } else if (args[4].equals(""))
                        Toast.makeText(mContext, "Enter contact's phone number", Toast.LENGTH_SHORT).show();
                    else if (args[3].equals(""))
                        Toast.makeText(mContext, "Enter contact's name", Toast.LENGTH_SHORT).show();
                    addNewContact(args[4], mContext);
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();

    }

    private void acceptContact(String name, String phone) {

        SharedPreferences prefs = ContactsActivity.this.getSharedPreferences(
                "com.aiff.callup_001.app", Context.MODE_PRIVATE);

        String storedLogin = prefs.getString(userLoginKey, new String());
        String storedPass = prefs.getString(userPassKey, new String());

        String[] args = new String[6];
        args[0] = "contact";
        args[1] = storedLogin;
        args[2] = storedPass;
        args[3] = name;
        args[4] = phone;
        args[5] = "1";
        final UserData g = UserData.getInstance();

        try {
            String res = new ConnectServer().execute(args).get();

            if (res.equals("1")) {
                g.addContact(phone, "confirm");
                Toast.makeText(ContactsActivity.this, "Contact added", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(ContactsActivity.this, "An error occurred. Please, try again later.", Toast.LENGTH_SHORT).show();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void declineContact(String phone) {

        SharedPreferences prefs = ContactsActivity.this.getSharedPreferences(
                "com.aiff.callup_001.app", Context.MODE_PRIVATE);

        String storedLogin = prefs.getString(userLoginKey, new String());
        String storedPass = prefs.getString(userPassKey, new String());

        String[] args = new String[6];
        args[0] = "unfriend";
        args[1] = storedLogin;
        args[2] = storedPass;
        args[3] = phone;
        final UserData g = UserData.getInstance();

        try {
            String res = new ConnectServer().execute(args).get();
            if (res.equals("1")) {
                g.addContact(phone, "deleteFromFriends");
                Toast.makeText(ContactsActivity.this, "Request declined", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(ContactsActivity.this, "An error occurred. Please, try again later.", Toast.LENGTH_SHORT).show();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public static void makeCall(final String phone, final Activity mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences(
                "com.aiff.callup_001.app", Context.MODE_PRIVATE);

        final String userLoginKey = "com.aiff.callup_001.app.login";
        final String userPassKey = "com.aiff.callup_001.app.pass";

        String storedLogin = prefs.getString(userLoginKey, new String());
        String storedPass = prefs.getString(userPassKey, new String());

        final String[] args = new String[6];
        args[0] = "call";
        args[1] = storedLogin;
        args[2] = storedPass;
        args[3] = phone;
        final UserData g = UserData.getInstance();

        if (phone.equals(storedLogin)) {
            Toast.makeText(mContext, "Calling yourself?\n Ha-Ha *_*", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Calling to " + phone + "...");
        if (!g.findContact(phone).equals("-1"))
            builder.setTitle("Calling to " + g.findContact(phone) + "...");
        builder.setCancelable(true);

        final AlertDialog dlg = builder.create();
        dlg.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    List<List<String>> data = new ArrayList<List<String>>();
                    List<String> d = new ArrayList<String>();

                    d.add("calls");
                    d.add(phone);
                    d.add(String.valueOf(new Timestamp(new Date().getTime())));
                    d.add("1");
                    data.add(d);

                    g.delCall(phone, "1");
                    g.setData(data);

                    final String res = new ConnectServer().execute(args).get();
                    Log.d("Room returned", res);

                    if (!res.equals("-1") && !res.equals("-2") && !res.equals("-2") && !res.equals("-4")) {
                        dlg.dismiss();
                        Intent intentStartCall = new Intent(mContext, CallingActivity.class);
                        intentStartCall.putExtra("contact", phone);
                        intentStartCall.putExtra("room", res);
                        mContext.startActivity(intentStartCall);

                    } else {
                       mContext.runOnUiThread(new Runnable() {
                            public void run() {


                        dlg.dismiss();
                        if (res.equals("-1"))
                            Toast.makeText(mContext, "No response", Toast.LENGTH_SHORT).show();
                        else if (res.equals("-2"))
                            Toast.makeText(mContext, "Contact is busy", Toast.LENGTH_SHORT).show();
                        else if (res.equals("-3"))
                            Toast.makeText(mContext, "Authentication error. Please, try again later", Toast.LENGTH_SHORT).show();
                        else if (res.equals("-4"))
                            Toast.makeText(mContext, "Contact not found", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(mContext, "An error occurred", Toast.LENGTH_SHORT).show();
                            }
                       });
                    }

                } catch (InterruptedException e) {
                    Toast.makeText(mContext, "System error occurred", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Toast.makeText(mContext, "System error occurred", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private void addToFriends(final String phone, String name, String type) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ContactsActivity.this);

        SharedPreferences prefs = ContactsActivity.this.getSharedPreferences(
                "com.aiff.callup_001.app", Context.MODE_PRIVATE);

        String storedLogin = prefs.getString(userLoginKey, new String());
        String storedPass = prefs.getString(userPassKey, new String());

        final String[] args = new String[6];
        args[1] = storedLogin;
        args[2] = storedPass;
        final UserData g = UserData.getInstance();

        if (type.equals("2")) {
            alert.setTitle("Delete from friends?");
            args[0] = "unfriend";
            args[3] = phone;
        } else {
            alert.setTitle("Add to friends?");
            args[0] = "contact";
            args[3] = name;
            args[4] = phone;
            args[5] = "1";
        }

        final long[] mLastClickTime = {0};

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 1000) return;
                mLastClickTime[0] = SystemClock.elapsedRealtime();

                try {
                    String res = new ConnectServer().execute(args).get();
                    if (res.equals("1")) {

                        if (args[0].equals("contact"))
                            g.addContact(phone, "add");
                        else
                            g.addContact(phone, "deleteFromFriends");

                        Toast.makeText(ContactsActivity.this, "Contact successfully " + (args[0].equals("contact") ? "added" : "deleted"), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(ContactsActivity.this, "An error occurred. Please, try again later", Toast.LENGTH_SHORT).show();

                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    private void deleteContact(final String phone) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ContactsActivity.this);

        SharedPreferences prefs = ContactsActivity.this.getSharedPreferences(
                "com.aiff.callup_001.app", Context.MODE_PRIVATE);

        String storedLogin = prefs.getString(userLoginKey, new String());
        String storedPass = prefs.getString(userPassKey, new String());

        final String[] args = new String[6];
        args[0] = "delcontact";
        args[1] = storedLogin;
        args[2] = storedPass;
        args[3] = phone;
        final UserData g = UserData.getInstance();

        alert.setTitle("Delete Contact?");

        final long[] mLastClickTime = {0};

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 1000) return;
                mLastClickTime[0] = SystemClock.elapsedRealtime();

                try {
                    String res = new ConnectServer().execute(args).get();
                    if (res.equals("1")) {
                        g.addContact(phone, "delete");
                        Toast.makeText(ContactsActivity.this, "Contact successfully deleted", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(ContactsActivity.this, "An error occurred. Please, try again later.", Toast.LENGTH_SHORT).show();

                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

}

