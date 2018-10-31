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

package com.texaconnect.texa.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Binding adapters that work with a fragment instance.
 */
public class FragmentBindingAdapters {
    final Fragment fragment;

    public FragmentBindingAdapters(Fragment fragment) {
        this.fragment = fragment;
    }

    @BindingAdapter("imageUrl")
    public void bindImage(ImageView imageView, String url) {
        Glide.with(fragment).load(url).into(imageView);
    }

    @BindingAdapter("imageUrl")
    public void bindImage(ImageView imageView, Drawable drawable) {
        Glide.with(fragment).load(drawable).into(imageView);
    }

    @BindingAdapter("imageUrlSupport")
    public void bindImage(AppCompatImageView imageView, Drawable drawable) {
        Glide.with(fragment).load(drawable).into(imageView);
    }

    @BindingAdapter("circleImageUrl")
    public void bindCircleImage(ImageView imageView, String url) {

        Glide.with(fragment).load(url).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    @BindingAdapter("setSwitchState")
    public void setSwitchState(ImageView imageView, int url) {

        Glide.with(fragment).load(url).into(imageView);
    }

   /* @BindingAdapter({"bind:setSwitchAdapter", "bind:setSwitchCallback", "bind:setSwitchComponent"})
    public void setSwitchAdapter(){

    }*/
}