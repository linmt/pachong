import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 张洲徽 on 2019/1/8.
 * 直接一个网页生成一个文件
 * 依然会内存溢出
 * 设置了不使用缓存，setChunkedStreamingMode，占用内存反而更多
 * 设置了多线程的缓冲队列值，减少了内存占用
 * 增加断点续传功能
 */
public class getOriginalURL_oracle {
    static ThreadPoolExecutor threadPool;
    private static boolean flag=true;
    private static boolean writeFlag=false;
    static List<Integer> pages=new ArrayList<Integer>();

    public getOriginalURL_oracle() throws Exception {
        threadPool = new ThreadPoolExecutor(
                0, 100,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(100),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        pages=HandleData.getPages();
        if(pages.isEmpty()){
            System.out.println("数据库中查不到需要下载的文件");
            System.exit(0);
        }
    }

    public synchronized static Integer getPage(){
        Integer page=null;
        try{
            page=pages.remove(0);
            System.out.println(page);
            return page;
        }catch (IndexOutOfBoundsException e1) {
            System.out.println("遍历结束");
            return -1;
        }catch (Exception e3){
            e3.printStackTrace();
            return -1;
        }
    }

    public static void main(String args[]) throws Exception {
        getOriginalURL_oracle getOriginalURL=new getOriginalURL_oracle();
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
            //有没有必要？
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void workurl(String strurl,Integer page) throws Exception {
        HandleData.updateStatus(page,"下载中");
        File file = new File("C:\\Users\\张洲徽\\Desktop\\page"+ File.separator+page+".html");
        File parent = file.getParentFile();

        FileOutputStream fos=null;
        OutputStreamWriter osw=null;
        PrintWriter pw=null;

        URL url=null;
        HttpURLConnection conn=null;

        InputStream is=null;
        BufferedReader br=null;

        Thread t = Thread.currentThread();
        String name = t.getName();

        try {
            //创建文件夹和文件，用于保存链接
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
            fos= new FileOutputStream(file);
            osw= new OutputStreamWriter(fos, "UTF-8");
            pw= new PrintWriter(osw);

            url=new URL(strurl);
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

            //通过链接取得网页返回的数据
            is=conn.getInputStream();
            br=new BufferedReader(new InputStreamReader(is,"UTF-8"));

            //按行读取并打印
            String line = null;
            //System.out.println(name+"开始写入："+page);
            while((line=br.readLine())!=null){
                //System.out.println(line);
                pw.println(line);
                pw.flush();
            }
            //System.out.println(name+"写入结束："+page);
        }catch (Exception e){
            HandleData.updateStatus(page,"下载失败");
            e.printStackTrace();
        }finally {
            conn.disconnect();
            br.close();
            pw.close();
            System.gc();
        }
        HandleData.updateStatus(page,"已完成");

        writeFlag=false;
    }
}