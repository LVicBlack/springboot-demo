package com.websocket.sockjs.demo.domain;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionWithThread {

    private int counter;
    private Lock lock = new ReentrantLock();
    private Condition condition=lock.newCondition();
    private Condition condition2=lock.newCondition();

    class Await2Task extends Thread {
        @Override
        public void run() {
            for(int i=0;i<10;i++){
                lock.lock();
//                System.out.println("Lock by:"
//                                + Thread.currentThread().getName() + " and "
//                                + ((ReentrantLock2)lock).getQueuedThreads()
//                                + " waits.");
                System.out.println("获得锁的线程："+Thread.currentThread().getName());
                lock.unlock();
            }
        }
    }

    class AwaitTask extends Thread {
        @Override
        public void run() {
            lock.lock();
            counter++;
            int num = counter;
            try {
//                System.out.println("------暂停:"+num+"------");
                condition.await();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            System.out.println("------finish:"+num+"------");
        }
    }

    class SignalTask extends Thread {
        @Override
        public void run() {
            lock.lock();
//            System.out.println("------恢复------");
            condition.signal();
            lock.unlock();
        }
    }

    class SignalAllTask extends Thread {
        @Override
        public void run() {
            lock.lock();
            System.out.println("------恢复全部------");
            condition.signalAll();
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ConditionWithThread conditionWithThread = new ConditionWithThread();
        for(int i=1;i<50;i++){
            conditionWithThread.lock.lock();
            executorService.execute(conditionWithThread.new AwaitTask());
            conditionWithThread.lock.unlock();
        }
//        executorService.execute(conditionWithThread.new SignalAllTask());
        for(int i=1;i<100;i++){
            executorService.execute(conditionWithThread.new SignalTask());
        }
    }
}
