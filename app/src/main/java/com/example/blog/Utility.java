package com.example.blog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

public class Utility {


    private static Utility utilityInstance;

    private Utility() {
    }

    public static synchronized Utility getUtilityInstance() {
        if (null == utilityInstance) {
            utilityInstance = new Utility();
        }
        return utilityInstance;
    }

    public void showGifPopup(final Context mContext, boolean show, Dialog dialog, String title) {
        dialog.setContentView(R.layout.gif_popup);
        dialog.setCancelable(false);
        AVLoadingIndicatorView imageView = dialog.findViewById(R.id.avi);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if (mContext != null) {
            if (!((Activity) mContext).isFinishing()) {
                try {
                    if (show) {
                        dialog.show();
                    } else {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static String readAll(String  string) {
        String str = string.split(" ")[0];
        str = str.replace("\\","");
        String[] arr = str.split("u");
        String text = "";
        for(int i = 1; i < arr.length; i++){
            int hexVal = Integer.parseInt(arr[i], 16);
            text += (char)hexVal;
        }
        return text;
    }
}
