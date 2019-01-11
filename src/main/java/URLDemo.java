import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 张洲徽 on 2019/1/8.
 * 网上的例子
 */
public class URLDemo {
    public static void main(String args[]) throws Exception {
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
        OutputStreamWriter osw=new OutputStreamWriter(fos,"UTF-8");
        PrintWriter pw= new PrintWriter(osw,true);

        //遍历所有链接
        String strurl=null;
        for(int i=1;i<6570;i++){
            strurl="http://search.chinalaw.gov.cn/SearchLawTitle?effectLevel=&SiteID=124&Query=%25%25&Sort=PublishTime&Type=1&PageIndex="+i;
            URL url=new URL(strurl);
            //通过url建立与网页的连接
            URLConnection conn=url.openConnection();
            //通过链接取得网页返回的数据
            InputStream is=conn.getInputStream();

            //获取连接编码，无效
            //System.out.println("获取连接编码"+conn.getContentEncoding());

            //一般按行读取网页数据，并进行内容分析，因此用BufferedReader和InputStreamReader把字节流转化为字符流的缓冲流
            //进行转换时，需要处理编码格式问题
            BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));

            //按行读取并打印
            String line=null;
            System.out.println("开始写入："+i);
            while((line=br.readLine())!=null){
                pw.println(line);
            }
            System.out.println("写入结束："+i);
            br.close();
        }
        pw.close();
    }
}
