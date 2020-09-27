import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/9/1 18:18
 */
public class QiaPaoMian {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        qiaPaoMian();
        long end = System.currentTimeMillis();
        System.out.println("恰泡面一共花费了：" + (end - start) + "毫秒");
    }

    private static void qiaPaoMian() throws InterruptedException, ExecutionException {
        FutureTask<String> shaoKaishui = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                shaoKaishui();
                return "开水";
            }
        });
        FutureTask<String> paoMian = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                paoMian();
                return "泡面";
            }
        });
//        FutureTask<String> qia = new FutureTask<>(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                qia();
//                return "恰";
//            }
//        });
        Thread t = new Thread(shaoKaishui);
        t.start();
        Thread t1 = new Thread(paoMian);
        t1.start();
//        Thread t2 = new Thread(qia);
//        t2.start();

        shaoKaishui.get();
        paoMian.get();
//        qia.get();

        qia();

    }

    private static void qia() throws InterruptedException {
        System.out.println("恰饭。。。");
        Thread.sleep(200L);
    }

    private static void paoMian() throws InterruptedException {
        System.out.println("泡面。。。");
        Thread.sleep(200L);
    }

    private static void shaoKaishui() throws InterruptedException {
        System.out.println("先烧开水。。。");
        Thread.sleep(500L);
    }
}
