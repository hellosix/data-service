package cn.hellosix.util;

import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by lzz on 17/5/1.
 */
public class TestJdbcTool {
    @Test
    public void testExe() throws SQLException {
        String host = "jdbc:mysql://localhost:3306/test";
        String name = "root";
        String pass = "root";
        String sql = "select * from sql_detail";
        JdbcTool.exe(host, name, pass, sql);
    }
}
