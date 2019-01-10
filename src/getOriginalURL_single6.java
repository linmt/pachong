import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 张洲徽 on 2019/1/10.
 */
public class getOriginalURL_single6 {

    public static void main(String args[]) throws Exception {
        for(int page=3942;page>3285;page--){
            String strurl="http://search.chinalaw.gov.cn/SearchLawTitle?effectLevel=&SiteID=124&Query=%25%25&Sort=PublishTime&Type=1&PageIndex="+page;
            workurl(strurl,page);
        }
        System.out.println("执行完所有任务");
    }

    public static void workurl(String strurl, int page) throws Exception {
        //创建文件夹和文件，用于保存链接
        File file = new File("page"+ File.separator+page+".html");
        File parent = file.getParentFile();
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
        FileOutputStream fos= new FileOutputStream(file);
        OutputStreamWriter osw= new OutputStreamWriter(fos, "UTF-8");
        PrintWriter pw= new PrintWriter(osw);

        URL url=new URL(strurl);
        HttpURLConnection conn=null;
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
//        conn.setChunkedStreamingMode(0);
//        conn.setDoInput(true);
//        conn.setDoOutput(true);

        //通过链接取得网页返回的数据
        InputStream is=conn.getInputStream();
        BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));

//        for(int i=0;i<100;i++){
//            pw.println("dasdsadasdsadsadasdsadasdsadsadsadad");
//            pw.flush();
//        }
        //按行读取并打印
        String line = null;
        System.out.println("开始写入："+page);
        while((line=br.readLine())!=null){
            //System.out.println(line);
            pw.println(line);
            pw.flush();
        }
        System.out.println("写入结束："+page);

        conn.disconnect();
        br.close();
        pw.close();
        System.gc();
    }
}
