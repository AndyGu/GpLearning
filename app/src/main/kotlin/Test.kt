package com.bard.gplearning.lifecycle

import kotlinx.coroutines.*

//类型后面加?表示可为空
var age: String? = "23"
//抛出空指针异常
val ages = age!!.toInt()
//不做处理返回 null
val ages1 = age?.toInt()
//age为空返回-1
val ages2 = age?.toInt() ?: -1

fun main() = runBlocking{
    try {
        coroutineScope {
            launch { // “1”
                println("a")
            }
            launch {// “2”
                println("b")
                launch {// “3”
                    delay(1000)
                    println("c")
                    throw ArithmeticException("Hey!!")
                }
            }
            val job = launch {// “4”
                println("d")
                delay(2000)
                println("e")
            }
            job.join()
            println("f")
        }

    } catch (e: Exception) {
        println("g")
    }
    println("h")




    try {
        supervisorScope{
            launch { // “1”
                println("a1")
            }
            launch {// “2”
                println("b1")
                launch {// “3”
                    delay(1000)
                    println("c1")
                    throw ArithmeticException("Hey1!!")
                }
            }
            val job = launch {// “4”
                println("d1")
                delay(2000)
                println("e1")
            }
            job.join()
            println("f1")
        }

    } catch (e: Exception) {
        println("g1")
    }
    println("h1")


    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("${coroutineContext[CoroutineName]} $throwable")
    }

    runBlocking {
        try {
            supervisorScope {
                launch {
                    // "1"
                    println("a2")
                }
                launch(exceptionHandler + CoroutineName("\"2\"")) {
                    // "2"
                    println("b2")
                    launch(exceptionHandler + CoroutineName("\"3\"")) {
                        //"3"
                        launch (exceptionHandler + CoroutineName("\"5\"")){// "5"
                            delay(1000)
                            println("c2-")
                        }
                        println("c2")
                        throw ArithmeticException("Hey2!!")
                    }
                }
                val job = launch {
                    //"4"
                    println("d2")
                    delay(2000)
                    println("e2")
                }
                job.join()
                println("f2")
            }

        } catch (e: Exception) {
            println("g2")
        }
        println("h2")
    }





    val myJob = GlobalScope.launch {
        delay(3000)

        println("Kotlin Coroutines")
    }


    delay(2000)

    println("Hello")


//    delay(1500)
//    Thread.sleep(1500)

    myJob.join()

    println("world")

    repeat(20){
        launch {
            delay(123)
            println("123")
        }
    }

//    repeat(100, action = {
//        launch {
//            delay(123)
//            println("123")
//        }
//    })

    fun abc(x:Int, y:Int){
        println("$x - $y")
    }

    test5(4) { x, y -> abc(x, y) }

    test5(4) { x, y ->
        run {
            println("$x - $y")
        }
    }

    test5(5) { x, y ->
        run {
            println("$x - $y")
        }
    }

    var value = getNumResult{ a, b -> a + b }
}

fun test5(x:Int, action: (Int, Int) -> Unit){
    action(1 * x, 2 * x);
}


fun getNumResult(result: (Int, Int) -> Int): Int {
    return result(1,2)
}

////调用
//var value = getNumResult{ a, b -> a + b }
//==> value = 3
//
//var value = getNumResult{ a, b -> a * b }
//==> value = 2

//fun main1(){
//    GlobalScope.launch {
//
//    }
//}

