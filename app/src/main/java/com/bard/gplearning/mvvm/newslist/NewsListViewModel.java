package com.bard.gplearning.mvvm.newslist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.bard.gplearning.mvvm.base.BaseCustomViewModel;
import com.bard.gplearning.mvvm.base.model.IBaseModelListener;
import com.bard.gplearning.mvvm.base.model.PagingResult;

import java.util.ArrayList;
import java.util.List;

import static com.bard.gplearning.mvvm.newslist.NewsListFragment.BUNDLE_KEY_PARAM_CHANNEL_ID;
import static com.bard.gplearning.mvvm.newslist.NewsListFragment.BUNDLE_KEY_PARAM_CHANNEL_NAME;

public class NewsListViewModel extends ViewModel implements IBaseModelListener<List<BaseCustomViewModel>> {
    public MutableLiveData<List<BaseCustomViewModel>> dataList = new MutableLiveData<>();
    NewsListModel model;
    public MutableLiveData<ViewStatus> viewStatusLiveData = new MutableLiveData<>();
    public String errorMessage;


    public NewsListViewModel(SavedStateHandle savedStateHandle) {
        dataList.setValue(new ArrayList<BaseCustomViewModel>());
        model = new NewsListModel((String)savedStateHandle.get(BUNDLE_KEY_PARAM_CHANNEL_ID), (String)savedStateHandle.get(BUNDLE_KEY_PARAM_CHANNEL_NAME), this);
        model.load();
    }

    public void refresh(){
        model.refresh();
    }

    public void tryToLoadNextPage(){
        model.loadNextPage();
    }

    @Override
    public void onLoadFinish(List<BaseCustomViewModel> data, PagingResult... pageResult) {
        if(pageResult[0].isFirstPage){
            dataList.getValue().clear();
        }

        if(pageResult[0].isEmpty){
            if(pageResult[0].isFirstPage){
                viewStatusLiveData.postValue(ViewStatus.EMPTY);
            } else{
                viewStatusLiveData.postValue(ViewStatus.NO_MORE_DATA);
            }
        }else{
            dataList.getValue().addAll(data);
            dataList.postValue(dataList.getValue());
            viewStatusLiveData.postValue(ViewStatus.SHOW_CONTENT);
        }
    }

    @Override
    public void onLoadFail(String prompt, PagingResult... pageResult) {
        errorMessage = prompt;
        if(!pageResult[0].isFirstPage){
            viewStatusLiveData.postValue(ViewStatus.LOAD_MORE_FAILED);
        }else{
            viewStatusLiveData.postValue(ViewStatus.REFRESH_ERROR);
        }
    }
}
