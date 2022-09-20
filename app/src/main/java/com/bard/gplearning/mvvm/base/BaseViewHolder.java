package com.bard.gplearning.mvvm.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder{
    ICustomView view;

    public BaseViewHolder(ICustomView itemView) {
        super((View) itemView);
        this.view = itemView;
    }

    public void bind(@NonNull BaseCustomViewModel item){
        view.setData(item);
    }
}
