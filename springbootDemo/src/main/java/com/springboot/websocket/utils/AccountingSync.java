package com.sbfirstdemo.demo.utils;

import java.util.concurrent.CountDownLatch;

public class AccountingSync implements Runnable{
    //共享资源(临界资源)
    static int i=0;

    final static CountDownLatch countDownLatch = new CountDownLatch(2);
    /**
     * synchronized 修饰实例方法
     */
    public static synchronized void increase(){
        i++;
    }
    @Override
    public void run() {
        for(int j=0;j<100000;j++){
            System.out.println(Thread.currentThread().getName()+":::"+i);
            synchronized (AccountingSync.class){
                i++;
            }
//            increase();
        }
        countDownLatch.countDown();
    }
    public static void main(String[] args) throws InterruptedException {
        AccountingSync instance = new AccountingSync();
        Thread t1=new Thread(instance);
        Thread t2=new Thread(new AccountingSync());
        t1.start();
        t2.start();
//        t1.join();
//        t2.join();
        countDownLatch.await();
        System.out.println(i);
    }
}
