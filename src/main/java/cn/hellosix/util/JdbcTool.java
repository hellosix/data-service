package cn.hellosix.util;

import net.sf.json.JSONArray;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzz on 17/5/1.
 */
public class JdbcTool {
    public static List<String> exe(String host, String username, String passowrd, String sql) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection( host, username, passowrd);
            stmt = conn.prepareStatement( sql );
            rs = stmt.executeQuery();
            List result = convertToList(rs);
            return result;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
        return null;
    };

    public static <T> List<T> convertToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map<String, Object> rowData = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        JSONArray jr = JSONArray.fromObject(list);
        List<T> resultList = JSONArray.toList(jr);
        return resultList;
    }
}
