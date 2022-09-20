package com.bard.gplearning.mvvm.base;

public interface ICustomView<VIEW_MODEL extends BaseCustomViewModel>{
    void setData(VIEW_MODEL data);
}
