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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ContactsActivity extends Activity implements View.OnClickListener {

    int[] colors = new int[2];

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    List<String> online = new ArrayList<>();

    final String userLoginKey = "com.aiff.callup_001.app.login";
    final String userPassKey = "com.aiff.callup_001.app.pass";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        final UserData g = UserData.getInstance();
        List<List<String>> data = g.getContacts();
        List<String> online = g.getFriendsOnline();

        Log.d("ContactsLogs", String.valueOf(data));
        Log.d("ContactsLogs", String.valueOf(data.size()));

        colors[0] = Color.parseColor("#FFFFCC");
        //colors[1] = Color.parseColor("#55336699");

        Button addContact = (Button) findViewById(R.id.button5);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ContactsActivity.this);

                final EditText textPhone = new EditText(ContactsActivity.this);
                textPhone.setHint("Phone Number");
                textPhone.setInputType(InputType.TYPE_CLASS_PHONE);

                final EditText textName = new EditText(ContactsActivity.this);
                textName.setHint("Name");

                final CheckBox beFriends = new CheckBox(ContactsActivity.this);
                beFriends.setText("Add to friends");
                beFriends.setChecked(false);

                alert.setTitle("New Contact");

                LinearLayout ll = new LinearLayout(ContactsActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);


                ll.addView(textPhone);
                ll.addView(textName);
                ll.addView(beFriends);
                alert.setView(ll);

                final long[] mLastClickTime = {0};

                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 1000){
                            return;
                        }
                        mLastClickTime[0] = SystemClock.elapsedRealtime();

                        SharedPreferences prefs = ContactsActivity.this.getSharedPreferences(
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

                                    Toast.makeText(ContactsActivity.this, "Contact added", Toast.LENGTH_SHORT).show();
                                    break;

                                case "-1":
                                    Toast.makeText(ContactsActivity.this, "Authentication error. Please, try again later.", Toast.LENGTH_LONG).show();
                                    break;

                                case "-2":
                                    Toast.makeText(ContactsActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();

                                case "2":
                                    Toast.makeText(ContactsActivity.this, "The contact is already in your phone book", Toast.LENGTH_SHORT).show();
                                    break;

                                case "3":
                                    Toast.makeText(ContactsActivity.this, "The contact is already your friend", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        } catch (InterruptedException e) {
                            Toast.makeText(ContactsActivity.this, "System error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            Toast.makeText(ContactsActivity.this, "System error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();
            }
        });

        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);

        LayoutInflater ltInflater = getLayoutInflater();

        int online_counter = 1;

        for (int i = 0; i < data.size(); i++) {

            final List<String> d = data.get(i);

            View item = ltInflater.inflate(R.layout.item_contact, linLayout, false);
            item.setOnClickListener(this);

            TextView tvName = (TextView) item.findViewById(R.id.tvName);
            tvName.setText(d.get(2));

            TextView tvPosition = (TextView) item.findViewById(R.id.tvPosition);
            tvPosition.setText(d.get(1));

            TextView onlineText = (TextView) item.findViewById(R.id.onlineText);
            ImageView onlineImage = (ImageView) item.findViewById(R.id.onlineImage);

            ImageButton btnFriend = (ImageButton) item.findViewById(R.id.button2);

            if (d.get(3).equals("2")) {
                int idDel = getResources().getIdentifier("@android:drawable/ic_delete", null, null);
                btnFriend.setImageResource(idDel);


                Log.d("Boolean", online.get(online_counter));
                if (!Boolean.valueOf(online.get(online_counter))) {
                    onlineText.setText("Offline");
                    int id = getResources().getIdentifier("@android:drawable/presence_invisible", null, null);
                    onlineImage.setImageResource(id);
                } else {
                    onlineText.setText("Online");
                    int id = getResources().getIdentifier("@android:drawable/presence_online", null, null);
                    onlineImage.setImageResource(id);
                }
                online_counter++;
            }

            if (d.get(3).equals("0") || d.get(3).equals("3")) {
                onlineText.setText("Pending");
                int id = getResources().getIdentifier("@android:drawable/presence_offline", null, null);
                onlineImage.setImageResource(id);
            }

            // *** If friend request is received ***

            if (d.get(3).equals("3")) {
                Log.d("ContactsContact", String.valueOf(d));
                LinearLayout ll = (LinearLayout) item.findViewById(R.id.linearLayout3);
                ll.setVisibility(View.VISIBLE);
                final ImageButton contactAccept = (ImageButton) item.findViewById(R.id.buttonAccept);
                final ImageButton contactDecline = (ImageButton) item.findViewById(R.id.buttonDecline);

                contactAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        contactAccept.setEnabled(false);

                        SharedPreferences prefs = ContactsActivity.this.getSharedPreferences(
                                "com.aiff.callup_001.app", Context.MODE_PRIVATE);

                        String storedLogin = prefs.getString(userLoginKey, new String());
                        String storedPass = prefs.getString(userPassKey, new String());

                        String[] args = new String[6];
                        args[0] = "contact";
                        args[1] = storedLogin;
                        args[2] = storedPass;
                        args[3] = d.get(2);
                        args[4] = d.get(1);
                        args[5] = "1";

                        try {
                            String res = new ConnectServer().execute(args).get();
                            if (res.equals("1")) {
                                g.addContact(d.get(1), "confirm");
                                Toast.makeText(ContactsActivity.this, "Contact added", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(ContactsActivity.this, "An error occurred. Please, try again later.", Toast.LENGTH_SHORT).show();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });

                contactDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        contactDecline.setEnabled(false);

                        SharedPreferences prefs = ContactsActivity.this.getSharedPreferences(
                                "com.aiff.callup_001.app", Context.MODE_PRIVATE);

                        String storedLogin = prefs.getString(userLoginKey, new String());
                        String storedPass = prefs.getString(userPassKey, new String());

                        String[] args = new String[6];
                        args[0] = "unfriend";
                        args[1] = storedLogin;
                        args[2] = storedPass;
                        args[3] = d.get(1);

                        try {
                            String res = new ConnectServer().execute(args).get();
                            if (res.equals("1")) {
                                g.addContact(d.get(1), "deleteFromFriends");
                                Toast.makeText(ContactsActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(ContactsActivity.this, "An error occurred. Please, try again later.", Toast.LENGTH_SHORT).show();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
            item.setBackgroundColor(colors[0]);

            // *** Make a call ***

            final ImageButton btnCall = (ImageButton) item.findViewById(R.id.button);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences prefs = ContactsActivity.this.getSharedPreferences(
                            "com.aiff.callup_001.app", Context.MODE_PRIVATE);

                    String storedLogin = prefs.getString(userLoginKey, new String());
                    String storedPass = prefs.getString(userPassKey, new String());

                    String[] args = new String[6];
                    args[0] = "call";
                    args[1] = storedLogin;
                    args[2] = storedPass;
                    args[3] = d.get(1);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ContactsActivity.this);
                    builder.setTitle("Calling to " + d.get(2) + "...");
                    builder.setCancelable(true);

                    final AlertDialog dlg = builder.create();

                    Toast.makeText(ContactsActivity.this, "Call sent", Toast.LENGTH_SHORT).show();
                    dlg.show();

                    try {

                        String res = new ConnectServer().execute(args).get();
                        Toast.makeText(ContactsActivity.this, res, Toast.LENGTH_SHORT).show();
                        Log.d("Room returned", res);

                        if (!res.equals("-1") && !res.equals("-2")) {
                            dlg.dismiss();
                            Intent intentStartCall = new Intent(ContactsActivity.this, CallingActivity.class);
                            intentStartCall.putExtra("contact", d.get(1));
                            intentStartCall.putExtra("room", res);
                            startActivity(intentStartCall);
                        } else {
                            if (res.equals("-1"))
                                Toast.makeText(ContactsActivity.this, "No response", Toast.LENGTH_SHORT).show();
                            if (res.equals("-2"))
                                Toast.makeText(ContactsActivity.this, "Contact is busy", Toast.LENGTH_SHORT).show();
                            else Toast.makeText(ContactsActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {
                        Toast.makeText(ContactsActivity.this, "System error occurred", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        Toast.makeText(ContactsActivity.this, "System error occurred", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            });

            // *** Send message ***

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

            // *** Add/Delete to friends ***

            btnFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(ContactsActivity.this);

                    SharedPreferences prefs = ContactsActivity.this.getSharedPreferences(
                            "com.aiff.callup_001.app", Context.MODE_PRIVATE);

                    String storedLogin = prefs.getString(userLoginKey, new String());
                    String storedPass = prefs.getString(userPassKey, new String());

                    final String[] args = new String[6];
                    args[1] = storedLogin;
                    args[2] = storedPass;

                    if (d.get(3).equals("2")) {
                        alert.setTitle("Delete from friends?");
                        args[0] = "unfriend";
                        args[3] = d.get(1);
                    } else {
                        alert.setTitle("Add to friends?");
                        args[0] = "contact";
                        args[3] = d.get(2);
                        args[4] = d.get(1);
                        args[5] = "1";
                    }

                    final long [] mLastClickTime = {0};

                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 1000){
                                return;
                            }
                            mLastClickTime[0] = SystemClock.elapsedRealtime();

                            try {
                                String res = new ConnectServer().execute(args).get();
                                if (res.equals("1")) {

                                    if (args[0].equals("contact"))
                                        g.addContact(d.get(1), "add");
                                    else
                                        g.addContact(d.get(1), "deleteFromFriends");

                                    Toast.makeText(ContactsActivity.this, "Contact successfully " + (args[0].equals("contact") ? "added" : "deleted"), Toast.LENGTH_SHORT).show();
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
            });

            // *** Delete contact ***

            final ImageButton btnDelete = (ImageButton) item.findViewById(R.id.button3);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btnDelete.setEnabled(false);

                    AlertDialog.Builder alert = new AlertDialog.Builder(ContactsActivity.this);

                    SharedPreferences prefs = ContactsActivity.this.getSharedPreferences(
                            "com.aiff.callup_001.app", Context.MODE_PRIVATE);

                    String storedLogin = prefs.getString(userLoginKey, new String());
                    String storedPass = prefs.getString(userPassKey, new String());

                    final String[] args = new String[6];
                    args[0] = "delcontact";
                    args[1] = storedLogin;
                    args[2] = storedPass;
                    args[3] = d.get(1);

                    alert.setTitle("Delete Contact?");

                    final long[] mLastClickTime = {0};

                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 1000){
                                return;
                            }
                            mLastClickTime[0] = SystemClock.elapsedRealtime();

                            try {
                                String res = new ConnectServer().execute(args).get();
                                if (res.equals("1")) {
                                    g.addContact(d.get(1), "delete");
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
            });

            linLayout.addView(item);
        }

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 5000, 5000);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    SharedPreferences prefs = ContactsActivity.this.getSharedPreferences(
                            "com.aiff.callup_001.app", Context.MODE_PRIVATE);

                    final String userLoginKey = "com.aiff.callup_001.app.login";
                    final String userPassKey = "com.aiff.callup_001.app.pass";

                    String storedLogin = prefs.getString(userLoginKey, new String());
                    String storedPass = prefs.getString(userPassKey, new String());

                    final String[] args = new String[6];
                    args[0] = "hello";
                    args[1] = storedLogin;
                    args[2] = storedPass;
                    String res = null;

                    try {
                        res = new ConnectServer().execute(args).get();

                        UserData g = UserData.getInstance();
                        List<List<String>> data = g.getNewEvents();

                        for (int i = 0; i < data.size(); i++) {

                            final List<String> d = new ArrayList<String>(data.get(i));
                            if (d.get(0).equals("events")) {

                                switch (d.get(3)) {

                                    case "1":   // new message
                                        d.set(0, "messages");
                                        d.set(3, "2");
                                        List<List<String>> addData1 = new ArrayList<>();
                                        addData1.add(d);
                                        g.setData(addData1);

                                        Toast.makeText(ContactsActivity.this, "New incoming message", Toast.LENGTH_SHORT).show();
                                        Log.d("ContactsLogs:events", String.valueOf(g.getEvents()));

                                        break;

                                    case "2":   // new call

                                        args[0] = "respcall";
                                        args[1] = storedLogin;
                                        args[2] = storedPass;
                                        args[3] = d.get(1);

                                        AlertDialog.Builder alert = new AlertDialog.Builder(ContactsActivity.this);

                                        alert.setTitle(d.get(1) + " is calling...");
                                        if (!g.findContact(d.get(1)).equals("-1"))
                                            alert.setTitle(g.findContact(d.get(1)) + " is calling...");

                                        alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                args[4] = "1";
                                                try {
                                                    String res = new ConnectServer().execute(args).get();


                                                    if (!res.equals("-1") && !res.equals("-3")){
                                                        Intent intentStartCall = new Intent(ContactsActivity.this, CallingActivity.class);
                                                        intentStartCall.putExtra("contact", d.get(1));
                                                        intentStartCall.putExtra("room", res);
                                                        startActivity(intentStartCall);
                                                    } else {
                                                        Toast.makeText(ContactsActivity.this, "Call cancelled", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                } catch (ExecutionException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        alert.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                args[4] = "0";
                                                try {
                                                    String res = new ConnectServer().execute(args).get();
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                } catch (ExecutionException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        alert.show();

                                        break;

                                    case "3":   // missing call
                                        d.set(0, "calls");
                                        d.set(2, d.get(4));
                                        d.set(3, "3");
                                        d.remove(4);
                                        List<List<String>> addData3 = new ArrayList<>();
                                        addData3.add(d);
                                        g.setData(addData3);

                                        Toast.makeText(ContactsActivity.this, "Missing call", Toast.LENGTH_SHORT).show();
                                        break;

                                    case "4":   // new contact

                                        if(g.findContact(d.get(1)).equals("-1")){
                                            d.set(0, "contacts");
                                            d.set(3, "3");
                                            d.remove(4);
                                            List<List<String>> addData4 = new ArrayList<>();
                                            addData4.add(d);
                                            g.setData(addData4);
                                        } else g.addContact(d.get(1), "contactRequest");

                                        Toast.makeText(ContactsActivity.this, "New contact request", Toast.LENGTH_SHORT).show();
                                        break;

                                    case "5":
                                        g.addContact(d.get(1), "confirm");
                                        Toast.makeText(ContactsActivity.this, "Contact " + String.valueOf(g.findContact(d.get(1))) +
                                                " accepted your friend request", Toast.LENGTH_SHORT).show();
                                        break;

                                    case "6":
                                        g.addContact(d.get(1), "deleteFromFriends");
                                        break;

                                }
                            } if (d.get(0).equals("friends_online")) {
                                List<List<String>> addContacts = new ArrayList<>();
                                addContacts.add(d);
                                g.setData(addContacts);
//                                TabActivity tab = (TabActivity) getParent();
//                                tab.getTabHost().setCurrentTabByTag("tag2");
//                                tab.getTabHost().setCurrentTabByTag("tag1");
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

    }

    @Override
    public void onClick(View v) {

        LinearLayout ll = (LinearLayout) v.findViewById(R.id.linearLayout2);
        ll.setBackgroundColor(Color.parseColor("#00000000"));

        //Log.d("ContactsLogs", String.valueOf(ll.getVisibility()));

        if (ll.getVisibility() == View.GONE) {
            ll.setVisibility(View.VISIBLE);
        } else ll.setVisibility(View.GONE);

        //Log.d("ContactsLogs", String.valueOf(ll.getVisibility()));
    }
}
