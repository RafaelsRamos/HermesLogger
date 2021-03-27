package com.example.myapplication.debugToaster;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;

import com.example.myapplication.R;

public enum ToastType {
    Success(R.layout.toast_layout, R.drawable.ic_success),
    Error(R.layout.toast_layout, R.drawable.ic_error),
    Warning(R.layout.toast_layout, R.drawable.ic_warning),
    Debug(R.layout.toast_layout, R.drawable.ic_debug);

    public int layoutResource;
    public int drawableResource;

    ToastType(@LayoutRes int layoutResource, @DrawableRes int drawableResource) {
        this.layoutResource = layoutResource;
        this.drawableResource = drawableResource;
    }
}
