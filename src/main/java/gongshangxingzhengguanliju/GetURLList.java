package gongshangxingzhengguanliju;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 张洲徽 on 2019/1/14.
 */
public class GetURLList {
    //将列表页下载下来
    //获取链接
    //根据链接下载网页
    //http://www.gzedu.gov.cn/gzsjyj/zcfg/list.shtml
    public static void main(String args[]) throws Exception {
        String url;
        for(int i=1;i<=3;i++){
            if(i==1){
                url="http://www.gzaic.gov.cn/gzaic/zcfg_dfxfg/common_list.shtml";
            }else {
                url="http://www.gzaic.gov.cn/gzaic/zcfg_dfxfg/common_list_"+i+".shtml";
            }
            workurl(url,i);
            System.out.println("下载完第"+i+"页");
        }
    }

    public static void workurl(String strurl,Integer page) throws Exception {
        File file = new File("C:\\Users\\张洲徽\\Desktop\\gongshangxingzhengguanliju_page"+ File.separator+page+".html");
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
            e.printStackTrace();
        }finally {
            conn.disconnect();
            br.close();
            pw.close();
            System.gc();
        }
    }
}
