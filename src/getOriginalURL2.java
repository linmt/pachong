import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 热带雨林 on 2019/1/9.
 * 内存溢出，没有添加回收
 */
public class getOriginalURL2 {
    public static int page = 6570;
    static PrintWriter pw;
    private static Object obj=new Object();
    static ExecutorService threadPool;
    private static boolean flag=true;
    private static boolean writeFlag=false;
    //<h3 style="width: 100%;"><a href="law/searchTitleDetail?LawID=404785&Query=%25%25&IsExact=" target="_blank">行政区划管理条例</a></h3>
    //<title>[\s\S]*?</title>
    Pattern p = Pattern.compile("<h3 style=\"width: 100%;\"><a href=\"[\\s\\S]*?</a></h3>");

    public getOriginalURL2() throws Exception {
        //创建文件夹和文件，用于保存链接
        File file = new File("a"+ File.separator+"c.html");
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
        pw= new PrintWriter(osw);

        threadPool= Executors.newFixedThreadPool(5);
    }

    public synchronized static int getPage(){
        if(page==0){
            return 0;
        }
        return page--;
    }

    public static void main(String args[]) throws Exception {
        getOriginalURL2 getOriginalURL=new getOriginalURL2();
        getOriginalURL.start();
        threadPool.shutdown();
        while (true) {
            if (threadPool.isTerminated()&!flag&&!writeFlag) {
                pw.close();
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
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void workurl(String strurl,int page) throws Exception {
        URL url=new URL(strurl);
        URLConnection conn=null;
        //通过url建立与网页的连接
        for(int j=0;j<3;j++){
            try{
                conn=url.openConnection();
            }catch(Exception e){
                continue;
            }
        }
        if(conn==null){
            throw new Exception("获取连接失败");
        }
        //通过链接取得网页返回的数据
        InputStream is=conn.getInputStream();

        //获取连接编码，无效
        //System.out.println("获取连接编码"+conn.getContentEncoding());

        //一般按行读取网页数据，并进行内容分析，因此用BufferedReader和InputStreamReader把字节流转化为字符流的缓冲流
        //进行转换时，需要处理编码格式问题
        BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));

        Thread t = Thread.currentThread();
        String name = t.getName();

        //按行读取并打印
        String line = new String();
        synchronized(this){
            System.out.println(name+"开始写入："+page);
//            pw.println("***********************************************");
//            pw.println(name+"：：：：：："+page);
            StringBuffer content=null;
            while((line=br.readLine())!=null){
                System.out.println("输出line"+line);
                if(!"".equals(line)){
                    content.append(line);
                }
            }

            //匹配
            Matcher m = p.matcher(content);
            List<StringBuffer> lists=new ArrayList<>();
            // 通过Matcher类的group方法和find方法来进行查找和匹配
            while (m.find()) {
                StringBuffer value = new StringBuffer(m.group());
                value.replace(0,value.length(),value.substring(value.indexOf("LawID=",0)+6,value.indexOf("&Query",0)));
                System.out.println(value);
                lists.add(value);
            }

            content.delete(0, content.length());

            for(StringBuffer s:lists){
                //System.out.println(s);
                pw.println(s);
            }
            pw.flush();
            lists.clear();
//            pw.println("***********************************************");
            System.out.println(name+"写入结束："+page);
            br.close();
        }
        writeFlag=false;
    }
}