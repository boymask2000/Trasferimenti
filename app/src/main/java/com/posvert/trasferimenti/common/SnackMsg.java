package com.posvert.trasferimenti.common;

import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.View;

import com.posvert.trasferimenti.R;

/**
 * Created by giovanni on 14/11/16.
 */

public class SnackMsg {
    public static void showMsg(View viewById, String text , String color){
        Snackbar.make(viewById,
                Html.fromHtml("<font color=\"#"+color+"\">"+text+"</font>"), Snackbar.LENGTH_LONG).show();
    }
    public static void showErrMsg(View viewById, String text){
        showMsg(viewById,text, "ff0000");
    }
    public static void showInfoMsg(View viewById, String text){
        showMsg(viewById,text, "000000");
    }
}
