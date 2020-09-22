package com.example.demo.JUC.thread;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/9/17 18:28
 */
public class SingletonDemo {

    private static volatile SingletonDemo instance = null;

    private SingletonDemo() {
        System.out.println(Thread.currentThread().getName() + "\t 我是构造方法SingletomDemo()！");
    }

    // DCL模式(double check locks双端检测模式)
    private static SingletonDemo getInstance() {
        if (instance == null) {
            synchronized (SingletonDemo.class) {
                if (instance == null) {
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        // 单线程
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());

//         改为多线程后 可能多次调用构造函数
//         可以在 getInstance上加SYNC解决问题
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                SingletonDemo.getInstance();
            }, String.valueOf(i)).start();
        }
        long end = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "\t time is :" + (end - start));
    }

}
