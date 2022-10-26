package com.bard.gplearning.mvvm.newslist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bard.gplearning.MyApplication;
import com.bard.gplearning.R;
import com.bard.gplearning.databinding.FragmentNewsBinding;
import com.bard.gplearning.mvvm.base.BaseCustomViewModel;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

public class NewsListFragment extends Fragment implements Observer {
    private NewsListRecyclerViewAdapter mAdapter;
    private FragmentNewsBinding viewDataBinding;
    private LoadService mLoadService;

    protected final static String BUNDLE_KEY_PARAM_CHANNEL_ID = "bundle_key_param_channel_id";
    protected final static String BUNDLE_KEY_PARAM_CHANNEL_NAME = "bundle_key_param_channel_name";
    NewsListViewModel viewModel;

    public static NewsListFragment newInstance(String channelId, String channelName) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_ID, channelId);
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_NAME, channelName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String channelId = getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_ID);
        String channelName = getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_NAME);
        viewModel = new ViewModelProvider(getActivity(),
                new SavedStateViewModelFactory(MyApplication.sApplication, getActivity(), getArguments()))
                    .get(channelId + channelName, NewsListViewModel.class);

        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        mAdapter = new NewsListRecyclerViewAdapter();
        viewDataBinding.listview.setHasFixedSize(true);
        viewDataBinding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        viewDataBinding.listview.setAdapter(mAdapter);

        viewModel.dataList.observe(getViewLifecycleOwner(), new Observer<List<BaseCustomViewModel>>() {
            @Override
            public void onChanged(List<BaseCustomViewModel> baseCustomViewModels) {
                viewDataBinding.refreshLayout.finishRefresh();
                viewDataBinding.refreshLayout.finishLoadMore();
                mAdapter.setData(baseCustomViewModels);
            }
        });
        viewDataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.refresh();
            }
        });
        viewDataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.tryToLoadNextPage();
            }
        });

        viewModel.viewStatusLiveData.observe(getViewLifecycleOwner(), this);
        mLoadService = LoadSir.getDefault().register(viewDataBinding.refreshLayout, new Callback.OnReloadListener(){
            @Override
            public void onReload(View v){
                viewModel.refresh();
            }
        });

        return viewDataBinding.getRoot();
    }


    @Override
    public void onChanged(Object o) {
        if(o instanceof ViewStatus && mLoadService != null){
            viewDataBinding.refreshLayout.finishRefresh();
            viewDataBinding.refreshLayout.finishLoadMore();
            switch ((ViewStatus)o){
                case LOADING:
//                    mLoadService.showCallback(LoadingCallback.class);
                    break;

                case EMPTY:
//                    mLoadService.showCallback(EmptyCallback.class);
                    break;

                case SHOW_CONTENT:
                    mLoadService.showSuccess();
                    break;

                case NO_MORE_DATA:
//                    ToastUtil.show(getString(R.string.no_more_data));
                    break;

                case REFRESH_ERROR:
                    if(viewModel.dataList.getValue().size() == 0){
//                        mLoadService.showCallback(ErrorCallback.class);
                    }else{
//                        ToastUtil.show(viewModel.errorMessage);
                    }
                    break;

                case LOAD_MORE_FAILED:
//                    ToastUtil.show(viewModel.errorMessage);
                    break;
            }
        }
    }
}




































