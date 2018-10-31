package com.texaconnect.texa.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.texaconnect.texa.R;
import com.texaconnect.texa.event.MQTTDisconnect;
import com.texaconnect.texa.interfaces.OnFragmentInteractionListener;
import com.texaconnect.texa.interfaces.PermissionCallback;
import com.texaconnect.texa.ui.common.NavigationController;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import static com.texaconnect.texa.util.Constantz.PERMISSION_REQUEST_CODE;

public class BaseActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private WeakReference<PermissionCallback> permissionCallback;

    //    @Inject
    protected NavigationController navigationController;
    private ProgressDialog mProgressDialog;
    protected AlertDialog mAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationController = new NavigationController(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:

                if (permissionCallback.get() != null) {
                    Arrays.sort(grantResults);
                    if (Arrays.binarySearch(grantResults, PackageManager.PERMISSION_DENIED) >= 0) {
                        permissionCallback.get().onFailed();
                    } else {
                        permissionCallback.get().onSuccess();
                    }
                }

                break;
            default:
                break;
        }

//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onRequestPermission(PermissionCallback permissionCallback, String... permissions) {
        this.permissionCallback = new WeakReference<>(permissionCallback);
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void setTitle(String title) {
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(title);

        /*Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitleTextAppearance(this, R.style.RobotoTextAppearance);
        myToolbar.setTitle(title);*/

    }

    @Override
    public void startWifiScan() {

    }

    @Override
    public void cancelProgressDialog(){
        if(mProgressDialog != null) mProgressDialog.cancel();

    }

    @Override
    public void showProgressDialog(String message){
        if(mProgressDialog == null) mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }


    public void showAlert(String title,String message){

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Quit app", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EventBus.getDefault().post(new MQTTDisconnect());
                finish();

//                finishAffinity();
//                System.exit(0);
            }
        });
        mAlertDialog=builder.create();
        mAlertDialog.show();

    }

    public void disMissAlert(){
        if(mAlertDialog!=null){

            if (mAlertDialog.isShowing()){

                mAlertDialog.cancel();
            }
        }
    }
}