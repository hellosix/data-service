package cn.hellosix.dao;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzz on 17/5/1.
 */
public class TestSql {
    @Test
    public void testAddSql(){
        Sql sql = new Sql();
        HashMap hm = new HashMap();
        int id = sql.addSql(hm);
        System.out.println( id );
    }

    @Test
    public void testSelectSql(){
        Sql sql = new Sql();
        Map hm = sql.getSqlById(1);
        System.out.println( hm );
    }
}
