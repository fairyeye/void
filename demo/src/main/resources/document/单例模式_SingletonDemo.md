
#### 1. 最简单的单例模式：

```
package com.example.demo.JUC.thread;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/9/17 18:28
 */
public class SingletomDemo {

    private static SingletomDemo singletomDemo = null;
    
    private SingletomDemo() {
        System.out.println(Thread.currentThread().getName() + "\t 我是构造方法SingletomDemo()！");
    }

    private static SingletomDemo getInstance() {
        if (singletomDemo == null) {
        		singletomDemo = new SingletomDemo();
        }
        return singletomDemo;
    }
    
    public static void main(String[] args) {
        // 单线程
        System.out.println(SingletomDemo.getInstance() == SingletomDemo.getInstance());
        System.out.println(SingletomDemo.getInstance() == SingletomDemo.getInstance());
        System.out.println(SingletomDemo.getInstance() == SingletomDemo.getInstance());
    }
}

```

在单线程的情况下，打印结果如下：

![image-20200917191550283](https://i.loli.net/2020/09/17/J9rb5oynQclfD4d.png)

可以看到，虽然我们一共调用了六次`getInstance()`， 但是只打印了一次构造方法输出内容，也就是只调用了一个构造函数，所获得的对象地址自然是一样的。

#### 2. 多线程下的单例模式

我们对`main()`方法做一下改造，改造后的代码如下：

```
    public static void main(String[] args) {
        // 改为多线程后 可能多次调用构造函数
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                SingletomDemo.getInstance();
            }, String.valueOf(i)).start();
        }
    }
```

打印结果为：

![image-20200917192220270](https://i.loli.net/2020/09/17/Q7RWYjayOgoXfH1.png)

多次执行下可以看到打印的次数是不同的。

可以对`getInstance()`方法添加`synchronized`加锁，保证只生成一个实例。

```
    private static synchronized SingletomDemo getInstance() {
        if (singletomDemo == null) {
        		singletomDemo = new SingletomDemo();
        }
        return singletomDemo;
    }
```

再次执行程序发现只打印了一次构造方法输出函数，还有另外一种方法就是`DCL：double check locks双端检测模式`模式也可以达到同样的目的。

#### 3.DCL + 单例模式

我们再次对`getInstance()`方法进行改造，代码如下：

```
    // DCL模式(double check locks双端检测模式)
    private static SingletomDemo getInstance() {
        if (singletomDemo == null) {
            synchronized (SingletomDemo.class) {
                if (singletomDemo == null) {
                    singletomDemo = new SingletomDemo();
                }
            }
        }
        return singletomDemo;
    }
```

当我们的单例模式写到这种程度的时候，基本可以应对99%的情况，但是由于`指令排序`的存在，还是有可能会出现问题。

```
memory = allocate(); //1.分配对象内存空间
instance(memory);    //2.初始化对象
instance = memory;   //3.设置instance指向刚分配的内存地址，此时instance!=null
```

步骤2、3不存在数据依赖，所以由于指令重排的关系，可能会出现：

```
memory = allocate(); //1.分配对象内存空间
instance = memory;   //3.设置instance指向刚分配的内存地址，此时instance!=null
instance(memory);    //2.初始化对象
```



#### 4.Volatile + 单例模式

我们在声明 `singletomDemo`时，加上`Volatile`关键字修饰，就可以达到完美的效果。

最终代码：

```
package com.example.demo.JUC.thread;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/9/17 18:28
 */
public class SingletomDemo {

    private static volatile SingletomDemo singletomDemo = null;

    private SingletomDemo() {
        System.out.println(Thread.currentThread().getName() + "\t 我是构造方法SingletomDemo()！");
    }

    // DCL模式(double check locks双端检测模式)
    private static SingletomDemo getInstance() {
        if (singletomDemo == null) {
            synchronized (SingletomDemo.class) {
                if (singletomDemo == null) {
                    singletomDemo = new SingletomDemo();
                }
            }
        }
        return singletomDemo;
    }

    public static void main(String[] args) {
        // 单线程
//        System.out.println(SingletomDemo.getInstance() == SingletomDemo.getInstance());
//        System.out.println(SingletomDemo.getInstance() == SingletomDemo.getInstance());
//        System.out.println(SingletomDemo.getInstance() == SingletomDemo.getInstance());

//         改为多线程后 可能多次调用构造函数
//         可以在 getInstance上加SYNC解决问题
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                SingletomDemo.getInstance();
            }, String.valueOf(i)).start();
        }
    }

}

```

