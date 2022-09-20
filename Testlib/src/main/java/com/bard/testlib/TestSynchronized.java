package com.bard.testlib;

public class TestSynchronized {



    public void innerBlockSyncA() {
        synchronized(this) {
            int i = 5;
            while( i-- > 0){
                System.out.println("innerBlockSyncA-"+Thread.currentThread().getName() + " : " + i);
                try{
                    Thread.sleep(500);
                } catch (InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        }
    }

    public void innerBlockSyncB() {
        synchronized(this) {
            int i = 5;
            while( i-- > 0){
                System.out.println("innerBlockSyncB-"+Thread.currentThread().getName() + " : " + i);
                try{
                    Thread.sleep(500);
                } catch (InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        }
    }


    /**
     * 锁方法
     */
    public synchronized void isSyncA() {
        int i = 5;
        while( i-- > 0){
            System.out.println("isSyncA-"+Thread.currentThread().getName() + " : " + i);
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
    }


    /**
     * 锁方法
     */
    public synchronized void isSyncB(){
        int i = 5;
        while( i-- > 0){
            System.out.println("isSyncB-"+Thread.currentThread().getName() + " : " + i);
            try {
                Thread.sleep(500);
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
    }


    /**
     * 静态方法锁
     */
    public static synchronized void staticSyncA(){
        int i = 5;
        while( i-- > 0) {
            System.out.println("staticSyncA-"+Thread.currentThread().getName() + " : " + i);
            try{
                Thread.sleep(500);
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
    }

    /**
     * 静态方法锁
     */
    public static synchronized void staticSyncB() {
        int i = 5;
        while( i-- > 0) {
            System.out.println("staticSyncB-"+Thread.currentThread().getName() + " : " + i);
            try{
                Thread.sleep(500);
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
    }

}
