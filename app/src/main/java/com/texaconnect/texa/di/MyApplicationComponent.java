package com.texaconnect.texa.di;

import android.app.Application;

import com.texaconnect.texa.TexaApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = { AndroidInjectionModule.class, MyApplicationModule.class, AppModule.class})
public interface MyApplicationComponent extends AndroidInjector<TexaApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        MyApplicationComponent build();
    }
    void inject(TexaApplication texaApplication);
}
