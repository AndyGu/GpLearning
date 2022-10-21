package com.bard.gplearning.lifecycle

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun main(){
    //1.
    println(runTaskSuspend())

    //2.
    try {
        println(sendRequestSuspend())
    }catch (e: Exception){
        println("send request: $e")
    }

    //3.
    val scope = CoroutineScope(Dispatchers.Default)
    scope.launch {
        try {
            println(sendRequestCancelableSuspend())
        }catch (e: Exception){
            println("send cancelable request: $e")
        }
    }

    delay(100)
    scope.cancel()

    //4.
    val scope1 = CoroutineScope(Dispatchers.Default)
    scope1.launch {
        startTaskAsFlow().collect {
            when(it){
                OnComplete -> println("Donw")
                is OnError -> println("Error: ${it.t}")
                is OnProgress -> println("Progress: ${it.value}")
                is OnResult<*> -> println("Result: ${it.value}")
                else -> {}
            }
        }
    }

    delay(1000)
    scope1.cancel()
}


/**
 * delayed task, like handler.post
 */
fun interface SingleMethodCallback{
    fun onCallback(value: String)
}

fun runTask(callback: SingleMethodCallback){
    thread {
        Thread.sleep(1000)
        callback.onCallback("runTask onCallback result")
    }
}

//转成挂起函数
suspend fun runTaskSuspend() = suspendCoroutine<String> { continuation ->
    runTask{
        continuation.resume(it)
    }
}





/**
 *  like Http request callback
 *  Dialog yes/no
 */
interface SuccessOrFailureCallback{
    fun onSuccess(value: String)

    fun onFailure(t: Throwable)
}

fun sendRequest(callback: SuccessOrFailureCallback){
    thread {
        try {
            Thread.sleep(1000)
            callback.onSuccess("Success")
        }catch (e: Exception){
            callback.onFailure(e)
        }
    }
}

suspend fun sendRequestSuspend() = suspendCoroutine<String> { continuation ->
    sendRequest(object : SuccessOrFailureCallback{
        override fun onSuccess(value: String) {
            continuation.resume(value)
        }

        override fun onFailure(t: Throwable) {
            continuation.resumeWithException(t)
        }
    })
}






fun interface Cancelable{
    fun cancel()
}

fun sendRequestCancelable(callback: SuccessOrFailureCallback): Cancelable{
    val t = thread {
        try {
            Thread.sleep(1000)
            callback.onSuccess("Success")
        }catch (e: Exception){
            callback.onFailure(e)
        }
    }

    return Cancelable {
        t.interrupt()
    }
}


suspend fun sendRequestCancelableSuspend() = suspendCancellableCoroutine<String> { cancellableContinuation ->
    val cancelable = sendRequestCancelable(object: SuccessOrFailureCallback{
        override fun onSuccess(value: String) {
            cancellableContinuation.resume(value)
        }

        override fun onFailure(t: Throwable) {
            cancellableContinuation.resumeWithException(t)
        }
    })

    cancellableContinuation.invokeOnCancellation {
        cancelable.cancel()
    }
}











interface MultiPathsCallback<T>{
    fun onProgress(value: Int)

    fun onResult(value: T)

    fun onError(t: Throwable)

    fun onComplete()
}

fun startTask(callback: MultiPathsCallback<String>): Cancelable{
    val t = thread {
        try {
            (0..100).forEach {
                Thread.sleep(50)
                callback.onProgress(it)
            }
            callback.onResult("Done")
            callback.onComplete()
        } catch (e : Exception){
            callback.onError(e)
        }
    }

    return Cancelable {
        t.interrupt()
    }
}

//密封接口
sealed interface Event
class OnProgress(val value: Int): Event
class OnError(val t: Throwable): Event
class OnResult<T>(val value: T): Event
object OnComplete: Event

fun startTaskAsFlow() = callbackFlow {
    val cancelable = startTask(object : MultiPathsCallback<String>{
        override fun onProgress(value: Int) {
            trySendBlocking(OnProgress(value))
        }

        override fun onResult(value: String) {
            trySendBlocking(OnResult(value))
        }

        override fun onError(t: Throwable) {
            trySendBlocking(OnError(t))
        }

        override fun onComplete() {
            trySendBlocking(OnComplete)
            close()
        }
    })

    //消费协程的取消，如果不用，callbackFlow替换成 channelFlow，处理好构造块提前结束的问题
    awaitClose {
        cancelable.cancel()
    }
}.conflate() //处理背压策略











