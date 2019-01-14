package huanjingbaohuju;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 张洲徽 on 2019/1/14.
 * 乱码
 */
public class DownloadHTML {
    //遍历page，获取URL
    //下载HTML
    public static void main(String[] args) throws IOException {
        //生成HTML的路径
        for(int i=1;i<=6;i++){
            String msg=getLowidAndTitle(i);
            System.out.println("第"+i+"页，"+msg);
            System.out.println("");
            System.out.println("");
            System.out.println("");
        }
    }

    public static String getLowidAndTitle(Integer page) throws IOException {
        //url网址作为输入源
        Document doc = Jsoup.connect("http://www.example.com").timeout(60000).get();
//File文件作为输入源
        File input = new File("/tmp/input.html");
        Document doc = Jsoup.parse(input, "UTF-8", "http://www.example.com/");
//String作为输入源
        Document doc = Jsoup.parse(htmlStr);



        String path="C:\\Users\\张洲徽\\Desktop\\huanjingbaohuju_page\\"+page+".html";
        //Document doc = Jsoup.parse(readHtml(path));
        //Jsoup.parse(InputStream in, String charsetName, String baseUri)
        Document document = Jsoup.parse(new URL(path).openStream(), "GB2312", url);
        File file = new File("C:\\Users\\张洲徽\\Desktop\\a"+ File.separator+page+".html");
        File parent = file.getParentFile();
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

        FileOutputStream fos=new FileOutputStream(file);
        OutputStreamWriter osw=new OutputStreamWriter(fos, "GB2312");
        PrintWriter pw=new PrintWriter(osw);

        String msg=null;
        Elements elements = doc.select("table");
        for (Element e : elements) {
            String width=e.attr("width");
            if(width.equals("96%")){
                int count=0;
                Elements trElements =e.select("tr");
                try {
                    for (Element ee : trElements) {
                        count++;
                        String text=ee.text();
//                        byte[] bbs = text.getBytes();
//                        text=new String(bbs, "GB2312");
                        System.out.println(text);

                        pw.println(text);
                        pw.flush();

                        //http://www.gzedu.gov.cn/gzsjyj/zcfg/201812/67ed9e57d21c4845b20c0598b7a0a0b9.shtml
                        String url=ee.select("a").attr("href").replace("../../","http://www.gzedu.gov.cn/");
                        String title=ee.select("a").text();
                        //String time=e.select("span").text();
                        //System.out.println(title+time);
                        //下载HTML
//                        workurl(url,page+"-"+count+title);
                        System.out.println(count);
                    }
                    msg="插入成功";
                }catch (Exception e2){
                    msg="插入失败";
                    e2.getStackTrace();
                }finally {
                    return msg;
                }
            }
        }

        return "插入成功";
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
        File file = new File("C:\\Users\\张洲徽\\Desktop\\huanjingbaohuju"+ File.separator+title+".html");
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
                System.out.println("++++++++++++++++++++++++++++");
//                file = new File("C:\\Users\\张洲徽\\Desktop\\jiaoyuju_page"+ File.separator+title+".html——已经存在");
//                file.createNewFile();
            }

            //创建输出流
            fos= new FileOutputStream(file);
            osw= new OutputStreamWriter(fos, "GB2312");
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
            br=new BufferedReader(new InputStreamReader(is,"GB2312"));

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
