package cn.hellosix.dao;

import cn.hellosix.util.DBTool;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lzz on 17/5/2.
 */
public class History {

    /**
     * 获取所有的历史记录
     */
    public List getAllHistory(){
        DBTool dbTool = DBTool.getSqlite();
        List res = dbTool.select("select * from history");
        return res;
    }

    public int addOneHisotry(HashMap hm){
        String sql = "insert into history(sql_id, run_sql, param, run_time, client_ip, client_type) " +
                "value(" + hm.get("sql_id") + ", \"" + hm.get("run_sql")+ "\", '"+ hm.get("param") +"', "+ hm.get("run_time")+", '"+hm.get("client_ip")+"', '"+hm.get("client_type")+"')";
        System.out.println(sql);
        DBTool dbTool = DBTool.getSqlite();
        int id = dbTool.insertId( sql );
        return id;
    }
}
