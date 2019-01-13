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

    //更改文件状态
    public static void updateGetLowidStatus(Integer page,String status){
        try{
            Connection conn= DBUtil2.getConnection();
            String sql = "UPDATE FLFG_PAGES set GET_ID_AND_TITLE_STATUS=? where PAGE=?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2,page);
            ps.executeUpdate();
            System.out.println("插入："+page+"页状态为："+status);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DBUtil2.closeConnection();
        }
    }

    //插入读取的lowID等信息
    public static void insertLowid(
            Integer page,Integer lowid,String title,String publishDepartment,String publishTime,String tiZhu){
        try{
            Connection conn= DBUtil2.getConnection();
            String sql = "insert into  FLFG_INFO ( PAGE, LOWID, TITLE, PUBLISHDEPARTMENT, PUBLISHTIME ) " +
                    "VALUES (?,?,?,?,?)";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setInt(1, page);
            ps.setInt(2,lowid);
            ps.setString(3, title);
            ps.setString(4, publishDepartment);
            ps.setString(5, publishTime);
            ps.setString(6, tiZhu);
            ps.executeUpdate();
            System.out.println("插入："+page+"，lowID："+lowid);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DBUtil2.closeConnection();
        }
    }

    //清除插入失败的lowID等信息
    public static void deleteLowid(Integer page){
        try{
            Connection conn= DBUtil2.getConnection();
            String sql = "DELETE from FLFG_INFO where PAGE=?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setInt(1, page);
            ps.executeUpdate();
            System.out.println("删除："+page+"信息");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DBUtil2.closeConnection();
        }
    }
}
