import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张洲徽 on 2019/1/11.
 */
public class HandleData {
    public static void main(String args[]) throws Exception {
        //测试获取状态为空的page
        List<Integer> pages=new ArrayList<Integer>();
        pages=HandleData.getPages();
        Integer page=null;
        try{
            while (true) {
                page = pages.remove(0);
                System.out.println(page);
            }
        }catch (IndexOutOfBoundsException e1){
            System.out.println("遍历结束");
        }catch (Exception e2){
            e2.printStackTrace();
        }

        //测试updateStatus
//        updateStatus(1,"已完成");
    }
    //读取未下载的page
    public static List<Integer> getPages(){
        List<Integer> list=new ArrayList<Integer>();
        try{
            Connection conn=DBUtil2.getConnection();
            Statement state= conn.createStatement();
            ResultSet rs=state.executeQuery(
                    "SELECT PAGE FROM FLFG_PAGES " +
                            "WHERE STATUS!='已完成' or STATUS is null " +
                            "ORDER BY PAGE");
            while(rs.next()){
                int page = rs.getInt("page");
                System.out.println(page);
                list.add(page);
            }
            rs.close();
            state.close();
            //return list;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DBUtil2.closeConnection();
            return list;
        }
    }

    //更改文件状态
    public static void updateStatus(Integer page,String status){
        try{
            Connection conn= DBUtil2.getConnection();
            String sql = "UPDATE FLFG_PAGES set STATUS=? where PAGE=?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2,page);
            ps.executeUpdate();
            System.out.println("更改："+page+"页状态为："+status);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DBUtil2.closeConnection();
        }
    }
}
