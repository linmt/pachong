import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 热带雨林 on 2019/1/13.
 */
public class TestStopThread {
    static ThreadPoolExecutor threadPool;
    private static boolean flag=true;
    public static int page = 9;

    public TestStopThread() {
        threadPool = new ThreadPoolExecutor(
                0, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(2),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public synchronized static int getPage(){
        if(page==0){
            return 0;
        }
        return page--;
    }

    public static void main(String args[]) throws Exception {
        TestStopThread testStopThread=new TestStopThread();
        testStopThread.start();

        System.out.println("准备执行shutdown");
        //这一句是否合适？
        threadPool.shutdown();
        while (true) {
            if (threadPool.isTerminated()&!flag) {
                System.out.println("执行完所有任务");
                System.exit(0);
            }
        }
        /*
        开始执行9
        开始执行8
        开始执行7
        结束执行9
        开始执行6
        结束执行7
        开始执行5
        结束执行8
        开始执行4
        结束执行4
        结束执行5
        结束执行6
        开始执行3
        开始执行2
        开始执行1
        结束执行3
        执行到最后一个任务
        执行完start
        准备执行shutdown
        结束执行2
        执行到最后一个任务
        执行到最后一个任务
        结束执行1
        执行完所有任务
         */
    }

    public void start(){
        while(flag){
            Runnable handler=new TestStopThread.MyHandler();
            threadPool.execute(handler);
        }
        System.out.println("执行完start");
    }

    class MyHandler implements Runnable{
        public void run() {
            int page=getPage();
            //判断是否新链接，有则获取
            if(page>0){
                System.out.println("开始执行"+page);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("结束执行"+page);
            }else{
                System.out.println("执行到最后一个任务");
                flag=false;
                //threadPool.shutdown();是不是写在这里更好一些？
            }
        }
    }
}
