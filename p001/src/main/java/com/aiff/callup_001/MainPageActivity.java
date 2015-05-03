package com.aiff.callup_001;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainPageActivity extends TabActivity {
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // получаем TabHost
        TabHost tabHost = getTabHost();

        // инициализация была выполнена в getTabHost
        // метод setup вызывать не нужно

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Cont");
        tabSpec.setContent(new Intent(this, ContactsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Calls");
        tabSpec.setContent(new Intent(this, CallsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("Msgs");
        tabSpec.setContent(new Intent(this, MessagesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setIndicator("Events");
        tabSpec.setContent(new Intent(this, EventsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(tabSpec);

    }
}
