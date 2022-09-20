package com.bard.gplearning.mvvm.newslist.picturetitleview;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.bard.gplearning.R;
import com.bard.gplearning.databinding.PictureTitleViewBinding;
import com.bard.gplearning.databinding.TitleViewBinding;
import com.bard.gplearning.mvvm.base.BaseCustomViewModel;
import com.bard.gplearning.mvvm.base.ICustomView;
import com.bard.gplearning.mvvm.newslist.titleview.TitleViewViewModel;

public class PictureTitleView extends LinearLayout implements ICustomView<PictureTitleViewViewModel> {

    private PictureTitleViewBinding mBinding;
    private PictureTitleViewViewModel mViewModel;


    public PictureTitleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.picture_title_view, this, false);
        mBinding.getRoot().setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.e("PictureTitleView", "link="+mViewModel.link + " title-"+mViewModel.title);
            }
        });

        addView(mBinding.getRoot());
    }


    @Override
    public void setData(PictureTitleViewViewModel data) {
        mBinding.setViewModel(data);
        mBinding.executePendingBindings();
        this.mViewModel = data;
    }
}
