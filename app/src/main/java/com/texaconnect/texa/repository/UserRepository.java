package com.texaconnect.texa.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.texaconnect.texa.model.ErrorBody;
import com.texaconnect.texa.model.ProfileUpdateRequest;
import com.texaconnect.texa.model.Resource;
import com.texaconnect.texa.model.SignUpRequest;
import com.texaconnect.texa.model.User;
import com.texaconnect.texa.model.UserData;
import com.texaconnect.texa.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class UserRepository {

    ApiInterface apiInterface;

    @Inject
    public UserRepository(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public LiveData<Resource<User>> login(String grantType, String username, String password) {
        // Do an asynchronous operation to fetch users.
        MutableLiveData<Resource<User>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiInterface.login(grantType, username,
                password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                Log.d("UserRepository", "onResponse request " + call.request());

                if (response.isSuccessful()) {
                    data.setValue(Resource.success(response.body()));

                } else {

                    try {
                        ResponseBody s = response.errorBody();

                        Gson gson = new Gson();
                        ErrorBody errorBody = gson.fromJson(s.string(),ErrorBody.class);
//                                Log.d("ERROR", "onResponse: "+errorBody.getError_description());
                        data.setValue(Resource.error(errorBody.getError_description(),null));
                    } catch (IOException e) {
                        e.printStackTrace();
                        data.setValue(Resource.error("Login failed",null));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
//                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Log.d("FAILED EXCEPTION", "onFailure: " + t.getMessage());
                    data.setValue(Resource.error("Please check your internet connection!", null));
                } else data.setValue(Resource.error("Failed to login", null));

            }
        });

        return data;
    }

    public LiveData<Resource<User>> getNewTokens(String grantType, String refreshToken) {
        // Do an asynchronous operation to fetch users.
        MutableLiveData<Resource<User>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiInterface.getRefreshedTokens(grantType, refreshToken)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            data.setValue(Resource.success(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                        if (t instanceof IOException) {
                            data.setValue(Resource.error("Please check your internet connection!", null));
                        } else
                            data.setValue(Resource.error("Failed to perform the operation", null));

                    }
                });

        return data;
    }

    public LiveData<Resource<String>> signUp(SignUpRequest signUpRequest) {
        // Do an asynchronous operation to fetch users.

        MutableLiveData<Resource<String>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        apiInterface.signUp(signUpRequest).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        Log.d("UserRepository", "signUp onResponse " +
                                response.code() + " and " + response.message() +
                                " and " + response.body());
                        if (response.isSuccessful()) {
                            data.setValue(Resource.success(response.body().toString()));

                        } else {
                            try {
                                ResponseBody s = response.errorBody();

                                Gson gson = new Gson();
                                ErrorBody errorBody = gson.fromJson(s.string(),ErrorBody.class);
//                                Log.d("ERROR", "onResponse: "+errorBody.getError_description());
                                data.setValue(Resource.error(errorBody.getError_description(),null));
                            } catch (IOException e) {
                                e.printStackTrace();
                                data.setValue(Resource.error("Signup failed",null));
                            }
                        }

                       /* else if (response.code()==400){
                            data.setValue(Resource.error("Activation Pending",null));
                        }else if (response.code()==409){
                            data.setValue(Resource.error("User exists",null));
                        }
                        else {
                            data.setValue(Resource.error(""+response.message(),null));
                        }*/
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.d("UserRepository", "signUp onFailure " + t.getMessage());
                        if (t instanceof IOException) {
                            data.setValue(Resource.error("Please check your internet connection!", null));
                        } else data.setValue(Resource.error("Failed to perform sign up", null));

                    }
                }
        );

        return data;
    }

    public LiveData<Resource<ResponseBody>> forgotPassword(String emailAddress) {
        // Do an asynchronous operation to fetch users.
        MutableLiveData<Resource<ResponseBody>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        apiInterface.forgotPassword(emailAddress).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            data.setValue(Resource.success(response.body()));
                        }else{
                            try {
                                ResponseBody s = response.errorBody();

                                Gson gson = new Gson();
                                ErrorBody errorBody = gson.fromJson(s.string(),ErrorBody.class);
//                                Log.d("ERROR", "onResponse: "+errorBody.getError_description());
                                data.setValue(Resource.error(errorBody.getError_description(),null));
                            } catch (IOException e) {
                                e.printStackTrace();
                                data.setValue(Resource.error("Login failed",null));
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        if (t instanceof IOException) {
                            data.setValue(Resource.error("Please check your internet connection!", null));
                        } else data.setValue(Resource.error("Failed to update", null));

                    }
                }
        );

        return data;
    }

    public LiveData<Resource<UserData>> getUserProfileDetails() {
        MutableLiveData<Resource<UserData>> data = new MutableLiveData<>();
        data.postValue(Resource.loading(null));
        apiInterface.getUserProfile().enqueue(
                new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        if (response.isSuccessful()) {
                            data.setValue(Resource.success((response.body())));
                        }else{
                            try {
                                ResponseBody s = response.errorBody();

                                Gson gson = new Gson();
                                ErrorBody errorBody = gson.fromJson(s.string(),ErrorBody.class);
//                                Log.d("ERROR", "onResponse: "+errorBody.getError_description());
                                data.setValue(Resource.error(errorBody.getError_description(),null));
                            } catch (IOException e) {
                                e.printStackTrace();
                                data.setValue(Resource.error("Login failed",null));
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        if (t instanceof IOException) {
                            data.setValue(Resource.error("Please check your internet connection!", null));
                        } else data.setValue(Resource.error("Failed", null));
                    }
                }
        );
        return data;
    }

    public LiveData<Resource<ProfileUpdateRequest>> updateProfile(ProfileUpdateRequest updateRequest) {
        // Do an asynchronous operation to fetch users.

        MutableLiveData<Resource<ProfileUpdateRequest>> data = new MutableLiveData<>();
        data.postValue(Resource.loading(null));
        apiInterface.updateProfile(updateRequest).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            data.setValue(Resource.success(updateRequest));
                        }else{
                            try {
                                ResponseBody s = response.errorBody();

                                Gson gson = new Gson();
                                ErrorBody errorBody = gson.fromJson(s.string(),ErrorBody.class);
//                                Log.d("ERROR", "onResponse: "+errorBody.getError_description());
                                data.setValue(Resource.error(errorBody.getError_description(),null));
                            } catch (IOException e) {
                                e.printStackTrace();
                                data.setValue(Resource.error("Login failed",null));
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        if (t instanceof IOException) {
                            data.setValue(Resource.error("Please check your internet connection!", null));
                        } else data.setValue(Resource.error("Failed to update profile", null));

                    }
                }
        );

        return data;

    }
}
