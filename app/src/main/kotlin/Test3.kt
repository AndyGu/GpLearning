package com.bard.gplearning.lifecycle

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KMutableProperty0

suspend fun main(){
//    eatGame()

    fun String.lastChar() = get(length - 1)

    val name = "leavesC"
    println("$name lastChar is: " + name.lastChar())

    val age = 24
    println("$age doubleValue is: " + age.doubleValue())
}

//为 String 类声明一个扩展函数 lastChar() ，用于返回字符串的最后一个字符
//get方法是 String 类的内部方法，length 是 String 类的内部成员变量，在此处可以直接调用
//fun String.lastChar() = get(length - 1)

//为 Int 类声明一个扩展函数 doubleValue() ，用于返回其两倍值
//this 关键字代表了 Int 值本身
fun Int.doubleValue() = this * 2


suspend fun noSuspend(){
    println("noSuspend called")
}

suspend fun suspended(){
    delay(100)
    println("resumed after 100ms")
}

suspend fun fakeSuspend() = suspendCoroutine<Int> {
    it.resume(1)
}

suspend fun fakeSuspend2() = suspendCoroutine<Int> {
    Thread.sleep(100)
    it.resume(1)
}

suspend fun realSuspend() = suspendCoroutine<Int> {
    thread {
        Thread.sleep(100)
        it.resume(1)
    }
}



class EatGame{
    private var feedContinuation: Continuation<Int>? = null
    private var eatContinuation: Continuation<String>? = null
    private var eatAttempts = 0

    var isActive: Boolean = true
        private set

    suspend fun eat(): String{
        return if(isActive) suspendCoroutine<String> {
            this.eatContinuation = it
            resumeContinuation(this::feedContinuation, eatAttempts++)
        } else ""
    }

    suspend fun feed(food: String): Int{
        return if(isActive) suspendCoroutine {
            this.feedContinuation = it
            resumeContinuation(this::eatContinuation, food)
        } else -1
    }

    fun timeout(){
        isActive = false
        resumeContinuation(this::feedContinuation, eatAttempts)
        resumeContinuation(this::eatContinuation, "")
    }

    private fun <T> resumeContinuation(
        continuationRef: KMutableProperty0<Continuation<T>?>,
        value: T
    ){
        val continuation = continuationRef.get()
        continuationRef.set(null)
        continuation?.resume(value)
    }
}

suspend fun eatGame(){
    coroutineScope {
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val game = EatGame()
        launch(dispatcher) {
            println("Ready Go")
            delay(1000)
            game.timeout()
            println("Timeout!")
        }

        launch(dispatcher) {
            while(game.isActive){
                delay(60)
                val food = Math.random()
                println("[${Thread.currentThread().name} #1] Feed $food >>>")
                println("[${Thread.currentThread().name} #1] Complete ${game.feed("$food")}")
            }
        }

        launch(dispatcher) {
            while(game.isActive){
                delay(50)
                println("[${Thread.currentThread().name} #2] Eat ${game.eat()} >>>")
            }
        }
    }
}