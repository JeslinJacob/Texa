/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.texaconnect.texa.ui.adddevice;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.texaconnect.texa.R;
import com.texaconnect.texa.databinding.WifiListItemBinding;
import com.texaconnect.texa.model.SelectedWifiItem;
import com.texaconnect.texa.ui.common.DataBoundListAdapter;

/**
 * A RecyclerView adapter for {@link SelectedWifiItem} class.
 */
public class WifiListListAdapter extends DataBoundListAdapter<SelectedWifiItem, WifiListItemBinding> {
    private static final String TAG = "SwitchListAdapter";
    private final DataBindingComponent dataBindingComponent;
//    private final SwitchClickCallback switchClickCallback;
    private final WifiSelected wifiSelected;

    public WifiListListAdapter(DataBindingComponent dataBindingComponent,
                               WifiSelected callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.wifiSelected = callback;
    }

    @Override
    protected WifiListItemBinding createBinding(ViewGroup parent, int viewType) {
        WifiListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), viewType,
                        parent, false, dataBindingComponent);

        binding.setCallback(wifiSelected);
        return binding;
    }

    @Override
    protected void bind(WifiListItemBinding binding, SelectedWifiItem item, int position) {
        binding.setData(item);
    }

    @Override
    protected boolean areItemsTheSame(SelectedWifiItem oldItem, SelectedWifiItem newItem) {
        return oldItem.getSSID().equals(newItem.getSSID()) && oldItem.getSecurityType().equals(newItem.getSecurityType());
    }

    @Override
    protected boolean areContentsTheSame(SelectedWifiItem oldItem, SelectedWifiItem newItem) {
        return oldItem.getSSID().equals(newItem.getSSID()) && oldItem.getSecurityType().equals(newItem.getSecurityType());

    }

    @Override
    protected int getLayoutId(SelectedWifiItem item, int position) {
        return R.layout.wifi_list_item;
    }

    public interface WifiSelected {
        void onWifiSelected(SelectedWifiItem wifiItem);
    }
}