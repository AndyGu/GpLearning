package com.bard.gplearning.coroutine

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bard.gplearning.coroutine.bean.ApiResult
import com.bard.gplearning.coroutine.bean.Article
import com.bard.gplearning.coroutine.bean.Pagination
import com.bard.gplearning.coroutine.bean.Tree
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

/**RetrofitClient单例*/
object RetrofitClient {
    /**log**/
    private val logger = HttpLoggingInterceptor.Logger {
        Log.i(this::class.simpleName, it)
    }
    private val logInterceptor = HttpLoggingInterceptor(logger).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**OkhttpClient*/
    private val okHttpClient = OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .addNetworkInterceptor(logInterceptor)
            .build()
    /**Retrofit*/
    private val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ApiService.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    /**ApiService*/
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}


/**ViewModel*/
class SystemViewModel : ViewModel(){
    private val remoteRepository : SystemRemoteRepository by lazy { SystemRemoteRepository() }

    val page = MutableLiveData<Pagination<Article>>()

    fun getArticleList() {
        remoteRepository.getArticleList(){
            page.value = it
        }
    }


    private val scope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext = Job()
    }

    fun getArticleListByCoroutine() {
        scope.launch {  //主线程开启一个协程
            // 网络请求：IO线程
            val tree : ApiResult<MutableList<Tree>> = RetrofitClient.apiService.getTreeByCoroutines()
            // 主线程
            val cid = tree.data?.get(0)?.id
            if(cid!=null){
                // 网络请求：IO线程
                val pageResult : ApiResult<Pagination<Article>> = RetrofitClient.apiService.getArticleListByCoroutines(0, cid)
                // 主线程
                page.value = pageResult.data!!
            }
        }
    }
}

/**数据仓库*/
class SystemRemoteRepository{
    /**
     * 1. 展示回调嵌套，回调地狱
     */
    fun getArticleList(responseBack: (result: Pagination<Article>?) -> Unit) {
        /**1. 获取文章树结构*/
        val call: Call<ApiResult<MutableList<Tree>>> = RetrofitClient.apiService.getTree()
        //同步（需要自己手动切换线程）
        //val response : Response<ApiResult<MutableList<Tree>>> = call.execute()
        //异步回调
        call.enqueue(object : Callback<ApiResult<MutableList<Tree>>> {
            override fun onFailure(call: Call<ApiResult<MutableList<Tree>>>, t: Throwable) {
            }
            override fun onResponse(call: Call<ApiResult<MutableList<Tree>>>, response: Response<ApiResult<MutableList<Tree>>>) {
                Log.v("SystemRemoteRepository","请求文章树结构成功："+response.body())

                /**2. 获取分支id下的第一页文章*/
                val treeid = response.body()?.data?.get(0)?.id
                //当treeid不为null执行
                treeid?.let {
                    RetrofitClient.apiService.getArticleList(0, treeid)
                            .enqueue(object : Callback<ApiResult<Pagination<Article>>> {
                                override fun onFailure(call: Call<ApiResult<Pagination<Article>>>, t: Throwable) {
                                }
                                override fun onResponse(call: Call<ApiResult<Pagination<Article>>>, response: Response<ApiResult<Pagination<Article>>>) {
                                    //返回获取的文章列表
                                    responseBack(response.body()?.data)
                                }
                            })
                }
            }
        })
    }




    /**
     * 2. Retrofit+RxJava消除回调嵌套
     */
    fun getArticleListByRx(responseBack: (result: Pagination<Article>?) -> Unit) {
        /**1. 获取文章树结构*/
        val observable1: Observable<ApiResult<MutableList<Tree>>> = RetrofitClient.apiService.getTreeByRx()
        observable1.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //使用当前Observable发出的值调用给定的Consumer，然后将其转发给下游
                .doOnNext {
                    Log.v("getArticleListByRx", "1请求文章树成功，切换到主线程处理数据：${Thread.currentThread()}")
                }
                .observeOn(Schedulers.io())
                .flatMap {
                    Log.v("getArticleListByRx", "2请求文章树成功，IO线程中获取接口1的数据，然后将被观察者变换为接口2的Observable：${Thread.currentThread()}")
                    if (it?.errorCode == 0) {
                        //当treeid不为null执行
                        it?.data?.get(0)?.id?.let { it1 -> RetrofitClient.apiService.getArticleListByRx(0, it1) }
                    } else {
                        //请求错误的情况，发射一个Error
                        Observable.error {
                            Throwable("获取文章树失败：${it.errorCode}:${it.errorMsg}")
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Observer<ApiResult<Pagination<Article>>> {
                    override fun onComplete() {}
                    override fun onSubscribe(d: Disposable?) {}
                    override fun onNext(t: ApiResult<Pagination<Article>>?) {
                        Log.v("","3请求文章列表成功：${t?.data}")
                        responseBack(t?.data)
                    }
                    override fun onError(e: Throwable?) {
                        Log.e("","3请求失败：${e?.message}")
                    }
                })
    }


}