package comp4342.grp15.gem.DAO;

import comp4342.grp15.gem.Model.ClientPostMeta;
import comp4342.grp15.gem.Model.UploadMeta;

import java.util.ArrayList;

public class DBController {
    static OracleConnection conn;
    static FileOutputStream stream;
    
    static {
        try {
            stream = new FileOutputStream("SQL_File.txt");
            stream.write(("Record All SQL statements at " + new Date() + "\n").getBytes());
        } catch (Exception ignored) {
        }
    }
    
    // get the latest post items, refer to 文档 从服务端获取动态的Json
    public ArrayList<ClientPostMeta> getTrends(){
        return null;
    }

    /**
     * @param uploadMeta 将其信息添加成为数据库中 post 的新条目。
     */
    public void savePost(UploadMeta uploadMeta){
    }
}
