import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

/**
 * Created by 张洲徽 on 2019/1/8.
 * 直接一个网页生成一个文件
 * 依然会内存溢出
 * 设置了不使用缓存，setChunkedStreamingMode，占用内存反而更多
 * 设置了多线程的缓冲队列值，减少了内存占用
 */
public class getOriginalURL {
    public static int page = 6570;
    static ThreadPoolExecutor threadPool;
    private static boolean flag=true;
    private static boolean writeFlag=false;

    public getOriginalURL() throws Exception {
        threadPool = new ThreadPoolExecutor(
                0, 40,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10),
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
        getOriginalURL getOriginalURL=new getOriginalURL();
        getOriginalURL.start();
        threadPool.shutdown();
        while (true) {
            if (threadPool.isTerminated()&!flag&&!writeFlag) {
                System.out.println("执行完所有任务");
                System.exit(0);
            }
        }
    }

    public void start(){
        while(flag){
            Runnable handler=new getURLHandler();
            threadPool.execute(handler);
        }
        System.out.println("执行完start");
    }

    class getURLHandler implements Runnable{
        public void run() {
            writeFlag=true;
            int page=getPage();
            //判断是否新链接，有则获取
            if(page>0){
                //获取url进行处理
                String strurl="http://search.chinalaw.gov.cn/SearchLawTitle?effectLevel=&SiteID=124&Query=%25%25&Sort=PublishTime&Type=1&PageIndex="+page;
                //调用workurl方法爬取
                try {
                    workurl(strurl,page);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                flag=false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void workurl(String strurl,int page) throws Exception {
        //创建文件夹和文件，用于保存链接
        File file = new File("C:\\Users\\热带雨林\\Desktop\\page"+ File.separator+page+".html");
        File parent = file.getParentFile();
        if(!parent.exists()){
            parent.mkdirs();
        }
        if(!file.exists()){
            file.createNewFile();
            System.out.println("文件创建完毕");
        }else {
            System.out.println("文件已经存在");
        }

        //创建输出流
        FileOutputStream fos= new FileOutputStream(file);
        OutputStreamWriter osw= new OutputStreamWriter(fos, "UTF-8");
        PrintWriter pw= new PrintWriter(osw);

        URL url=new URL(strurl);
        HttpURLConnection conn=null;
        //通过url建立与网页的连接
        for(int j=0;j<3;j++){
            try{
                conn=(HttpURLConnection) url.openConnection();
            }catch(Exception e){
                continue;
            }
        }
        if(conn==null){
            throw new Exception("获取连接失败");
        }
//        conn.setChunkedStreamingMode(0);
//        conn.setDoInput(true);
//        conn.setDoOutput(true);

        //通过链接取得网页返回的数据
        InputStream is=conn.getInputStream();
        BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));

        Thread t = Thread.currentThread();
        String name = t.getName();

//        for(int i=0;i<100;i++){
//            pw.println("dasdsadasdsadsadasdsadasdsadsadsadad");
//            pw.flush();
//        }
        //按行读取并打印
        String line = null;
        System.out.println(name+"开始写入："+page);
        while((line=br.readLine())!=null){
            //System.out.println(line);
//            pw.println(line);
//            pw.flush();
        }
        System.out.println(name+"写入结束："+page);

        conn.disconnect();
        br.close();
        pw.close();
        //写在整个方法最后，才能回收更多的内存
        System.gc();
        writeFlag=false;
    }
}