package com.texaconnect.texa.di;

import android.app.Application;

import com.texaconnect.texa.mqtt.MQTTManager;
import com.texaconnect.texa.network.ApiInterface;
import com.texaconnect.texa.network.RestClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ViewModelModule.class)
class AppModule {

    @Singleton
    @Provides
    ApiInterface provideApiInterface(Application app) {

        return RestClient.getApiInterface(app);
    }

    /*@Singleton
    @Provides
    User provideUser(Application app) {
        return new User(app);
    }*/

    @Singleton
    @Provides
    MQTTManager provideMQTTManager(Application app) {
        return new MQTTManager(app);
    }
}
