package com.aiff.callup_001;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

class ConnectServer extends AsyncTask<String, Void, String> {

    String resp = "";

    protected String doInBackground(String... args) {

        String reqType = args[0];

        HttpClient client = new DefaultHttpClient();
        // HttpPost post = new HttpPost("http://10.0.2.2:8080/requestServlet");
        HttpPost post = new HttpPost("http://52.25.0.226:8080/requestServlet");

        // -- Switch between different request types -->

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            if (reqType.equals("getId"))
                nameValuePairs.add(new BasicNameValuePair("request_type",
                        "getId"));

            if (reqType.equals("register")) {
                nameValuePairs.add(new BasicNameValuePair("request_type",
                        "register"));
                nameValuePairs.add(new BasicNameValuePair("login",
                        args[1]));
                nameValuePairs.add(new BasicNameValuePair("pass",
                        args[2]));
                nameValuePairs.add(new BasicNameValuePair("email",
                        args[3]));
                nameValuePairs.add(new BasicNameValuePair("name",
                        args[4]));
            }

            if (reqType.equals("login")) {
                nameValuePairs.add(new BasicNameValuePair("request_type",
                        "login"));
                nameValuePairs.add(new BasicNameValuePair("login",
                        args[1]));
                nameValuePairs.add(new BasicNameValuePair("pass",
                        args[2]));
            }

            if (reqType.equals("hello")) {
                nameValuePairs.add(new BasicNameValuePair("request_type",
                        "hello"));
                nameValuePairs.add(new BasicNameValuePair("login",
                        args[1]));
                nameValuePairs.add(new BasicNameValuePair("pass",
                        args[2]));
            }

            if (reqType.equals("call")) {
                nameValuePairs.add(new BasicNameValuePair("request_type",
                        "call"));
                nameValuePairs.add(new BasicNameValuePair("login",
                        args[1]));
                nameValuePairs.add(new BasicNameValuePair("pass",
                        args[2]));
                nameValuePairs.add(new BasicNameValuePair("id_contact",
                        args[3]));
            }

            if (reqType.equals("msg")) {
                nameValuePairs.add(new BasicNameValuePair("request_type",
                        "msg"));
                nameValuePairs.add(new BasicNameValuePair("login",
                        args[1]));
                nameValuePairs.add(new BasicNameValuePair("pass",
                        args[2]));
                nameValuePairs.add(new BasicNameValuePair("id_contact",
                        args[3]));
                nameValuePairs.add(new BasicNameValuePair("msg_text",
                        args[4]));
            }

            if (reqType.equals("contact")) {
                nameValuePairs.add(new BasicNameValuePair("request_type",
                        "contact"));
                nameValuePairs.add(new BasicNameValuePair("login",
                        args[1]));
                nameValuePairs.add(new BasicNameValuePair("pass",
                        args[2]));
                nameValuePairs.add(new BasicNameValuePair("contact_name",
                        args[3]));
                nameValuePairs.add(new BasicNameValuePair("id_contact",
                        args[4]));
                nameValuePairs.add(new BasicNameValuePair("be_friends",
                        args[5]));
            }

            if (reqType.equals("delcontact")) {
                nameValuePairs.add(new BasicNameValuePair("request_type",
                        "delcontact"));
                nameValuePairs.add(new BasicNameValuePair("login",
                        args[1]));
                nameValuePairs.add(new BasicNameValuePair("pass",
                        args[2]));
                nameValuePairs.add(new BasicNameValuePair("id_contact",
                        args[3]));
            }

            if (reqType.equals("unfriend")) {
                nameValuePairs.add(new BasicNameValuePair("request_type",
                        "unfriend"));
                nameValuePairs.add(new BasicNameValuePair("login",
                        args[1]));
                nameValuePairs.add(new BasicNameValuePair("pass",
                        args[2]));
                nameValuePairs.add(new BasicNameValuePair("id_contact",
                        args[3]));
            }

            if (reqType.equals("msgstatus")) {
                nameValuePairs.add(new BasicNameValuePair("request_type",
                        "msgstatus"));
                nameValuePairs.add(new BasicNameValuePair("login",
                        args[1]));
                nameValuePairs.add(new BasicNameValuePair("pass",
                        args[2]));
                nameValuePairs.add(new BasicNameValuePair("id_contact",
                        args[3]));
            }

            if (reqType.equals("respcall")) {
                nameValuePairs.add(new BasicNameValuePair("request_type",
                        "respcall"));
                nameValuePairs.add(new BasicNameValuePair("login",
                        args[1]));
                nameValuePairs.add(new BasicNameValuePair("pass",
                        args[2]));
                nameValuePairs.add(new BasicNameValuePair("id_contact",
                        args[3]));
                nameValuePairs.add(new BasicNameValuePair("decision",
                        args[4]));
            }

            if (reqType.equals("delcall")) {
                nameValuePairs.add(new BasicNameValuePair("request_type",
                        "delcall"));
                nameValuePairs.add(new BasicNameValuePair("login",
                        args[1]));
                nameValuePairs.add(new BasicNameValuePair("pass",
                        args[2]));
                nameValuePairs.add(new BasicNameValuePair("id_contact",
                        args[3]));
                nameValuePairs.add(new BasicNameValuePair("type",
                        args[4]));
            }

            if (reqType.equals("delmsg")) {
                nameValuePairs.add(new BasicNameValuePair("request_type",
                        "delmsg"));
                nameValuePairs.add(new BasicNameValuePair("login",
                        args[1]));
                nameValuePairs.add(new BasicNameValuePair("pass",
                        args[2]));
                nameValuePairs.add(new BasicNameValuePair("id_contact",
                        args[3]));
                nameValuePairs.add(new BasicNameValuePair("type",
                        args[4]));
            }

            // -- Send request to the server -->

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post);

            // -- Read response -->

            if (reqType.equals("login") || reqType.equals("register") || reqType.equals("hello")) {

                ObjectInputStream stream = new ObjectInputStream(response.getEntity().getContent());

                try {
                    List<List<String>> data = (List<List<String>>) stream.readObject();

                    Log.d("ConnectServer", String.valueOf(data));

                    UserData g = UserData.getInstance();
                    if ((data.size() > 1 && reqType.equals("hello")) || !reqType.equals("hello")) g.setData(data);

                    if (reqType.equals("hello")) g.setNewEvents(data);

                    resp = "1";

                } catch (ClassNotFoundException e) {
                    resp = "-1";
                    e.printStackTrace();
                }

            } else {

                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    System.out.println(line);
                    resp = resp + line;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resp;
    }

    protected void onPostExecute(String feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}

