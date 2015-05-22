package com.aiff.callup_001;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainPageActivity extends TabActivity {
    /** Called when the activity is first created. */

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_page);

        // получаем TabHost
        TabHost tabHost = getTabHost();

        // инициализация была выполнена в getTabHost
        // метод setup вызывать не нужно

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.business49));
        tabSpec.setContent(new Intent(this, ContactsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.telephonecall));
        tabSpec.setContent(new Intent(this, CallsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.comments16));
        tabSpec.setContent(new Intent(this, MessagesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.buy7));
        tabSpec.setContent(new Intent(this, EventsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(tabSpec);

        // -- Process exit button -->

        ImageButton btnExit = (ImageButton) findViewById(R.id.shutdown);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(MainPageActivity.this);

                alert.setNeutralButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });

                //alert.setNegativeButton("Log Out", new DialogInterface.OnClickListener() {
                //    @Override
                //    public void onClick(DialogInterface dialog, int which) {
                //        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //        intent.putExtra("EXIT", true);

                //        startActivity(intent);
                //    }
                //});

                alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();

            }
        });

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

                    SharedPreferences prefs = MainPageActivity.this.getSharedPreferences(
                            "com.aiff.callup_001.app", Context.MODE_PRIVATE);

                    final String userLoginKey = "com.aiff.callup_001.app.login";
                    final String userPassKey = "com.aiff.callup_001.app.pass";

                    String storedLogin = prefs.getString(userLoginKey, new String());
                    String storedPass = prefs.getString(userPassKey, new String());

                    final String[] args = new String[6];
                    args[0] = "hello";
                    args[1] = storedLogin;
                    args[2] = storedPass;

                    try {

                        String res = new ConnectServer().execute(args).get();

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

                                        Toast.makeText(MainPageActivity.this, "New incoming message", Toast.LENGTH_SHORT).show();
                                        Log.d("ContactsLogs:events", String.valueOf(g.getEvents()));

                                        break;

                                    case "2":   // new incoming call
                                        callResponse(d.get(1), d.get(4));
                                        break;

                                    case "3":   // missing call
                                        d.set(0, "calls");
                                        d.set(2, d.get(4));
                                        d.set(3, "3");
                                        d.remove(4);
                                        List<List<String>> addData3 = new ArrayList<>();
                                        addData3.add(d);
                                        g.setData(addData3);

                                        Toast.makeText(MainPageActivity.this, "Missing call", Toast.LENGTH_SHORT).show();
                                        break;

                                    case "4":   // new contact

                                        if (g.findContact(d.get(1)).equals("-1")) {
                                            d.set(0, "contacts");
                                            d.set(3, "3");
                                            d.remove(4);
                                            List<List<String>> addData4 = new ArrayList<>();
                                            addData4.add(d);
                                            g.setData(addData4);
                                        } else g.addContact(d.get(1), "contactRequest");

                                        Toast.makeText(MainPageActivity.this, "New contact request", Toast.LENGTH_SHORT).show();
                                        break;

                                    case "5":
                                        g.addContact(d.get(1), "confirm");
                                        Toast.makeText(MainPageActivity.this, "Contact " + String.valueOf(g.findContact(d.get(1))) +
                                                " accepted your friend request", Toast.LENGTH_SHORT).show();
                                        break;

                                    case "6":
                                        g.addContact(d.get(1), "deleteFromFriends");
                                        break;

                                }
                            }
                            if (d.get(0).equals("friends_online")) {
                                List<List<String>> addContacts = new ArrayList<>();
                                addContacts.add(d);
                                //g.setData(addContacts);
                                List<String> lastOnline = g.getFriendsOnline();

                                if (!lastOnline.equals(d)) {
                                    g.setData(addContacts);
                                    Log.d("ContactsActivity", "updateContactStatus");
                                    Log.d("last", d.toString());
                                    Log.d("last", lastOnline.toString());

                                    TabHost tabHost = getTabHost();
                                    String tag = tabHost.getCurrentTabTag();
                                    tabHost.setCurrentTabByTag("tag4");
                                    tabHost.setCurrentTabByTag(tag);
                                    //TabActivity tab = (TabActivity) getParent();
                                    //String tag = tab.getTabHost().getCurrentTabTag();
                                    Log.d("ContactsActivity", tag);
                                    //tab.getTabHost().setCurrentTabByTag("tag4");
                                    //tab.getTabHost().setCurrentTabByTag(tag);
                                }

                                //          TabActivity tab = (TabActivity) getParent();
                                //          tab.getTabHost().setCurrentTabByTag("tag2");
                                //          tab.getTabHost().setCurrentTabByTag("tag1");
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

    private void callResponse(final String phone, String time) {

        SharedPreferences prefs = MainPageActivity.this.getSharedPreferences(
                "com.aiff.callup_001.app", Context.MODE_PRIVATE);

        final String userLoginKey = "com.aiff.callup_001.app.login";
        final String userPassKey = "com.aiff.callup_001.app.pass";

        String storedLogin = prefs.getString(userLoginKey, new String());
        String storedPass = prefs.getString(userPassKey, new String());

        final String[] args = new String[6];
        args[0] = "respcall";
        args[1] = storedLogin;
        args[2] = storedPass;
        args[3] = phone;
        final UserData g = UserData.getInstance();

        AlertDialog.Builder alert = new AlertDialog.Builder(MainPageActivity.this);

        alert.setTitle(phone + " is calling...");
        if (!g.findContact(phone).equals("-1"))
            alert.setTitle(g.findContact(phone) + " is calling...");

        final List<List<String>> data = new ArrayList<List<String>>();
        final List<String> d = new ArrayList<String>();

        d.add("calls");
        d.add(phone);
        d.add(time);

        g.delCall(phone, "2");
        g.delCall(phone, "3");

        alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                args[4] = "1";
                d.add("2");
                data.add(d);
                g.setData(data);

                try {

                    String res = new ConnectServer().execute(args).get();


                    if (!res.equals("-1") && !res.equals("-3")) {
                        Intent intentStartCall = new Intent(MainPageActivity.this, CallingActivity.class);
                        intentStartCall.putExtra("contact", phone);
                        intentStartCall.putExtra("room", res);
                        startActivity(intentStartCall);
                    } else {
                        Toast.makeText(MainPageActivity.this, "Call cancelled", Toast.LENGTH_SHORT).show();
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
                d.add("3");
                data.add(d);
                g.setData(data);

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

    }

}
