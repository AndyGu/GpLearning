package com.bard.gplearning.mvvm.newslist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bard.gplearning.mvvm.base.BaseCustomViewModel;
import com.bard.gplearning.mvvm.base.BaseViewHolder;
import com.bard.gplearning.mvvm.newslist.picturetitleview.PictureTitleView;
import com.bard.gplearning.mvvm.newslist.picturetitleview.PictureTitleViewViewModel;
import com.bard.gplearning.mvvm.newslist.titleview.TitleView;
import com.bard.gplearning.mvvm.newslist.titleview.TitleViewViewModel;

import java.util.List;

public class NewsListRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder>{

    private List<BaseCustomViewModel> mItems;
    private final int VIEW_TYPE_PICTURE_TITLE =1;
    private final int VIEW_TYPE_TITLE = 2;

    public NewsListRecyclerViewAdapter() {
    }

    void setData(List<BaseCustomViewModel> items){
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mItems != null && mItems.size() > 0){
            return mItems.size();
        }
        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        if(mItems.get(position) instanceof PictureTitleViewViewModel){
            return VIEW_TYPE_PICTURE_TITLE;
        }else if(mItems.get(position) instanceof TitleViewViewModel){
            return VIEW_TYPE_TITLE;
        }
        return -1;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_PICTURE_TITLE){
            return new BaseViewHolder(new PictureTitleView(parent.getContext()));
        }else if(viewType ==  VIEW_TYPE_TITLE){
            return new BaseViewHolder(new TitleView(parent.getContext()));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }


}
