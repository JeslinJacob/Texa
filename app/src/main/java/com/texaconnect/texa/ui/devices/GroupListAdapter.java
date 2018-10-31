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
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.texaconnect.texa.BR;
import com.texaconnect.texa.R;
import com.texaconnect.texa.interfaces.GroupClickCallback;
import com.texaconnect.texa.model.GroupItem;
import com.texaconnect.texa.ui.common.DataBoundListAdapter;

/**
 * A RecyclerView adapter for {@link Object} class.
 */
public class GroupListAdapter extends DataBoundListAdapter<Object, ViewDataBinding> {
    private static final String TAG = "GroupListAdapter";
    private final DataBindingComponent dataBindingComponent;
    private final GroupClickCallback groupClickCallback;

    public GroupListAdapter(DataBindingComponent dataBindingComponent,
                            GroupClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.groupClickCallback = callback;
    }

    @Override
    protected ViewDataBinding createBinding(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), viewType,
                        parent, false, dataBindingComponent);

        binding.setVariable(BR.callback, groupClickCallback);
        return binding;
    }

    @Override
    protected void bind(ViewDataBinding binding, Object item, int position) {
        binding.setVariable(BR.data, item);
    }

    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        return oldItem.equals(newItem) && oldItem.equals(newItem);
    }

    @Override
    protected boolean areContentsTheSame(Object oldItem, Object newItem) {
        return oldItem.equals(newItem) && oldItem.equals(newItem);

    }

    @Override
    protected int getLayoutId(Object item, int position) {
        if (item instanceof GroupItem) {
            return R.layout.group_item;
        }else return R.layout.element_item;
    }
}