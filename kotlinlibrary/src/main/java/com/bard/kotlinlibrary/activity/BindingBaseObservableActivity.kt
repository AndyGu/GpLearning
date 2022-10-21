package com.bard.kotlinlibrary.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bard.kotlinlibrary.R
import com.bard.kotlinlibrary.databinding.ActivityBindingBaseobservableBinding
import com.bard.kotlinlibrary.viewmodel.UserBaseObservable

/**
 * databinding
 *
 * xml布局初始化
 * 1.setContentView 获取到decorView，进行解析，将一个layout进行分离，
 * 生成两个xml ：
 * 一个是剥离了<data> 和 元素引用的 xml，每个控件都标记了 tag
 * kotlinlibrary/build/intermediates/
 *      incremental/debug/packageDebugResources/stripped.dir/layout/activity_binding_observablefield.xml
 *
 * 一个是 关联了tag 和 控件view类型 以及记录了其他信息的xml
 * kotlinlibrary/build/intermediates/
 *      data_binding_layout_info_type_package/debug/out/activity_binding_baseobservable-layout.xml
 *
 * 2.bindToAddedViews 调用/循环调用 bind方法，进而调用 sMapper 的 getDataBinder方法
 *      sMapper 是一个apt生成的 DataBinderMapper 的子类 MergedDataBinderMapper 的子类
 *      【注意，每个module都会有一个 DataBinderMapperImpl，】
 * kotlinlibrary/build/generated/source/kapt/debug/com/bard/kotlinlibrary/DataBinderMapperImpl.java
 *      getDataBinder 方法 会回调到各个module之中的 getDataBinder方法实现
 *
 * 3.根据收集到的、标记了的 layout 和 tag标签，找到相应布局，对apt生成的 ActivityBindingBaseobservableBindingImpl 进行初始化
 *      目的是为了将布局中的各个控件 保存在数组中的相应位置，完成注入以及xml的初始化
 *
 *
 * 总的来说，就是把xml解析出来，把对象放入 ActivityBindingBaseobservableBinding 对象 中保存
 *
 *
 *
 *
 *
 *
 *
 *
 * #时序图
 * Edit Configuration - Profiling - Start this record on startUp - CPU activity - Trace Java method
 *  Android 26以上
 *  点击Profile按钮，start-stop，在 profile 的 All Thread - Top Down 窗口可观察时序图
 *
 *
 *
 *
 *
 *
 *  mBinding.userBaseObservable = userBaseObservable
 *
 *  这个set方法，就在 ActivityBindingBaseobservableBindingImpl 实现类中
 *
 *  public void setUserBaseObservable(@Nullable com.bard.kotlinlibrary.viewmodel.UserBaseObservable UserBaseObservable) {
        updateRegistration(0, UserBaseObservable);
        this.mUserBaseObservable = UserBaseObservable;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.userBaseObservable);
        super.requestRebind();
    }
 *
 * 这个 id 0 ，就是APT 生成的 BR 里的 id
 *
 *
 * updateRegistration方法就是为各个属性 在 mLocalFieldObservers 数组对应的下标，创建监听器
 * 比如 BR.userBaseObservable = 6，那么就在 mLocalFieldObservers 下标为6的位置，为 userBaseObservable 创建属性变动监听器
 *
 *
 * Observer                                                         注册
 *
 * ViewDataBinding (被观察者)                                       CallbackRegistry
 * -------------------------                            --------------------------------
 * WeakPropertyListener CREATE_PROPERTY_LISTENER;       List<C> mCallbacks;(存放WeakPropertyListener)(订阅集合)
 * WeakListener[] mLocalFieldObservers; (观察者)
 * updateRegistration                                              PropertyChangeRegistry
 *                                                      ---------------------------------
 * ActivityMainBinding                                  add(OnPropertyChangedCallback)
 *
 * ActivityMainBindingImpl                              listener.setTarget(observable);
 *                                                      mObservable.addListener(mTarget);
 *                                                      mCallbacks.add(callback);
 *                                                      为被观察者设置属性改变时的监听
 *                                                      target.addOnPropertyChangedCallback(this);
 *                                                      observable与observer绑定
 *
 *                                                                  监听
 *                                                      CreateWeakListener
 *
 *                                                      WeakListener(观察者)
 *                                                      --------------------
 *                                                      private T mTarget; 存放了user对象
 *                                                      volatile T referent; 存放了ViewDataBinding
 *
 *                                                      WeakPropertyListener
 *                                                      ---------------------
 *                                                      WeakListener<Observable> mListener
 *                                                      addListener()
 *                                                      onPropertyChanged(id) 发送消息的api
 *
 *
 *
 * 属性变化，会通过 ViewDataBinding内部类 WeakPropertyListener 的 onPropertyChanged 方法回调
 *      调到 ViewDataBinding 的 handleFieldChange
 *        然后 调用 onFieldChange 通知到 ActivityBindingBaseobservableBindingImpl 修改了 mDirtyFlags 标志位
 *      如果返回有改动， handleFieldChange 中继续 调用 requestRebind()
 *      在Android 16以上，直接 mChoreographer.postFrameCallback(mFrameCallback);
 *      通知编舞者
 *
 *      然后看 mFrameCallback 回调 doFrame 中的 mRebindRunnable.run();
 *
 *      run方法 -- executePendingBindings() -- executeBindingsInternal() -- executeBindings()
 *
 *      executeBindings() 的实现在 ActivityBindingBaseobservableBindingImpl
 *      所以又回来了，看 ActivityBindingBaseobservableBindingImpl 中的 executeBindings
 *
 *      再通过 dirtyFlags 取属性 和 setText【或其他UI操作】
 *
 *
 *
 *
 *
 *
 */
class BindingBaseObservableActivity : AppCompatActivity() {

    lateinit var userBaseObservable: UserBaseObservable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mBinding: ActivityBindingBaseobservableBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_binding_baseobservable)

        userBaseObservable = UserBaseObservable()
        mBinding.userBaseObservable = userBaseObservable

        mBinding.button.setOnClickListener {
            userBaseObservable.setName("882230")
        }
    }
}