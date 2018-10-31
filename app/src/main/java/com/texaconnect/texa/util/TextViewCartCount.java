package com.texaconnect.texa.util;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewCartCount extends TextView {
    public TextViewCartCount(Context context) {
        super(context);
    }

    public TextViewCartCount(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewCartCount(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextViewCartCount(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setCount(String count) {
//        animate().rotationXBy(360).setDuration(1000);
        animate().rotationYBy(360).setDuration(1500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                setText(count);
            }
        }, 500);
    }
}
