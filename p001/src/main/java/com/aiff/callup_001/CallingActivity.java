package com.aiff.callup_001;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


public class CallingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        // -- Get room number and contact id -->

        Intent intent = getIntent();
        String contact_number = intent.getStringExtra("contact");
        String room = intent.getStringExtra("room");

        UserData g = UserData.getInstance();
        TextView contact_name = (TextView) findViewById(R.id.ContactName);
        contact_name.setText(contact_number);

        if (!g.findContact(contact_number).equals("-1"))
            contact_name.setText(g.findContact(contact_number));

        // -- Give permissions to the webviewer -->

        WebView mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onPermissionRequest(PermissionRequest request) {
                    request.grant(request.getResources());
                }
            });

            // -- Load web page -->

            // String adress = "http://10.0.2.2:8080/basic/?r=" + room;
            //String adress = "http://gmail.com";
            String adress = "http://52.25.0.226:8080/basic/?r=" + room;
            mWebView.loadUrl(adress);

        }

        // -- If button "End" is pressed, then finish activity -->

        Button btnEndCall = (Button) findViewById(R.id.btnEndCall);
        btnEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
