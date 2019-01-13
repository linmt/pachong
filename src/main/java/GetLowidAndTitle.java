import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by 热带雨林 on 2019/1/13.
 */
public class GetLowidAndTitle {
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

    public static void main(String[] args) throws IOException {
        //生成HTML的路径
        for(int i=1;i<=1;i++){
            String msg=getLowidAndTitle(i);
            //将该页标记为插入成功
//            HandleData.updateGetLowidStatus(i,msg);
        }
    }

    public static String getLowidAndTitle(Integer page) throws IOException {
        String path="C:\\Users\\热带雨林\\Desktop\\page\\"+page+".html";
        Document doc = Jsoup.parse(readHtml(path));

        String msg=null;
        //获取lowID
        Elements elements = doc.select(".listLef");
        try {
            for (Element e : elements) {
                String url=e.select("a").attr("href");
                Integer lowid=Integer.valueOf(url.substring(url.indexOf("LawID=",0)+6,url.indexOf("&Query",0)));
                String title=e.select("a").text();
                String info=e.select(".searInfo").text();
                String publishDepartment=info.substring(info.indexOf("发布部门: ")+5,info.indexOf("发布时间:")).replace(" ","");
                String publishTime=info.substring(info.indexOf("发布时间:")+5);
                String tiZhu=e.select(".searContent").text();
                tiZhu=tiZhu.substring(tiZhu.indexOf("题注：")+3);
                System.out.println(tiZhu);
                //插入数据库
//                HandleData.insertLowid(page,lowid,title,publishDepartment,publishTime,tiZhu);
//                System.out.println("成功插入："+lowid);

                msg="插入成功";
            }
        }catch (Exception e){
            msg="插入失败";
            //清除该lowID插入的内容
            HandleData.deleteLowid(page);
            e.getStackTrace();
        }finally {
            return msg;
        }
    }
}
