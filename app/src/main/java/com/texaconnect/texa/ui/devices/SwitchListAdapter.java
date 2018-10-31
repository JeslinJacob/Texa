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
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.texaconnect.texa.R;
import com.texaconnect.texa.databinding.SwitchItemBinding;
import com.texaconnect.texa.interfaces.DeviceClickCallback;
import com.texaconnect.texa.ui.common.DataBoundListAdapter;
import com.texaconnect.texa.model.Node;

/**
 * A RecyclerView adapter for {@link Node} class.
 */
public class SwitchListAdapter extends DataBoundListAdapter<Node, SwitchItemBinding> {
    private static final String TAG = "SwitchListAdapter";
    private final DataBindingComponent dataBindingComponent;
//    private final SwitchClickCallback switchClickCallback;
    private final DeviceClickCallback switchClickCallback;

    public SwitchListAdapter(DataBindingComponent dataBindingComponent,
                             DeviceClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.switchClickCallback = callback;
    }

    @Override
    protected SwitchItemBinding createBinding(ViewGroup parent, int viewType) {
        SwitchItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), viewType,
                        parent, false, dataBindingComponent);

        binding.setCallback(switchClickCallback);
        return binding;
    }

    @Override
    protected void bind(SwitchItemBinding binding, Node item, int position) {
        binding.setData(item);
    }

    @Override
    protected boolean areItemsTheSame(Node oldItem, Node newItem) {
        return oldItem.id.equals(newItem.id) && oldItem.deviceId.equals(newItem.deviceId);
    }

    @Override
    protected boolean areContentsTheSame(Node oldItem, Node newItem) {
        return oldItem.id.equals(newItem.id) && oldItem.deviceId.equals(newItem.deviceId);

    }

    @Override
    protected int getLayoutId(Node item, int position) {
        return R.layout.switch_item;
    }

}