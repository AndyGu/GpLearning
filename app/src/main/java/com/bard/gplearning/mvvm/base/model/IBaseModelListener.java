package com.bard.gplearning.mvvm.base.model;

public interface IBaseModelListener<DATA> {
    void onLoadFinish(DATA data, PagingResult... pageResult);

    void onLoadFail(String prompt, PagingResult... pageResult);
}
