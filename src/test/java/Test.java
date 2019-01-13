import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张洲徽 on 2019/1/8.
 */
public class Test {
    public static void main(String[] args) throws IOException, SQLException {
//        String s="<h3 style=\"width: 100%;\"><a href=\"";
//        System.out.println(s.length());

//        String str="<h3 style=\"width: 100%;\"><a href=\"law/searchTitleDetail?LawID=400287&Query=%25%25&IsExact=\" target=\"_blank\">最高人民法院关于行政申请再审案件立案程序的规定</a></h3>";
//        System.out.println(str);
////        str=str.substring(str.indexOf("href=",0)+6,str.indexOf("target",0)-2);
//        str=str.substring(str.indexOf("blank\">",0)+7,str.indexOf("</a>",0));
//        System.out.println(str);

//        String str="第<input type=\"text\" id=\"index\" value=\"1\">/<span id=\"pagecount\">2</span>页";
//        str=str.substring(str.indexOf("pagecount\">",0)+11,str.indexOf("</span>页",0));
//        System.out.println(str);

//        StringBuffer ss=new StringBuffer();
//        if(ss.toString()==null){
//            System.out.println("null");
//        }else {
//            System.out.println("not null");
//            System.out.println("："+ss.toString().length());
//        }

        //测试创建文件夹位置
//        File file = new File("C:\\Users\\热带雨林\\Desktop\\test"+ File.separator+"a.html");
//        File parent = file.getParentFile();
//        if(!parent.exists()){
//            parent.mkdirs();
//        }
//        if(!file.exists()){
//            file.createNewFile();
//            System.out.println("文件创建完毕");
//        }else {
//            System.out.println("文件已经存在");
//        }

        //测试src下创建数据库配置文件
        try{
            Connection conn=DBUtil2.getConnection();
            conn.setAutoCommit(false);
            String sql ="INSERT INTO flfg_pages VALUES(?,NULL,null )";
            PreparedStatement ps= conn.prepareStatement(sql);
            for(int i=1;i<=6570;i++){
                ps.setInt(1, i);
                ps.addBatch();
                if(i%1000==0){
                    ps.executeBatch();
                    System.out.println("插入"+i);
                }
            }
            ps.executeBatch();
            System.out.println("插入剩余记录");
            conn.commit();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DBUtil2.closeConnection();
        }

        //测试List的取出
//        List<Integer> list=new ArrayList<Integer>();
//        for(int i=0;i<10;i++){
//            list.add(i);
//        }
//        Integer num=null;
//        while (true){
//            try{
//                num=list.remove(0);
//                System.out.println(num);
//            }catch (IndexOutOfBoundsException e1){
//                System.out.println("遍历结束");
//                break;
//            }catch (Exception e2){
//                e2.printStackTrace();
//            }
//        }

        //测试空List的长度
//        List<Integer> pages=HandleData.getPages();
//        if(pages.isEmpty()){
//            System.out.println("数据库中查不到需要下载的文件");
//            System.exit(0);
//        }else {
//            System.out.println("找到page");
//        }

    }
}
