package com.bard.gplearning.mvvm.newslist;

import com.bard.gplearning.mvvm.base.BaseCustomViewModel;
import com.bard.gplearning.mvvm.base.model.IBaseModelListener;
import com.bard.gplearning.mvvm.base.model.PagingResult;
import com.bard.gplearning.mvvm.newslist.picturetitleview.PictureTitleViewViewModel;
import com.bard.gplearning.mvvm.newslist.titleview.TitleViewViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewsListModel {
    private IBaseModelListener<List<BaseCustomViewModel>> iBaseModelListener;
    private String mChannelId = "";
    private String mChannelName = "";
    private boolean isRefresh;
    private int pageNumber;

    public NewsListModel(String mChannelId, String mChannelName, IBaseModelListener<List<BaseCustomViewModel>> iBaseModelListener) {
        this.iBaseModelListener = iBaseModelListener;
        this.mChannelId = mChannelId;
        this.mChannelName = mChannelName;
    }

    public void refresh(){
        isRefresh = true;
        load();
    }

    public void loadNextPage(){
        isRefresh = false;
        load();
    }

    protected void load(){
//        TecentNetworkApi.getService(NewsApiInterface.class)
//                .getNewsList(mChannelId, mChannelName, String.valueOf(isRefresh ? 1 : pageNumber))
//                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsListBean>(){
//                    @Override
//                    public void onSuccess(NewsLitBean newsChannelsBean){
//                        pageNumber = isRefresh ? 2 : pageNumber + 1;
//                        ArrayList<BaseCustomViewModel> baseViewModels = new ArrayList<>();
//
//                        for(NewsListModel.Contentlist source : newsChannelsBean.showapiResBody.pagebean.contentlist){
//                            if(source.imageurls != nubll && source.imageurls.size() > 1){
//                                PictureTitleViewViewModel viewModel = new PictureTitleViewViewModel();
//                                viewModel.avatarUrl = source.imageurls.get(0).url;
//                                viewModel.jumpUri = source.link;
//                                viewModel.title = source.title;
//                                baseViewModels.add(viewModel);
//                            }else{
//                                TitleViewViewModel viewModel = new TitleViewViewModel();
//                                viewModel.jumpUri = source.link;
//                                viewModel.title = source.title;
//                                baseViewModels.add(viewModel);
//                            }
//                        }
//                        iBaseModelListener.onLoadFinish(baseViewModels, new PagingResult(baseViewModels.size() ==0, isRefresh, baseViewModels.size() == 0 ));
//                    }
//
//                    @Override
//                    public void onFailure(Throwable e){
//                        e.printStackTrace();
//
//                        iBaseModelListener.onLoadFail(e.getMessage(), new PagingResult(true, isRefresh, false));
//
//                    }
//                }));
    }
}
