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
 */
public class DownloadHTML {
    //遍历page，获取URL
    //下载HTML
    public static void main(String[] args) throws IOException {
        //生成HTML的路径
        for(int i=1;i<=3;i++){
            String msg=getLowidAndTitle(i);
            System.out.println("第"+i+"页，"+msg);
            System.out.println("");
            System.out.println("");
            System.out.println("");
        }
    }

    public static String getLowidAndTitle(Integer page) throws IOException {
//        String path="C:\\Users\\张洲徽\\Desktop\\环境保护局_广东省环保法规_page\\"+page+".html";
        String path="C:\\Users\\张洲徽\\Desktop\\环境保护局_广州市环保法规_page\\"+page+".html";
        Document doc = Jsoup.parse(readHtml(path));

        String msg=null;
        Elements elements = doc.select("table");
        for (Element e : elements) {
            String width=e.attr("width");
            if(width.equals("96%")){
                int count=0;
                Elements trElements =e.select("tr");
                try {
                    for (Element ee : trElements) {
//                        String text=ee.text();
//                        System.out.println(text);

                        if(!ee.text().equals("")&&ee.text()!=null){
                            count++;
                            //广东省环保法规  1-14PDF
//                            if(!(page==1&&(count==14))){
                            if(true){
                                //http://www.gzepb.gov.cn/zwgk/fgybz/gdshbfg/201812/t20181228_91622.htm
                                //./201812/t20181228_91622.htm
                                String href=ee.select("a").attr("href");
                                if(href.contains(".pdf")){
                                    System.out.println(page+"-"+count+"PDF");
                                }else {
                                    //广东省环保法规
//                                    String url=ee.select("a").attr("href").replace("./","http://www.gzepb.gov.cn/zwgk/fgybz/gdshbfg/");
                                    //广州市环保法规
                                    //http://www.gzepb.gov.cn/zwgk/fgybz/gzshbfg/201812/t20181221_91540.htm
                                    String url=ee.select("a").attr("href").replace("./","http://www.gzepb.gov.cn/zwgk/fgybz/gzshbfg/");
                                    String title=ee.select("a").text();
//                            System.out.println(url);
//                            System.out.println(title);

                                    //下载HTML
                                workurl(url,page+"-"+count,"");
                                    System.out.println(count);
                                }
                            }
                        }
                    }
                    msg="插入成功";
                }catch (Exception e2){
                    msg="插入失败";
                    e2.getStackTrace();
                }
            }
        }
        return msg;
    }

    //读html文件
    public static String readHtml(String fileName) throws IOException {
        FileInputStream fis = null;
        StringBuffer sb = new StringBuffer();
        try {
            fis = new FileInputStream(fileName);
            byte[] bytes = new byte[60000];
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

    public static void workurl(String strurl,String prefix,String title) throws Exception {
//        File file = new File(
//                "C:\\Users\\张洲徽\\Desktop\\环境保护局_广东省环保法规"
//                        + File.separator+prefix+title+".html");
        File file = new File(
                "C:\\Users\\张洲徽\\Desktop\\环境保护局_广州市环保法规"
                        + File.separator+prefix+title+".html");
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
                for(int i=3;i>0;i--){
                    try{
                        file.createNewFile();
                        //System.out.println("文件创建完毕");
                    }catch (IOException ie){
//                        file = new File("C:\\Users\\张洲徽\\Desktop\\环境保护局_广东省环保法规"+ File.separator+prefix+".html");
                        file = new File("C:\\Users\\张洲徽\\Desktop\\环境保护局_广州市环保法规"+ File.separator+prefix+".html");
                        System.out.println("文件改名完毕");
                    }
                }
            }else {
                System.out.println(title+"文件已经存在");
                System.out.println("++++++++++++++++++++++++++++++");
//                file = new File("C:\\Users\\张洲徽\\Desktop\\huanjingbaohuju_page"+ File.separator+title+".html——已经存在");
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
                    System.out.println("连接失败，尝试重连"+j);
                    Thread.sleep(1000);
                    continue;
                }
            }
            if(conn==null){
                throw new Exception("获取连接失败");
            }
            //如果是服务器端禁止抓取,那么这个你可以通过设置User-Agent来欺骗服务器
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            for(int j=0;j<3;j++){
                try{
                    //通过链接取得网页返回的数据
                    is=conn.getInputStream();
                }catch(Exception e){
                    System.out.println("获取流失败，重试"+j);
                    Thread.sleep(1000);
                    continue;
                }
            }

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
