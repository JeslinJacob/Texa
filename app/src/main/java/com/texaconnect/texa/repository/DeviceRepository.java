package com.texaconnect.texa.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.texaconnect.texa.R;
import com.texaconnect.texa.TexaApplication;
import com.texaconnect.texa.model.AddGroup;
import com.texaconnect.texa.model.AddGroupResponse;
import com.texaconnect.texa.model.GroupItem;
import com.texaconnect.texa.network.ApiInterface;
import com.texaconnect.texa.model.DeviceItem;
import com.texaconnect.texa.model.Resource;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class DeviceRepository {
    ApiInterface apiInterface;
    @Inject
    public DeviceRepository(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public LiveData<Resource<List<DeviceItem>>> getDevices() {
        // Do an asynchronous operation to fetch users.
        MutableLiveData<Resource<List<DeviceItem>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiInterface.getDevices().enqueue(new Callback<List<DeviceItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<DeviceItem>> call, @NonNull Response<List<DeviceItem>> response) {
                Log.d("DeviceRepository","getDevices onResponse request "+response.isSuccessful()+", "+response.code()+", "+call.request());

                if (response.isSuccessful()) {
                    data.setValue(Resource.success(response.body()));
                }else if (response.code() == 204){
                    data.setValue(Resource.error("No Devices found", null));

                }else {
                    data.setValue(Resource.error("No Data found", null));

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DeviceItem>> call, @NonNull Throwable t) {
//                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                Log.d("DeviceRepository","getDevices onFailure "+t.getMessage());
//                data.setValue(Resource.error("Failed to fetch device data" + t.getMessage(), null));
                if(t instanceof IOException){
                    data.setValue(Resource.error("Please check your internet connection!", null));
                }else data.setValue(Resource.error("Failed to fetch device data", null));
            }
        });

        return data;
    }

    public LiveData<Resource<List<GroupItem>>> getGroups() {
        // Do an asynchronous operation to fetch users.
        MutableLiveData<Resource<List<GroupItem>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiInterface.getGroups().enqueue(new Callback<List<GroupItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<GroupItem>> call, @NonNull Response<List<GroupItem>> response) {
                Log.d("DeviceRepository","getGroups onResponse request "+response.isSuccessful()+", "+response.code()+", "+call.request());

                if (response.isSuccessful()) {
                    data.setValue(Resource.success(response.body()));
                }else if (response.code() == 204){
                    data.setValue(Resource.error("No Groups found", null));

                }else {
                    data.setValue(Resource.error("No Data found", null));

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GroupItem>> call, @NonNull Throwable t) {
//                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                Log.d("DeviceRepository","getGroups onFailure "+t.getMessage());
//                data.setValue(Resource.error("" + t.getMessage(), null));
                if(t instanceof IOException){
                    data.setValue(Resource.error("Please check your internet connection!", null));
                }else data.setValue(Resource.error("Failed to fetch group data", null));
            }
        });

        return data;
    }

    public LiveData<Resource<String>> addGroup(String name) {
        // Do an asynchronous operation to fetch users.
        MutableLiveData<Resource<String>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiInterface.addGroup(new AddGroup(name)).enqueue(new Callback<AddGroupResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddGroupResponse> call, @NonNull Response<AddGroupResponse> response) {
                Log.d("DeviceRepository","addGroup onResponse request "+response.isSuccessful()+", "+response.code()+", "+call.request());

                if (response.code() == 201) {
                    data.setValue(Resource.success(response.body().id));
                }else if (response.code() == 409){
                    data.setValue(Resource.error("" + response.body().error_description, null));

                }else {
                    data.setValue(Resource.error("Failed to create group", null));

                }
            }

            @Override
            public void onFailure(@NonNull Call<AddGroupResponse> call, @NonNull Throwable t) {
//                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                Log.d("DeviceRepository","getGroups onFailure "+t.getMessage());
//                data.setValue(Resource.error("" + t.getMessage(), null));
                if(t instanceof IOException){
                    data.setValue(Resource.error("Please check your internet connection!", null));
                }else data.setValue(Resource.error("Failed to create group", null));
            }
        });

        return data;
    }

}