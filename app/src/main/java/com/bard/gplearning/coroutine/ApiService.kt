package com.bard.gplearning.coroutine

import com.bard.gplearning.coroutine.bean.ApiResult
import com.bard.gplearning.coroutine.bean.Article
import com.bard.gplearning.coroutine.bean.Pagination
import com.bard.gplearning.coroutine.bean.Tree
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**接口定义*/
interface ApiService {
    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }


    /*获取文章树结构*/

    /**普通方式*/
    @GET("tree/json")
    fun getTree(): Call<ApiResult<MutableList<Tree>>>

    /*根据数结构下某个分支id，获取分支下的文章*/
    @GET("article/list/{page}/json")
    fun getArticleList(
            @Path("page") page: Int,
            @Query("cid") cid: Int
    ): Call<ApiResult<Pagination<Article>>>



    /**RxJava方式*/
    @GET("tree/json")
    fun getTreeByRx(): Observable<ApiResult<MutableList<Tree>>>

    @GET("article/list/{page}/json")
    fun getArticleListByRx(
            @Path("page") page: Int,
            @Query("cid") cid: Int
    ): Observable<ApiResult<Pagination<Article>>>



    /**Coroutine方式*/
    @GET("tree/json")
    suspend fun getTreeByCoroutines(): ApiResult<MutableList<Tree>>

    @GET("article/list/{page}/json")
    suspend fun getArticleListByCoroutines(
            @Path("page") page: Int,
            @Query("cid") cid: Int
    ): ApiResult<Pagination<Article>>
}