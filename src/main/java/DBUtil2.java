import org.apache.commons.dbcp.BasicDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by 张洲徽 on 2019/1/11.
 */
public class DBUtil2 {
    private static BasicDataSource ds;
    private static ThreadLocal<Connection> tl;
    static{
        try{
            Properties prop= new Properties();
            InputStream is= DBUtil2.class.getClassLoader().getResourceAsStream("config.properties");
            prop.load(is);
            is.close();
            ds = new BasicDataSource();
            ds.setDriverClassName(prop.getProperty("driver"));//设置驱动 (Class.forName())
            ds.setUrl(prop.getProperty("url"));  //设置url
            ds.setUsername(prop.getProperty("user"));
            ds.setPassword(prop.getProperty("pwd"));
//            ds.setInitialSize(Integer.parseInt(prop.getProperty("initsize")));
//            ds.setMaxActive(Integer.parseInt(prop.getProperty("maxactive")));
//            ds.setMaxWait(Integer.parseInt(prop.getProperty("maxwait")));
//            ds.setMinIdle(Integer.parseInt(prop.getProperty("minidle")));//设置最小空闲数
//            ds.setMaxIdle(Integer.parseInt(prop.getProperty("maxidle")));
            tl = new ThreadLocal<Connection>();//初始化线程本地
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        Connection conn = ds.getConnection();  //通过连接池获取一个空闲连接
        tl.set(conn);
        return conn;
    }
    public static void closeConnection(){
        try{
            Connection conn = tl.get();
            if(conn != null){
                conn.setAutoCommit(true);
//通过连接池获取的Connection的close()方法,实际上并没有将连接关闭，而是将该链接归还。
                conn.close();
                tl.remove();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
