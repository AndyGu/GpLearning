package com.bard.gplearning.mvvm.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseCustomView<DATA_BINDING extends ViewDataBinding, VIEW_MODEL extends BaseCustomViewModel>
        extends LinearLayout implements ICustomView<VIEW_MODEL>, View.OnClickListener {

    private DATA_BINDING dataBinding;
    private VIEW_MODEL viewModel;

    public BaseCustomView(Context context) {
        super(context);
        init();
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(getViewLayoutId() != 0){
            dataBinding = DataBindingUtil.inflate(inflater, getViewLayoutId(), this, false);
            dataBinding.getRoot().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRootClick(v);
                }
            });
            this.addView(dataBinding.getRoot());
        }
    }

    @Override
    public void setData(VIEW_MODEL data) {
        viewModel = data;
        setDataToView(viewModel); //工厂方法设计模式？？？
        if(dataBinding != null){
            dataBinding.executePendingBindings();
        }
    }

    @Override
    public void onClick(View v) {
    }


    protected DATA_BINDING getDataBinding(){
        return dataBinding;
    }

    protected VIEW_MODEL getViewModel(){
        return viewModel;
    }

    protected abstract int getViewLayoutId();

    protected abstract void setDataToView(VIEW_MODEL viewModel);

    protected abstract void onRootClick(View v);
}
