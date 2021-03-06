package cn.tommyyang.demo;

import cn.tommyyang.demo.lock.Consumer;
import cn.tommyyang.demo.lock.Producer;
import cn.tommyyang.demo.lock.ReentrantLockDemo;
import cn.tommyyang.demo.lock.SynchronizedDemo;
import volatilekey.unsafe.Counter;

import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author TommyYang on 2019/1/28
 */
public class RunTest {

    public static void main(String[] args) {

        //runSynchronizedDemo();

        //runReenTrantLockDemo();

        //runCASDemo();

        runSynchronizedP_C_Demo();

        //runReentrantLockP_C_Demo();
    }



    public static void runSynchronizedP_C_Demo(){
        Producer pro = new Producer();
        Consumer con = new Consumer();
        Thread t1 = new Thread(pro);
        Thread t2 = new Thread(pro);

        Thread c1 = new Thread(con);
        Thread c2 = new Thread(con);

        t1.start();
        t2.start();

        c1.start();
        c2.start();


    }

    public static void runReentrantLockP_C_Demo(){
        ReentrantLock lock = new ReentrantLock();
        Condition cCondition = lock.newCondition();
        Condition pCondition = lock.newCondition();
        Producer pro = new Producer(lock, cCondition, pCondition);
        Thread t1 = new Thread(pro);
        Thread t2 = new Thread(pro);

        Consumer con = new Consumer(lock, cCondition, pCondition);
        Thread c1 = new Thread(con);
        Thread c2 = new Thread(con);

        t1.start();
        t2.start();

        c1.start();
        c2.start();



    }

    public static void runSynchronizedDemo(){
        final ExecutorService pool = Executors.newFixedThreadPool(10);
        for(int i = 0; i < 2; i++){
            pool.execute(new SynchronizedDemo());
        }
        pool.shutdown();

        while (true){
            if(pool.isTerminated()){
                System.out.println(SynchronizedDemo.common);
                break;
            }
        }
    }

    public static void runReenTrantLockDemo(){
        ReentrantLock lock = new ReentrantLock(true);
        ReentrantLockDemo reentrantLockDemo = new ReentrantLockDemo();

        reentrantLockDemo.newThread(lock, "tttt1");
        reentrantLockDemo.newThread(lock, "tttt2");

        reentrantLockDemo.newThread(lock, "tttt3");
        reentrantLockDemo.newThread(lock, "tttt4");

    }

    public static void runCASDemo(){
        final Counter counter = new Counter(0);

        new Thread(new Runnable() {
            public void run() {
                counter.compareAndSet(0, 1);

                System.out.println("sleep 1s");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("重置value为0");
                counter.compareAndSet(1, 0);
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                System.out.println("等待中.....");
                counter.compareAndSet(0, 2);
                System.out.println(counter.getValue());
            }
        }).start();

    }
}
