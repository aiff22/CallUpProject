package com.aiff.callup_001;

import java.util.List;

public class UserData {
    private static UserData instance;

    private List<List<String>> data;

    private UserData() {
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> d) {
        this.data = d;
    }

    public static synchronized UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }
}

