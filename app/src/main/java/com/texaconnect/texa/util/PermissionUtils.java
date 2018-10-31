package com.texaconnect.texa.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

public class PermissionUtils {

    public static boolean checkSelfPermission(@NonNull Context context, @NonNull String permission){

        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
