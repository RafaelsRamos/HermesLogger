package com.example.myapplication.debugToaster;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;

import com.example.myapplication.R;

public enum LogType {
    Error(R.layout.toast_layout, R.drawable.ic_error),
    Warning(R.layout.toast_layout, R.drawable.ic_warning),
    Debug(R.layout.toast_layout, R.drawable.ic_debug),
    Success(R.layout.toast_layout, R.drawable.ic_success);

    public int layoutResource;
    public int drawableResource;

    LogType(@LayoutRes int layoutResource, @DrawableRes int drawableResource) {
        this.layoutResource = layoutResource;
        this.drawableResource = drawableResource;
    }
}
