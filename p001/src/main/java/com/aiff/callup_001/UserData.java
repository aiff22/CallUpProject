package com.aiff.callup_001;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserData {
    private static UserData instance;

    //private List<List<String>> data;
    private List<List<String>> data_contacts = new ArrayList<>();
    private List<List<String>> data_calls = new ArrayList<>();
    private List<List<String>> data_messages = new ArrayList<>();
    private List<List<String>> data_events = new ArrayList<>();
    private List<List<String>> new_events = new ArrayList<>();

    private UserData() {
    }

    public List<List<String>> getContacts() {
        return this.data_contacts;
    }

    public List<List<String>> getCalls() {
        return this.data_calls;
    }

    public List<List<String>> getMessages() {
        return this.data_messages;
    }

    public List<List<String>> getEvents() {
        return this.data_events;
    }

    public List<List<String>> getNewEvents() {
        return this.new_events;
    }

    public void setNewEvents(List<List<String>> data) {this.new_events = data; }


    public void setData(List<List<String>> data) {

        for (int i = 0; i < data.size(); i++) {
            List<String> d = data.get(i);
            switch (d.get(0)) {

                case "contacts":
                    this.data_contacts.add(d);
                    break;

                case "calls":
                    this.data_calls.add(d);
                    break;

                case "messages":
                    this.data_messages.add(d);
                    break;

                case "events":

                    this.data_events.add(d);
                    break;
            }
        }

        Log.d("UserDataLogs:contacts", String.valueOf(this.data_contacts));
        Log.d("UserDataLogs:calls", String.valueOf(this.data_calls));
        Log.d("UserDataLogs:messages", String.valueOf(this.data_messages));
        Log.d("UserDataLogs:events", String.valueOf(this.data_events));

    }

    public static synchronized UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    public void setMessageRead(String contact_number) {
        for (int i = 0; i < data_messages.size(); i++) {
             if (data_messages.get(i).get(1).equals(contact_number))
                 data_messages.get(i).set(3, "3");
        }

        for (int i = 0; i < data_events.size(); i++) {
             if (data_events.get(i).get(3).equals("1") && data_events.get(i).get(1).equals(contact_number))
                 data_events.remove(i);
        }
    }


    public String findContact(String contact_number) {
        for (int i = 0; i < data_contacts.size(); i++)
            if (data_contacts.get(i).get(1).equals(contact_number))
                return data_contacts.get(i).get(2);
        return "-1";
    }
}

