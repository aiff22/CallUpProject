package com.aiff.callup_001;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MainPageActivity extends TabActivity {
    /** Called when the activity is first created. */

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

    }
}
