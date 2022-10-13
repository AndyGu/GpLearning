package com.bard.gplearning.lifecycle

import android.app.Activity
import android.content.Intent
import android.util.Log

fun main() {
}

inline fun <reified T : Activity> Activity.to() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T, R> testt(t: T, r: R) {
    val c = T::class.java
    val t = t!!::class.java
//    val r = R::class.java //报错
}


fun fun1(doSomething: () -> Unit) {
    Log.e("tag", "1")
    doSomething()
}

fun mainFun() {
    fun1 {
        //do anything
        Log.e("tag", "2")
    }
}

fun mainFun2() {
    for (i in 1..10000) { //每个函数参数，都会编译成一个对象，耗费资源，如同在onDraw里实例化局部变量
        fun1 {
            //do anything
            Log.e("tag", "2")
        }
    }
}

//编译的代码
fun mainFun2_() {
    for (i in 1..10000) {
        val f = object : Function0<Unit> {
            override fun invoke() {
                Log.e("tag", "2")
            }
        }
        fun1(f)
    }
}

//每个函数参数，都会在编译时先实例化成一个匿名对象，再传入fun1，如果是在循环调用，就会耗费资源，如同在onDraw里实例化局部变量

//kotlin用inline在此做优化


inline fun fun3(doSomething: () -> Unit) {
    Log.e("tag", "3")
    doSomething()
}

fun mainFun3() {
    for (i in 1..10000) {
        fun3 {
            //do anything
            Log.e("tag", "4")
        }
    }
}

//编译的代码
fun mainFun3_() {
    for (i in 1..10000) {
        Log.e("tag", "3")
        Log.e("tag", "4")
    }
}


//////////////////// noinline
//noinline的意思就是不内联，这个关键字《只能》作用于《内联高阶函数的某个函数类型的参数》上，表明当前的函数参数不参与高阶函数的内联

inline fun fun4(doSomething1: () -> Unit, noinline doSomething2: () -> Unit) {
    Log.e("tag", "1")
    doSomething1()
    doSomething2()
}

fun mainFun4() {
    fun4({ Log.e("tag", "11") }, { Log.e("tag", "12") })
}

//编译代码
fun mianFun4_() {
    Log.e("tag", "1")
    Log.e("tag", "11")
    ({ Log.e("tag", "12") }).invoke()
}

// 在kotlin中 高阶函数的函数类型的参数 我们可以直接当做一个函数去调用，
// 但是函数类型的参数《终究还是一个对象》，既然是一个对象那么我们就可以《以对象的形式去使用》，
// 就比如说作为函数返回值进行返回：

inline fun fun5(doSomething1: () -> Unit, noinline doSomething2: () -> Unit): ()->Unit{
    Log.e("tag", "1")
    doSomething1()
    doSomething2()
    return doSomething2
}
//所以当我们在内联函数里面真的需要《将一个函数类型的参数》《作为对象去使用时》就需要 noinline关键字去修饰它


/////////////////////crossinline

//对inline做局部加强内联

inline fun fun6(doSomething1: () -> Unit){
    Log.e("tag", "1")
    doSomething1()
}

fun mainFun6(){
    fun6{
        Log.e("tag", "2")
        return
        // 按照一般的原则，这里的return结束的应该是fun1函数体中的逻辑，就是说Log.e("tag", "3")会被执行到。
        // 但是fun6作为内联函数会在编译时被完全铺平，
        // 所以，return的功效被扩大了，这里return结束的 变成了最外面mainFun6函数的逻辑，
        // 所以，Log.i("tag", "3")就不会被执行到。
    }

    Log.e("tag", "3")
}

//编译的代码
fun mainFun6_(){
    Log.e("tag","1")
    Log.e("tag","2")
    return
    Log.e("tag","3")
}

// 那我们在函数类型参数的lambda表达式中执行的return到底结束的是哪里？
// 是当前函数 还是最外层的函数？
// 这完全要看fun6到底是不是内联函数，这就让我们代码敲得很难受。
// ！！！为此，kotlin提出了一个新规定：！！！
// lambda表达式中不允许直接使用return，
// 除非是《内联函数的函数类型参数的lambda表达式》，《并且它结束的是最外层的函数逻辑》。
// 同时其他场景下的lambda表达式中可以《通过return@label的形式》来显示指定return结束的代码作用域

fun fun7(doSomething1: () -> Unit){
    Log.e("tag", "1")
    doSomething1()
}

fun mainFun7(){
    fun7{
        Log.e("tag", "2")
//        return //报错，直接return是不允许的，因为fun7不是inline内联函数
        return@fun7 //注意要紧密连接，中间不能有空格
    }
    Log.e("tag", "3")
}


//再看一种场景
inline fun fun8(doSomething1: () -> Unit){
    Log.e("tag", "1")
    Runnable{
//        doSomething1() //编译报错
    }
}

fun mainFun8(){
    fun8 {
        Log.e("tag", "2")
        return
    }
    Log.e("tag", "3")
}


// 我们在内联函数fun8中 将函数类型参数doSomething1放到了子线程里面去执行，
// 这样doSomething1 和 fun8就属于间接调用的关系，
// 那 mainFun8函数中的 return结束的 到底是Runnable子线程中的逻辑 还是 mainFun8函数中的逻辑呢？
// 所以kotlin是不允许直接这样写的，但是我确实有需求要这样在内联函数中间接调用函数类型的参数怎么办？
// kotlin为此又新增了一条规定：《内联函数中不允许类似上述问题中对函数类型参数的间接调用》，《除非》该函数类型参数被crossinline关键字修饰：（这就是局部加强内联的含义）


inline fun fun9(crossinline doSomething1: () -> Unit){
    Log.e("tag", "1")
    Runnable {
        doSomething1() //不报错了
    }
}

fun mainFun9(){
    fun9{
        Log.e("tag","2")
//        return //这里会编译报错
    }
    Log.e("tag", "3")
}

// crossinline可以达成《间接调用函数类型参数》的目的，
// 但是并没有解决《mainFun9函数中的return结束的 到底是Runnable子线程中的逻辑 还是mainFun9函数中的逻辑呢？》这一严重问题
// 于是kotlin干脆在间接调用的情况下内联函数的函数类型参数的lambda表达式《不允许直接使用return》，即下面这两个规定《不可以共存》：
//
// 1.lambda表达式中不允许直接使用return，除非是《内联函数的函数类型参数的lambda表达式》，《并且它结束的是最外层》的函数逻辑
//
// 2.内联函数中不允许类似上述问题中《对函数类型参数的间接调用》，除非该函数类型参数被crossinline关键字修饰
//
// 但是这里仍然可以用《return@label》的形式来显示指定return结束的代码作用域：

inline fun fun10(crossinline doSomething1: () -> Unit){
    Log.e("tag","1")
    Runnable {
        doSomething1()
    }
}

fun mainFun10(){
    fun10 {
        Log.e("tag", "2")
//        return@fun10
        return@fun10
    }
    Log.e("tag", "3")
}

















