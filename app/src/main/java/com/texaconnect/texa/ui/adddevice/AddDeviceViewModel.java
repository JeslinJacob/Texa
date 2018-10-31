package com.texaconnect.texa.ui.adddevice;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.net.wifi.ScanResult;

import com.texaconnect.texa.model.Resource;
import com.texaconnect.texa.model.SelectedWifiItem;
import com.texaconnect.texa.repository.AddDeviceRepository;
import com.texaconnect.texa.util.AbsentLiveData;

import java.util.List;

import javax.inject.Inject;

public class AddDeviceViewModel extends ViewModel {
    private final LiveData<Resource<String>> macId;
    private final LiveData<Resource<String>> localMacId;
    private final MutableLiveData<DeviceInfo> device = new MutableLiveData<>();
    private final MutableLiveData<List<ScanResult>> mScanResults = new MutableLiveData<>();
    private final MutableLiveData<String> mSsid = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mDetected = new MutableLiveData<>();
    private final MutableLiveData<String> mIPAddress = new MutableLiveData<>();
    private final MutableLiveData<String> mSecurityType = new MutableLiveData<>();
    private final MutableLiveData<SelectedWifiItem> mSelectedWiFi = new MutableLiveData<>();
    private final String mNohotSpot = "No hotspot detected";


    @Inject
    public AddDeviceViewModel(AddDeviceRepository addDeviceRepository) {

        macId = Transformations.switchMap(device, data -> {
            if (data == null) {
                return AbsentLiveData.create();
            } else {
                return addDeviceRepository.verifySerial(data);
            }
        });

        localMacId = Transformations.switchMap(mIPAddress, data -> {
            if (data == null) {
                return AbsentLiveData.create();
            } else {
                return addDeviceRepository.getLocalMACId(data);
            }
        });
        mSsid.setValue(mNohotSpot);
        mDetected.setValue(false);
    }

    public LiveData<Resource<String>> observeMacId() {

        return macId;
    }

    public LiveData<Resource<String>> observeLocalMacId() {

        return localMacId;
    }

    public LiveData<List<ScanResult>> observeScanResults() {

        return mScanResults;
    }

    public LiveData<Boolean> observeDetected() {

        return mDetected;
    }

    public LiveData<String> observeSSID() {

        return mSsid;
    }

    public LiveData<String> observeSecurityType() {

        return mSecurityType;
    }
    public LiveData<SelectedWifiItem> observeSelectedWiFi() {

        return mSelectedWiFi;
    }

    public void setSecurityType(String securityType) {
        mSecurityType.setValue(securityType);
    }

    public void setSelectedWiFi(SelectedWifiItem selectedWiFi) {
        mSelectedWiFi.setValue(selectedWiFi);
    }

    public void setScanResults(List<ScanResult> results) {
        mScanResults.setValue(results);
        detectHotspotName(results);
    }

    public void detectHotspotName(List<ScanResult> results) {

        String s = mNohotSpot;
        for (ScanResult result : results) {
            if (isValidDeviceHotspot(result.SSID)) {
                s = result.SSID;
            }
        }
        mSsid.setValue(s);
    }

    public void getLocalMacID(String ssid, String ip) {
        boolean b = isValidDeviceHotspot(ssid);
        if (b) {
            mIPAddress.setValue(ip);
        }
    }

    public boolean setDeviceDetected() {
        if (macId.getValue().equals(localMacId.getValue())) {
            mDetected.setValue(true);
            return true;
        }else {
            mDetected.setValue(false);
            return false;

        }
    }

    public void verifySerial(String name, String serial) {
        device.setValue(new DeviceInfo(name, serial));
    }

    public boolean isValidDeviceHotspot(String ssid) {
        return (ssid.contains("texaconnect") ||
                ssid.contains("mysimplelink") ||
                ssid.contains("Sayone")||
                ssid.contains("Android"));
    }

    public class DeviceInfo {
        String name;
        String serial;

        public DeviceInfo(String name, String serial) {
            this.name = name;
            this.serial = serial;
        }
    }
}