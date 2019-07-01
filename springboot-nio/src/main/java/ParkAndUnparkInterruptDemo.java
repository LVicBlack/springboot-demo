import java.util.concurrent.locks.LockSupport;

public class ParkAndUnparkInterruptDemo {

    class MyThread extends Thread {
        private Object object;

        public MyThread(Object object) {
            this.object = object;
        }

        public void run() {
            System.out.println("before unpark");
            // 释放许可
            LockSupport.unpark((Thread) object);
            System.out.println("after unpark");
        }
    }

    public static void main(String[] args) {
        MyThread myThread = new ParkAndUnparkInterruptDemo().new MyThread(Thread.currentThread());
        myThread.start();
        try {
            // 主线程睡眠3s
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("before park");
        // 获取许可
        /*LockSupport并不需要获取对象的监视器。
        LockSupport机制是每次unpark给线程1个“许可”——最多只能是1，
        而park则相反，如果当前 线程有许可，那么park方法会消耗1个并返回，
        否则会阻塞线程直到线程重新获得许可，在线程启动之前调用park/unpark方法没有任何效果。*/
        LockSupport.park("ParkAndUnparkInterruptDemo");
        System.out.println("after park");
    }
}