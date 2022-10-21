package com.bard.kotlinlibrary.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bard.kotlinlibrary.R
import com.bard.kotlinlibrary.viewmodel.MyNumberViewModel
import kotlinx.android.synthetic.main.activity_mvvm.*

class ViewModelActivity: AppCompatActivity() {

    private lateinit var myNumberViewModel : MyNumberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvvm)

        /**
         * 源码分析1
         * MyViewModel是不能用构造函数new的，只能通过工厂获取，这样也保证了ViewModel的"可共享"特性
         *
         * ComponentActivity 实现了 参数1：ViewModelStoreOwner接口
         *
         * ViewModelProvider的构造函数 调用了 this(owner.getViewModelStore(), factory);
         *
         * owner.getViewModelStore() 返回的 ViewModelStore 其实就是个包含着一个HashMap的对象，
         * ViewModelStore就相当于是个存储库、数据库、db，专门存储ViewModel的
         * private final HashMap<String, ViewModel> mMap = new HashMap<>();
         *
         * 然而 getViewModelStore() 是 ViewModelStoreOwner的一个接口，它是在哪实现的呢？
         * 答案是 实现 ViewModelStoreOwner 接口的爷爷类 ComponentActivity
         *
         *
            @Override
            public ViewModelStore getViewModelStore() {
                if (getApplication() == null) {
                    throw new IllegalStateException("Your activity is not yet attached to the "
                    + "Application instance. You can't request ViewModel before onCreate call.");
                }
                ensureViewModelStore();
                return mViewModelStore;
            }

            void ensureViewModelStore() {
                if (mViewModelStore == null) {  //首次进肯定为null
                    NonConfigurationInstances nc =
                        (NonConfigurationInstances) getLastNonConfigurationInstance();  //调到了
                    if (nc != null) {
                        // Restore the ViewModelStore from NonConfigurationInstances
                        mViewModelStore = nc.viewModelStore;  //转屏之前的数据，viewModelStore里有hashmap，hashmap里有viewmodel
                    }
                    if (mViewModelStore == null) {
                        mViewModelStore = new ViewModelStore();
                    }
                }
            }
         *
         * 可以看出，从 getLastNonConfigurationInstance 取出的nc对象 类型是NonConfigurationInstances，
         * mViewModelStore 是nc对象中的一个属性，那么， 这个nc是在何时放进去的呢？mViewModelStore又是什么时候set的？
         * 答案是
         * 1. NonConfigurationInstances 是 Activity attach时赋值 和 performResume置空
         * 2. NonConfigurationInstances 中的 mViewModelStore 是执行 onRetainNonConfigurationInstance 方法时，系统放的，
         *      而且，这个方法是final的，不能覆写
         *
         * 源码分析2
         * get(MyViewModel::class.java)
         * 看到传class，想都不想，肯定是要做反射用的
         * 看了源码，果然是反射newInstance实例化用的
         *
         * 总结，这句代码执行完，通过我们的NewInstanceFactory工厂，反射生成MyViewModel对象，保存到了ViewModelStore
         *
         *
         * 总总结：
         * 也就是说，ViewModelProvider构造函数 --> owner.getViewModelStore() -> ComponentActivity的实现，赋值、返回 mViewModelStore,
         * mViewModelStore 是通过 getLastNonConfigurationInstance得到的
         * mViewModelStore 内包含着 mMap, mMap 内包含着 ViewModel【这个ViewModel是get时，去反射初始化，然后put放入的，key是 DEFAULT_KEY + ":" + canonicalName】
         *
         *
         * 源码分析3：
         * ComponentActivity的构造函数中
         *
         * getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source,
                        @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        mContextAwareHelper.clearAvailableContext();
                        if (!isChangingConfigurations()) {
                            getViewModelStore().clear();
                        }
                    }
                }
            });
         *
         * 这说明了 除非生命周期走到了onDestroy,ViewModelStore是不会清空的，说明它生命周期够长，也就是为什么转屏都不会变
         */
        myNumberViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MyNumberViewModel::class.java)

        tv_counter.text = "${myNumberViewModel.number}"

        btn_plus.setOnClickListener{
            tv_counter.text = "${++myNumberViewModel.number}"
        }
    }

    /**
     * 每次Activity转屏重建，是靠这个方法激活ViewModel的
     * 而这个方法是由AMS激活的
     */
    override fun onRetainCustomNonConfigurationInstance(): Any? {
        Log.e("ViewModelActivity","onRetainCustomNonConfigurationInstance")
        return super.onRetainCustomNonConfigurationInstance()
    }

    override fun getLastNonConfigurationInstance(): Any? {
        Log.e("ViewModelActivity","getLastNonConfigurationInstance")
        return super.getLastNonConfigurationInstance()
    }
}