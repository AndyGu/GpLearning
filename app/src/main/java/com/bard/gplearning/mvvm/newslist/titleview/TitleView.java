package com.bard.gplearning.mvvm.newslist.titleview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.bard.gplearning.R;
import com.bard.gplearning.mvvm.base.BaseCustomView;
import com.bard.gplearning.mvvm.base.ICustomView;
import com.bard.gplearning.databinding.TitleViewBinding;

public class TitleView extends BaseCustomView<TitleViewBinding, TitleViewViewModel> {

    public TitleView(Context context) {
        super(context);
        init();
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.title_view;
    }

    @Override
    protected void setDataToView(TitleViewViewModel viewModel) {
        getDataBinding().setNewsModel(viewModel);
    }

    @Override
    protected void onRootClick(View v) {
        Log.e("TitleView","onRootClick "+getViewModel().jumpUri);
    }
}
