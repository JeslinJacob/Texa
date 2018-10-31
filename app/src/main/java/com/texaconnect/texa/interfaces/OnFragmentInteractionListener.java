package com.texaconnect.texa.interfaces;

public interface OnFragmentInteractionListener {
    void onRequestPermission(PermissionCallback permissionCallback, String... permission);
    void showToast(String message);
    void showProgressDialog(String message);
    void cancelProgressDialog();
    void setTitle(String title);
    void startWifiScan();
}
