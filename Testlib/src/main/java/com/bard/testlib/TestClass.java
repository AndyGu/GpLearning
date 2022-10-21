package com.bard.testlib;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestClass {
    public static void main(String[] args){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("123--"+System.currentTimeMillis());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        final TestSynchronized x = new TestSynchronized();
        final TestSynchronized y = new TestSynchronized();


        /** *
         * 同一个实例对象x，在不同的线程，执行两个不同的 非静态synchronized方法（锁对象）
         *
         * x.isSyncA()与x.isSyncB()
         *
         * 结论：
         * 对象锁有约束（条件：同一个对象，不同的 带对象锁的 方法，【该实例的所有synchronized块】）
         * 同一个对象，在不同的线程里，对于所有的该对象的加锁方法，都是同步执行，不会交叉执行
         *
         * isSyncA-test1 : 4
         * isSyncA-test1 : 3
         * isSyncA-test1 : 2
         * isSyncA-test1 : 1
         * isSyncA-test1 : 0
         * isSyncB-test2 : 4
         * isSyncB-test2 : 3
         * isSyncB-test2 : 2
         * isSyncB-test2 : 1
         * isSyncB-test2 : 0
         * **/
//        Thread test1 = new Thread(new Runnable() {
//            public void run() {
//                x.isSyncA();
//            }
//        }, "test1");
//
//        Thread test2 = new Thread(new Runnable() {
//            public void run() {
//                x.isSyncB();
//            }
//        }, "test2");



        /**
         *
         * 不同的实例对象x、y，调用同样的 非静态synchronized方法（锁对象）
         *
         * x.isSyncA() 与 y.isSyncA()
         *
         * 结论
         * 对象锁没有约束（条件：不同的对象，相同的 带对象锁的 方法）
         * 对象锁只对同一实例对象有效，不同的对象不受其约束，两个对象各执行各的
         *
         * isSyncA-test1 : 4
         * isSyncA-test2 : 4
         * isSyncA-test1 : 3
         * isSyncA-test2 : 3
         * isSyncA-test2 : 2
         * isSyncA-test1 : 2
         * isSyncA-test1 : 1
         * isSyncA-test2 : 1
         * isSyncA-test1 : 0
         * isSyncA-test2 : 0
         * **/
//        Thread test1 = new Thread(new Runnable() {
//            public void run() {
//                x.isSyncA();
//            }
//        }, "test1");
//        Thread test2 = new Thread(new Runnable() {
//            public void run() {
//                y.isSyncA();
//            }
//        }, "test2");


        /**
         * 不同的实例对象x、y，分别调用 两个不同的static synchronized方法（锁类对象）
         *
         * x.staticSyncA() 与 y.staticSyncB()
         *
         * 结论：类锁具有约束（条件：不同的 带类锁的 方法, 因为静态方法也不分是 类.方法 还是 类对象.方法 了，执行效果一样）
         * 类锁 对该类的所有对象都加了锁
         *
         * staticSyncA-test1 : 4
         * staticSyncA-test1 : 3
         * staticSyncA-test1 : 2
         * staticSyncA-test1 : 1
         * staticSyncA-test1 : 0
         * staticSyncB-test2 : 4
         * staticSyncB-test2 : 3
         * staticSyncB-test2 : 2
         * staticSyncB-test2 : 1
         * staticSyncB-test2 : 0
         *
         * **/
//        Thread test1 = new Thread(new Runnable() {
//            public void run() {
//                x.staticSyncA();
////                TestSynchronized.staticSyncA();
//            }
//        }, "test1");
//        Thread test2 = new Thread(new Runnable() {
//            public void run() {
//                y.staticSyncB();
//            }
//        }, "test2");



        /**
         * 不同的实例对象x、y，相同的static synchronized类锁 方法，
         *
         * x.staticSyncA() 与 y.staticSyncA()
         *
         * 类锁具有约束（条件：相同的 带类锁的 方法）
         * 类锁 对该类的所有对象都加了锁，何况静态方法，不同对象对静态方法来说没区别，类.方法 都能调出来
         *
         * static synchronized 相当于内存中只有一份 对调用 都是具有约束的
         *
         * staticSyncA-test1 : 4
         * staticSyncA-test1 : 3
         * staticSyncA-test1 : 2
         * staticSyncA-test1 : 1
         * staticSyncA-test1 : 0
         * staticSyncA-test2 : 4
         * staticSyncA-test2 : 3
         * staticSyncA-test2 : 2
         * staticSyncA-test2 : 1
         * staticSyncA-test2 : 0
         *
         * **/
//        Thread test1 = new Thread(new Runnable() {
//            public void run() {
//                x.staticSyncA();
//            }
//        }, "test1");
//        Thread test2 = new Thread(new Runnable() {
//            public void run() {
//                y.staticSyncA();
//            }
//        }, "test2");


        /**
         * 不同的实例x、y，对于对象锁 和 类锁
         *
         * x.isSyncA() 和 y.innerBlockSyncA()
         *
         * innerBlockSyncA 与 isSyncA 是等价的，都是对象锁
         *
         * 对象锁和类锁互不影响
         *
         * isSyncA-test1 : 4
         * innerBlockSyncA-test2 : 4
         * isSyncA-test1 : 3
         * innerBlockSyncA-test2 : 3
         * innerBlockSyncA-test2 : 2
         * isSyncA-test1 : 2
         * isSyncA-test1 : 1
         * innerBlockSyncA-test2 : 1
         * innerBlockSyncA-test2 : 0
         * isSyncA-test1 : 0
         * **/
//        Thread test1 = new Thread(new Runnable() {
//            public void run() {
//                x.isSyncA();
//            }
//        }, "test1");
//        Thread test2 = new Thread(new Runnable() {
//            public void run() {
////                x.innerBlockSyncA(); //有约束
//                y.innerBlockSyncA(); //无约束
//            }
//        }, "test2");


        /**
         * 相同的实例x，对于对象锁 和 类锁，不会相互阻塞
         *
         * x.isSyncA() 和 x.staticSyncA()
         *
         * 由于类对象和实例对象分别拥有自己的监视器锁，因此不会相互阻塞
         *
         * staticSyncA-test2 : 4
         * isSyncA-test1 : 4
         * isSyncA-test1 : 3
         * staticSyncA-test2 : 3
         * staticSyncA-test2 : 2
         * isSyncA-test1 : 2
         * staticSyncA-test2 : 1
         * isSyncA-test1 : 1
         * staticSyncA-test2 : 0
         * isSyncA-test1 : 0
         * **/
        Thread test1 = new Thread(new Runnable() {
            public void run() {
                x.staticSyncA();
            }
        }, "test1");
        Thread test2 = new Thread(new Runnable() {
            public void run() {
                x.isSyncA(); //无约束

                notifyAll();
            }
        }, "test2");

        test1.start();
        test2.start();

        PriorityBlockingQueue
        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue() ;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
            10,
            1000,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
            new RejectedExecutionHandler(){
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

                }
            });

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        /**
         *
         * 总结
         * 对象锁（两种形式：synchronized修饰方法或代码块）
         *
         *      1.当一个对象中有synchronized method 或synchronized block 的时候
         *      调用此对象的同步方法或进入其同步区域时，就必须先获得对象锁。
         *      如果此对象的对象锁已被其他调用者占用，则需要等待此锁被释放。
         *      2.方法锁也是对象锁，相当于是方法中添加了 synchronized(this)
         *      3.记住重点是 针对同一个类对象
         *
         *      java的所有对象都含有一个互斥锁，这个锁由jvm自动获取和释放。
         *      线程进入synchronized 方法的时候获取该对象的锁，当然如果已经有线程获取了这个对象的锁，那么当前线程会等待；
         *
         *      synchronized方法正常返回或者抛异常而终止，jvm会自动释放对象锁。这里也体现了用synchronized来加锁的一个好处，即 ：
         *      方法抛异常的时候，锁仍然可以由jvm来自动释放
         *
         *
         * 类锁（synchronized修饰静态的方法或者代码块）
         *
         *      由于一个class不论被实例化多少次，其中的静态方法和静态变量在内存中都只有一份。
         *      所以，一旦一个静态static的方法被声明为synchronized。
         *      此类所有的实例对象在调用此方法，共用同一把锁，我们称之为类锁。
         *
         *
         *
         * 对象锁是用来控制实例方法之间的同步，【一般用于同一个类对象，调用不同的(或者同一个)同步方法的时候】
         * 类锁是用来控制静态方法（或者静态变量互斥体）之间的同步的。【一般用于不同的类对象，调用相同的或不同的 静态类锁方法】
         *
         * 类锁只是一个概念上的东西，并不是真实存在的，他只是用来帮助我们理解锁定实例方法和静态方法的区别的。
         * java类可能会有很多对象，但是只有一个Class(字节码)对象，也就是说类的不同实例之间共享该类的Class对象。
         * Class对象其实也仅仅是1个java对象，只不过有点特殊而已。
         * 由于每个java对象都有1个互斥锁，而类的静态方法是需要Class对象。
         * 所以所谓的类锁，只不过是Class对象的锁而已。
         * 获取类的Class对象的方法有好几种，最简单的是[类名.class]的方式。(百度：获取字节码的三种方式)
         *
         */
    }
}



