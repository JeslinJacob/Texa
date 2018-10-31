package com.texaconnect.texa.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.texaconnect.texa.ui.account.UserViewModel;
import com.texaconnect.texa.ui.adddevice.AddDeviceViewModel;
import com.texaconnect.texa.ui.devices.MqttViewModel;
import com.texaconnect.texa.viewmodel.AppViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MqttViewModel.class)
    abstract ViewModel bindMqttViewModel(MqttViewModel mqttViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddDeviceViewModel.class)
    abstract ViewModel bindAddDeviceViewModel(AddDeviceViewModel addDeviceViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(AppViewModelFactory factory);

}