package com.yhb.test;

import com.yhb.httputils.config.HttpBaseConfig;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class MyBaseConfig extends HttpBaseConfig {

    /**TAG*/
    private static final String TAG = "MyDefaultConfig";

    @Override
    public Map<String, String> headers() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("device-type", "0");
        hashMap.put("app-version", BuildConfig.VERSION_NAME);
        hashMap.put("app-package", BuildConfig.APPLICATION_ID);
        hashMap.put("mac", mac());
        return hashMap;
    }

    /**获取mac地址*/
    public static String mac() {
        String mac = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("sys/class/net/eth0/address"));
            mac = reader.readLine();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac.trim();
    }

}
