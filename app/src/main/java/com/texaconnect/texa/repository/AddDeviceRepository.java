package com.texaconnect.texa.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.texaconnect.texa.model.Resource;
import com.texaconnect.texa.network.ApiInterface;
import com.texaconnect.texa.ui.adddevice.AddDeviceViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AddDeviceRepository {
    ApiInterface apiInterface;


    @Inject
    public AddDeviceRepository(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public LiveData<Resource<String>> verifySerial(AddDeviceViewModel.DeviceInfo deviceInfo) {
        MutableLiveData<Resource<String>> data = new MutableLiveData<>();

        data.setValue(Resource.loading(null));

        apiInterface.verifySerial(deviceInfo).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.d("AddDeviceRepository","verifySerial onResponse request "+
                        response.isSuccessful()+", "+response.code()+", "+call.request()+
                        ", body "+response.body());

                if (response.isSuccessful()) {
                    data.setValue(Resource.success(response.body()));
                }else if (response.code() == 400){
                    String error = "Device not found! Please recheck the serial number.";
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        error = jsonObject.getString("error_description");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    data.setValue(Resource.error(error, null));

                }else {
                    data.setValue(Resource.error("Error verifying Device", null));

                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
//                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                Log.d("AddDeviceRepository","verifySerial onFailure "+t.getMessage());
//                data.setValue(Resource.error("" + t.getMessage(), null));
                if(t instanceof IOException){
                    data.setValue(Resource.error("Please check your internet connection!", null));
                }else data.setValue(Resource.error("Failed to fetch group data", null));
            }
        });

        return data;

    }

    public LiveData<Resource<String>> getLocalMACId(String ip) {

        MutableLiveData<Resource<String>> data = new MutableLiveData<>();

        data.setValue(Resource.loading(null));

        apiInterface.getLocalMACId("http://"+ip+"/__SL_G_N.D").enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.d("AddDeviceRepository","getLocalMACId onResponse request "+
                        response.isSuccessful()+", "+response.code()+", "+call.request()+
                        ", body "+response.body());

                if (response.isSuccessful()) {
                    data.setValue(Resource.success(response.body()));
                }else if (response.code() == 400){
                    String error = "Device not found!";
                    data.setValue(Resource.error(error, null));

                }else {
                    data.setValue(Resource.error("Error verifying Device", null));

                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
//                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                Log.d("AddDeviceRepository","verifySerial onFailure "+t.getMessage());
//                data.setValue(Resource.error("" + t.getMessage(), null));
                if(t instanceof IOException){
                    data.setValue(Resource.error("Please check your internet connection!", null));
                }else data.setValue(Resource.error("Failed to fetch group data", null));
            }
        });

        return data;

    }
}