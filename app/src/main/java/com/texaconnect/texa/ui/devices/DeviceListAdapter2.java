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

package com.texaconnect.texa.ui.devices;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.texaconnect.texa.R;
import com.texaconnect.texa.databinding.DeviceItemBinding;
import com.texaconnect.texa.interfaces.DeviceClickCallback;
import com.texaconnect.texa.model.DeviceItem;
import com.texaconnect.texa.ui.common.DataBoundListAdapter;

/**
 * A RecyclerView adapter for {@link DeviceItem} class.
 */
public class DeviceListAdapter2 extends DataBoundListAdapter<DeviceItem, DeviceItemBinding> {
    private static final String TAG = "DeviceListAdapter";
    private final DataBindingComponent dataBindingComponent;
    private final DeviceClickCallback deviceClickCallback;

    public DeviceListAdapter2(DataBindingComponent dataBindingComponent,
                              DeviceClickCallback deviceClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.deviceClickCallback = deviceClickCallback;
    }

    @Override
    protected DeviceItemBinding createBinding(ViewGroup parent, int viewType) {
        DeviceItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), viewType,
                        parent, false, dataBindingComponent);

        binding.setCallback(deviceClickCallback);
        return binding;
    }

    @Override
    protected void bind(DeviceItemBinding binding, DeviceItem item, int position) {
        binding.setData(item);

        SwitchListAdapter adapter = new SwitchListAdapter(dataBindingComponent,
                deviceClickCallback);
        binding.recyclerViewSwitch.setLayoutManager(new GridLayoutManager(binding.recyclerViewSwitch.getContext(), 3));

        binding.recyclerViewSwitch.setAdapter(adapter);
        adapter.replace(item.device.nodes);
    }

    @Override
    protected boolean areItemsTheSame(DeviceItem oldItem, DeviceItem newItem) {
        return oldItem.id.equals(newItem.id) && oldItem.name.equals(newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(DeviceItem oldItem, DeviceItem newItem) {
        return oldItem.id.equals(newItem.id) && oldItem.name.equals(newItem.name);

    }

    @Override
    protected int getLayoutId(DeviceItem item, int position) {
        return R.layout.device_item;
    }

}