package cn.hellosix.dao;

import cn.hellosix.util.DBTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzz on 17/5/1.
 */
public class Sql {
    public Map getSqlById(int id){
        DBTool dbTool = DBTool.getSqlite();
        String sql = "select * from sql_detail where id=" + id;
        Map res = dbTool.selectRow( sql );
        System.out.println( res );
        return res;
    }

    public int addSql(HashMap hm){
        String sql = "insert into sql_detail(client_sql, host, username, password, param) " +
                "value('" + hm.get("client_sql") + "', '" + hm.get("host")+ "', '"+ hm.get("username") +"', '"+ hm.get("password")+"', '"+hm.get("param")+"')";
        System.out.println(sql);
        DBTool dbTool = DBTool.getSqlite();
        int id = dbTool.insertId( sql );
        return id;
    }

    public List allSql(){
        DBTool dbTool = DBTool.getSqlite();
        List res = dbTool.select("select * from sql_detail");
        return res;
    }

    public List allHost(){
        DBTool dbTool = DBTool.getSqlite();
        List<Map> res = dbTool.select("select host from sql_detail group by host");
        List columns = new ArrayList();
        for(int i = 0; i < res.size(); i++ ){
            columns.add( res.get(i).get("host"));
        }
        return columns;
    }

    public List reqSqlCount() {
        DBTool dbTool = DBTool.getSqlite();
        List<Map> res = dbTool.select("select sql_id, count(*) as num from history group by sql_id");
        return res;
    }

    public List statHisoryList() {
        String sql = "select * from (select sql_id, count(*) as num, sum(run_time) as sum, min(run_time) as min, max(run_time) as max,avg(run_time) as avg from history group by sql_id) as t1 left join (select id, client_sql from sql_detail) as t2 on t1.sql_id=t2.id";
        DBTool dbTool = DBTool.getSqlite();
        List<Map> res = dbTool.select( sql );
        return res;
    }

    public List dateGroupHistory() {
        String sql = "select date, count(*) as num from (select date_format(start_time,'%H') as date from history) as t1 group by t1.date";
        DBTool dbTool = DBTool.getSqlite();
        List<Map> res = dbTool.select( sql );
        return res;
    }
}
