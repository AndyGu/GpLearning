package com.bard.gplearning.coroutine

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Test {

    fun main() {
        runBlocking {
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
        }
    }
}