package com.posvert.mobility.common;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by giovanni on 11/11/16.
 */

public class URLBuilder {

    private static String server = null;
    private String url;
    private int numParams = 0;


    public URLBuilder(Context context, String restKey, String prefix, String protocol) {
        if (server == null) server = Config.getServerAddress(context);

        url = protocol + "://" + server + prefix + "/" + restKey;
    }

    public URLBuilder(Context context, String restKey, String prefix) {
        this(context, restKey, prefix, "http");
    }

    public URLBuilder(String url) {
        this.url = url;
    }

    public void addParameter(String key, String val) {
        if (val != null) val = val.trim();

        if (numParams == 0) {
            numParams++;
            if (!url.endsWith("?"))
                url += "?";
        } else url += "&";

        try {
            url += key + "=" + URLEncoder.encode(val, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        Log.e("BLD", url);
        return url;
    }

}
