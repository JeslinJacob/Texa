package com.texaconnect.texa.ui.account;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.texaconnect.texa.model.UserData;
import com.texaconnect.texa.repository.UserRepository;
import com.texaconnect.texa.util.AbsentLiveData;
import com.texaconnect.texa.model.AppTokens;
import com.texaconnect.texa.model.ProfileUpdateRequest;
import com.texaconnect.texa.model.Resource;
import com.texaconnect.texa.model.SignUpRequest;
import com.texaconnect.texa.model.User;

import javax.inject.Inject;

import okhttp3.ResponseBody;

public class UserViewModel extends ViewModel {
    //acts as a trigger
    private final MutableLiveData<Login> login = new MutableLiveData<>();
    private final MutableLiveData<String> profile= new MutableLiveData<>();
    private final MutableLiveData<SignUpRequest> signup = new MutableLiveData<>();
    private final MutableLiveData<String> forgotpassword = new MutableLiveData<>();
    private final MutableLiveData<ProfileUpdateRequest> profileupdate = new MutableLiveData<>();

    //gets the response
    private final LiveData<Resource<User>> loginResponse;
    private final LiveData<Resource<UserData>> profileResponse;
    private final LiveData<Resource<String>> signUpResponse;
    private final LiveData<Resource<ResponseBody>> forgotPasswordResponse;
    private final LiveData<Resource<ProfileUpdateRequest>> updateProfileResponse;


    private final LiveData<Resource<User>> tokenResponse;
    private final MutableLiveData<AppTokens> tokenRequest = new MutableLiveData<>();

    @Inject
    public UserViewModel(UserRepository userRepository) {

        loginResponse = Transformations.switchMap(login, data -> {
            if (data == null) {
                return AbsentLiveData.create();
            } else {
                return userRepository.login(data.grantType, data.username, data.password);
            }
        });


        profileResponse = Transformations.switchMap(profile, data -> {
            if (data == null) {
                return AbsentLiveData.create();
            } else {
                return userRepository.getUserProfileDetails();
            }
        });


        signUpResponse = Transformations.switchMap(signup, data -> {
            if (data == null) {
                return AbsentLiveData.create();
            } else {
                return userRepository.signUp(data);
            }
        });

        forgotPasswordResponse = Transformations.switchMap(forgotpassword, data -> {
            if (data == null) {
                return AbsentLiveData.create();
            } else {
                return userRepository.forgotPassword(data);
            }
        });

        updateProfileResponse = Transformations.switchMap(profileupdate, data -> {
            if (data == null) {
                return AbsentLiveData.create();
            } else {
                return userRepository.updateProfile(data);
            }
        });

        tokenResponse = Transformations.switchMap(tokenRequest, data -> {
            if (data == null) {
                return AbsentLiveData.create();
            } else {
                return userRepository.getNewTokens("refresh", data.getRefreshToken());
            }
        });
    }

    public LiveData<Resource<User>> getLoginData() {

        return loginResponse;
    }

    public LiveData<Resource<UserData>> observeProfileData(){
        return  profileResponse;
    }

    public LiveData<Resource<String>> getSignUpData() {

        return signUpResponse;
    }

    public LiveData<Resource<ResponseBody>> getForgotPasswordData() {

        return forgotPasswordResponse;
    }

    public LiveData<Resource<ProfileUpdateRequest>> UpdateProfileData() {

        return updateProfileResponse;
    }

    public void login(String grantType, String username, String password) {
        // Do an asynchronous operation to fetch users.
        login.setValue(new Login(grantType, username, password));

    }

    public void setGetNewToken(String grantType,String refreshToken){
        tokenRequest.setValue(new AppTokens(grantType,refreshToken));
    }

    public void signUp(SignUpRequest signUpRequest) {
        // Do an asynchronous operation to fetch users.

        signup.setValue(signUpRequest);

    }

    public void forgotPassword(String emailAddress) {
        // Do an asynchronous operation to fetch users.

        forgotpassword.setValue(emailAddress);

    }

    public void updateProfile(String name,String mobile,String location) {
        // Do an asynchronous operation to fetch users.

        profileupdate.setValue(new ProfileUpdateRequest(name,mobile,location));
    }

    public void getProfileData() {
        // Do an asynchronous operation to fetch users.

        profile.setValue("getProfileData");
    }

    class Login{
        String grantType;
        String username;
        String password;

        public Login(String grantType, String username, String password) {
            this.grantType = grantType;
            this.username = username;
            this.password = password;
        }
    }

    class GetNewToken{
        String grantType;
        String refreshToken;

        public GetNewToken(String grantType, String refreshToken) {
            this.grantType = grantType;
            this.refreshToken = refreshToken;
        }
    }
}