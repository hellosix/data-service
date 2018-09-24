package cn.hellosix.logic;

import cn.hellosix.dao.History;
import cn.hellosix.dao.Sql;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.hellosix.util.ClientSign;
import cn.hellosix.util.JdbcTool;
import cn.hellosix.util.SqlParser;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzz on 17/5/1.
 */
public class SqlLogic {

    public List runById(JSONObject requestBody) {
        HashMap historyHm = new HashMap();

        Sql sql = new Sql();
        int id = (int) requestBody.get("id");
        // 根据 ID 获取sql 详情
        Map sqlRow = sql.getSqlById( id );
        String client_sql = (String) sqlRow.get("client_sql");
        String host = (String) sqlRow.get("host");
        String username = (String) sqlRow.get("username");
        String password = (String) sqlRow.get("password");
        // 将 param 参数转成 hashmap 类型
        JSONArray paramList = (JSONArray) requestBody.get("param");

        HashMap hm = new HashMap();
        for(int i = 0; i < paramList.size(); i++){
            JSONObject paramItem = (JSONObject) paramList.get(i);
            String key = "$" + paramItem.get("key");
            String value = (String) paramItem.get("value");
            hm.put( key, value );
        }
        // 调用 sqlParser.syntaxParser 将 sql 和 param 一起翻译成要执行的sql
        SqlParser sqlParser = new SqlParser();
        String trueSql = sqlParser.syntaxParser( client_sql, hm );
        // 根据 username, host, password 获取jdbc 实例
        List result = null;
        try {
            long start = System.currentTimeMillis();
            result = JdbcTool.exe(host, username, password, trueSql );
            long end = System.currentTimeMillis();
            long runTime = end - start;
            historyHm.put("run_time", runTime);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        historyHm.put("sql_id", id);
        historyHm.put("run_sql", trueSql);
        historyHm.put("param", paramList.toString());
        historyHm.put("client_ip", ClientSign.clientIp());
        historyHm.put("client_type", "restful");
        History history = new History();
        history.addOneHisotry(historyHm);

        return result;
    }

    /**
     * 添加 sql 详情
     * @param requestBody
     * @return
     */
    public int addSql(JSONObject requestBody) {
        HashMap hm = new HashMap();
        String client_sql = requestBody.getString("sql");
        client_sql = client_sql.replaceAll("'", "''");
        String host = requestBody.getString("host");
        String password = requestBody.getString("password");
        String username = requestBody.getString("username");

        JSONArray paramList = requestBody.getJSONArray("param_list");
        JSONObject paramObj = new JSONObject();
        paramObj.put("param", paramList);
        String param = paramObj.toString();

        hm.put("client_sql", client_sql);
        hm.put("host", host);
        hm.put("password", password);
        hm.put("username", username);
        hm.put("param", param);
        Sql sql = new Sql();
        int res = sql.addSql( hm );
        return res;
    }

    public HashMap runByClient(JSONObject requestBody) {
        HashMap historyHm = new HashMap();

        String client_sql = requestBody.getString("sql");
        String username = requestBody.getString("username");
        String password = requestBody.getString("password");
        String host = requestBody.getString("host");
        JSONArray paramList = requestBody.getJSONArray("param_list");
        HashMap hm = new HashMap();
        for(int i = 0; i < paramList.size(); i++){
            JSONObject paramItem = (JSONObject) paramList.get(i);
            String key = "$" + paramItem.get("field");
            String value = (String) paramItem.get("value");
            hm.put( key, value );
        }
        // 调用 sqlParser.syntaxParser 将 sql 和 param 一起翻译成要执行的sql
        SqlParser sqlParser = new SqlParser();
        String trueSql = sqlParser.syntaxParser( client_sql, hm );
        System.out.println(trueSql);
        HashMap resHm = new HashMap();
        resHm.put("sql", trueSql );
        // 根据 username, host, password 获取jdbc 实例
        try {
            List result;
            long start = System.currentTimeMillis();
            result = JdbcTool.exe(host, username, password, trueSql );
            resHm.put("result", result);
            long end = System.currentTimeMillis();
            long runTime = end - start;
            historyHm.put("run_time", runTime);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        historyHm.put("run_sql", trueSql);
        historyHm.put("sql_id", -1);
        historyHm.put("param", "");
        historyHm.put("client_ip", ClientSign.clientIp());
        historyHm.put("client_type", "test");
        History history = new History();
        history.addOneHisotry(historyHm);
        return resHm;
    }

    public List getAllSql() {
        Sql sql = new Sql();
        List res = sql.allSql();
        return res;
    }

    public List getAllHistory(){
        History history = new History();
        List res = history.getAllHistory();
        return res;
    }

    public List getAllHost() {
        Sql sql = new Sql();
        List res = sql.allHost();
        return res;
    }

    public List getReqCount() {
        Sql sql = new Sql();
        List res = sql.reqSqlCount();
        return res;
    }

    public List statHistoryList() {
        Sql sql = new Sql();
        List res = sql.statHisoryList();
        return res;
    }

    public List dateGroupHistory() {
        Sql sql = new Sql();
        List res = sql.dateGroupHistory();
        return res;
    }

    public HashMap testJdbc(JSONObject requestBody) {
        HashMap hm = new HashMap();
        hm.put("error_code", 0);
        return hm;
    }
}
