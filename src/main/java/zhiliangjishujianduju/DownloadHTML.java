package zhiliangjishujianduju;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 张洲徽 on 2019/1/14.
 */
public class DownloadHTML {
    //遍历page，获取URL
    //下载HTML
    public static void main(String[] args) throws IOException {
        //生成HTML的路径
        for(int i=1;i<=9;i++){
            String msg=getLowidAndTitle(i);
            System.out.println("第"+i+"页，"+msg);
            System.out.println("");
            System.out.println("");
            System.out.println("");
        }
    }

    public static String getLowidAndTitle(Integer page) throws IOException {
        String path="C:\\Users\\张洲徽\\Desktop\\zhiliangjishujianduju_page\\"+page+".html";
        Document doc = Jsoup.parse(readHtml(path));

        String msg=null;
        Elements elements =null;
        Elements eles = doc.select("div");
        for (Element e : eles) {
            if(e.attr("class").equals("cont_r_box")){
                elements=e.select("li");
            }
        }
        int count=0;
        try {
            for (Element e : elements) {
                count++;
//                System.out.println(e.text());
                //http://www.gzq.gov.cn/gzq/2.3/201809/362331915b584c649403706e9b2d5a91.shtml
                //../../gzq/2.3/201809/362331915b584c649403706e9b2d5a91.shtml
                String url=e.select("a").attr("href").replace("../../","http://www.gzq.gov.cn/");
                String title=e.select("a").attr("title");
//                System.out.println(url);
//                System.out.println(title);
                //下载HTML
                workurl(url,page+"-"+count+title);
                System.out.println(count);
            }
            msg="插入成功";
        }catch (Exception e){
            msg="插入失败";
            e.getStackTrace();
        }finally {
            return msg;
        }
    }

    //读html文件
    public static String readHtml(String fileName) throws IOException {
        FileInputStream fis = null;
        StringBuffer sb = new StringBuffer();
        try {
            fis = new FileInputStream(fileName);
            byte[] bytes = new byte[40960];
            while (-1 != fis.read(bytes)) {
                sb.append(new String(bytes));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void workurl(String strurl,String title) throws Exception {
        File file = new File("C:\\Users\\张洲徽\\Desktop\\zhiliangjishujianduju"+ File.separator+title+".html");
        File parent = file.getParentFile();

        FileOutputStream fos=null;
        OutputStreamWriter osw=null;
        PrintWriter pw=null;

        URL url=null;
        HttpURLConnection conn=null;

        InputStream is=null;
        BufferedReader br=null;

        try {
            //创建文件夹和文件，用于保存链接
            if(!parent.exists()){
                parent.mkdirs();
            }
            if(!file.exists()){
                file.createNewFile();
                //System.out.println("文件创建完毕");
            }else {
                System.out.println(title+"文件已经存在");
                System.out.println("+++++++++++++++++++++++++");
//                file = new File("C:\\Users\\张洲徽\\Desktop\\jiaoyuju_page"+ File.separator+title+".html——已经存在");
//                file.createNewFile();
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
            while((line=br.readLine())!=null){
                pw.println(line);
                pw.flush();
            }
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
