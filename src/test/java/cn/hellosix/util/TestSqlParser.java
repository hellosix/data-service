package cn.hellosix.util;

import org.junit.Test;

import java.util.HashMap;

/**
 * Created by lzz on 17/4/27.
 */
public class TestSqlParser {
    @Test
    public void testStr(){
        SqlParser sqlParser = new SqlParser();
        String sql = "select * from table1 where {user_name='$name'} {and password=$pass} group by {$abc} {, $abb} order by {$aby} desc, aa";
        sql = "select * from user where id<>{$id} ";
        //如果 name 为空清空括号内
        String name = "";
        String name_key = "$name";
        HashMap hm = new HashMap();
        hm.put("$id", "1");
        hm.put("$pass", "");
        hm.put("$abc", "dd");
        hm.put("$abb", "");
        hm.put("$aby", "");
        sql = sqlParser.syntaxParser(sql, hm);
        System.out.println(sql);

    }


}
