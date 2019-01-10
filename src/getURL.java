import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 张洲徽 on 2019/1/8.
 * 本意是把6570页HTML拼成的大HTML加到内存中，读取其中的法律链接，然后根据法律链接爬取每个法律的总页数
 * 但是首先拼成一个大HTML太占内存，而且上一步没有完成，无法得到大HTML，所以该类作废
 */
public class getURL {
    public static void main(String[] args) throws Exception {
        Pattern p = Pattern.compile("<h3 style=\"width: 100%;\"><a href=\"[\\s\\S]*?</a></h3>");

        //读取文件
        File file = new File("a"+ File.separator+"c.html");
        FileInputStream is=new FileInputStream(file);
        BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
        //按行读取并打印
        String line=null;
        String content=null;
        System.out.println("开始读取");
        while((line=br.readLine())!=null){
            content+=line;
        }
        System.out.println("读取结束");
        br.close();
//        String content = "<div class=\"listCon clearself\">\n" +
//                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
//                "                              <tr>\n" +
//                "                                  <td class=\"listLef\">\n" +
//                "                                      <h3 style=\"width: 100%;\"><a href=\"law/searchTitleDetail?LawID=400287&Query=%25%25&IsExact=\" target=\"_blank\">最高人民法院关于行政申请再审案件立案程序的规定</a></h3>\n" +
//                "\t\t\t                          <div class=\"searInfo\">发布部门: 最高人民法院&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发布时间:  </div>\n" +
//                "\t\t\t                          <div class=\"searContent\"><strong>题注：</strong></div>\n" +
//                "                                  </td>\n" +
//                "                              </tr>\n" +
//                "                          </table>\n" +
//                "                      </div>";

        //匹配
        Matcher m = p.matcher(content);
        // System.out.println(p.matcher("sf@sina").matches());
        List<String> lists=new ArrayList<>();
        // 通过Matcher类的group方法和find方法来进行查找和匹配
        while (m.find()) {
            String value = m.group();
            lists.add(value);
        }
        for(String s:lists){
            System.out.println(s);
        }
    }
    public void createNewUrl(List<String> lists) throws IOException {
        //创建文件夹和文件，用于保存链接
        File file = new File("a"+ File.separator+"newURL.txt");
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
        for(String str:lists){
            //拼接URL和标题
            String strurl="http://search.chinalaw.gov.cn/"+str.substring(str.indexOf("href=",0)+6,str.indexOf("target",0)-2)+"&PageIndex=";
            System.out.println(strurl);
            String strtitle=str.substring(str.indexOf("blank\">",0)+7,str.indexOf("</a>",0));
            System.out.println(strtitle);

            URL url=new URL(strurl);
            //通过url建立与网页的连接
            URLConnection conn=url.openConnection();
            //通过链接取得网页返回的数据
            InputStream is=conn.getInputStream();

            BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));

            //按行读取并打印
            String line=null;
            System.out.println("开始读取");
            Integer totalPage=0;

            //第<input type="text" id="index" value="1">/<span id="pagecount">2</span>页
            Pattern p = Pattern.compile("第<input type=\"text\" id=\"index\" value=\"1\">/<span id=\"pagecount\">\\d</span>页");

            while((line=br.readLine())!=null){
                //查找总页数
                //匹配
                Matcher m = p.matcher(line);
                // 通过Matcher类的group方法和find方法来进行查找和匹配
                while (m.find()) {
                    String ss = m.group();
                    System.out.println("页码所在行："+ss);
                    totalPage=Integer.valueOf(ss.substring(str.indexOf("pagecount\">",0)+11,str.indexOf("</span>页",0)));
                }
            }
            System.out.println("读取结束");
            //拼接新的网址信息
            String newURL=strurl+"###"+strtitle+"###"+totalPage;
            pw.println(newURL);
            br.close();
        }
        pw.close();
    }
}
