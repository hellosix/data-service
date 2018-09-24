package cn.hellosix.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static java.util.Map.Entry;

/**
 * Created by lzz on 17/4/27.
 * sql: select * from table1 {where user_name='$name'}
 */
public class SqlParser {
    private final static String LEFT_TAG = "{";
    private final static String RIGHT_TAG = "}";
    private final static ArrayList<String> TAIL_TAGS = new ArrayList<String>();
    private final static HashMap<String, List<String>> SQL_SYNTAX_RIGHT = new HashMap<String, List<String>>();
    private final static HashMap<String, List<String>> SQL_SYNTAX_LEFT = new HashMap<String, List<String>>();
    static {
        // 后面的放前面
        TAIL_TAGS.add(";");
        TAIL_TAGS.add("by");
        TAIL_TAGS.add("order");
        TAIL_TAGS.add("group");
        TAIL_TAGS.add("and");
        TAIL_TAGS.add("limit");
        TAIL_TAGS.add("where");
        TAIL_TAGS.add(",");

        ArrayList<String> whereRight = new ArrayList();
        whereRight.add( "and" );
        whereRight.add( "group" );
        whereRight.add( "by" );
        whereRight.add( "order" );
        whereRight.add( "limit" );
        SQL_SYNTAX_RIGHT.put("where", whereRight );

        ArrayList<String> byRight = new ArrayList();
        byRight.add("desc");
        byRight.add("asc");
        byRight.add(",");
        SQL_SYNTAX_RIGHT.put("by", byRight);

        ArrayList<String> orderLeft = new ArrayList();
        orderLeft.add("by");
        orderLeft.add("group");
        orderLeft.add("where");
        orderLeft.add("and");
        orderLeft.add(",");
        SQL_SYNTAX_LEFT.put("order", orderLeft);

        ArrayList<String> groupLeft = new ArrayList();
        groupLeft.add("and");
        SQL_SYNTAX_LEFT.put("group", groupLeft);
    }


    public String syntaxParser(String sql, HashMap<String, String> hm){
        System.out.println(sql);
        sql = sql.toLowerCase();
        // 先进行参数的语法解析
        Iterator iter = hm.entrySet().iterator();
            while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            String field = (String) entry.getKey();
            String value = (String) entry.getValue();
            sql = parser( sql, field, value );
        }
        // sql 语法规范解析
        System.out.println(sql);
        //sql = sqlSyntaxRight(sql, SQL_SYNTAX_RIGHT);
        // sql 去除非法结尾
        sql = removeTailTag(sql, TAIL_TAGS);
        return sql;
    }

    private String sqlSyntaxLeft(String sql, HashMap<String, List<String>> sqlSyntaxLeft) {
        Iterator iter = sqlSyntaxLeft.entrySet().iterator();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            String key = (String) entry.getKey();
            List<String> valueList = (List<String>) entry.getValue();
            for( int i = 0; i < valueList.size(); i++ ){
                String value = valueList.get(i);
                String left = leftStr(sql, key).trim();
                if( left.equals( sql ) ){
                    return left;
                }
                // 递归调用比如有 group by abc order by这样要判断多个by
                left = sqlSyntaxLeft(left, sqlSyntaxLeft);
                String right = rightStr(sql, key).trim();
                if( right.equals( sql ) ){
                    return left;
                }
                // 递归调用比如有 group by abc order by这样要判断多个by
                right = sqlSyntaxLeft(right, sqlSyntaxLeft);
                int index = left.lastIndexOf( value );
                // 如果没有要去除的就返回原sql
                if( index < 0 ){
                    return sql;
                }
                int key_index = left.length() - value.length();
                if( index == key_index ){
                    left = left.substring(0, key_index ).trim();
                    System.out.println(left);
                    sql = left + " " + key + " " + right;
                }
            }
        }
        return sql;
    }

    /**
     * sql 标准语法规范右边的比如 where 右边不能有 and
     * @param sql
     * @param sqlSyntax
     * @return
     */
    private String sqlSyntaxRight(String sql, HashMap<String, List<String>> sqlSyntax) {
        Iterator iter = sqlSyntax.entrySet().iterator();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            String key = (String) entry.getKey();
            List<String> valueList = (List<String>) entry.getValue();
            for( int i = 0; i < valueList.size(); i++ ){
                String value = valueList.get(i);
                String left = leftStr( sql, key ).trim();
                String right = rightStr( sql, key ).trim();
                if( value.length() > right.length() ){
                    break;
                }
                String tag = right.substring(0, value.length()).trim();
                if( tag.equals(value) ){
                    right = right.substring( tag.length() );
                    sql = left + " " + key + right;
                }
            }
        }
        return sql;
    }

    /**
     * sql 标准语法规范右边的比如 where 右边不能有 and
     * @param sql
     * @param sqlSyntax
     * @return
     */
    private String sqlSyntaxRight2(String sql, HashMap<String, List<String>> sqlSyntax) {
        Iterator iter = sqlSyntax.entrySet().iterator();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            String key = (String) entry.getKey();
            List<String> valueList = (List<String>) entry.getValue();
            for( int i = 0; i < valueList.size(); i++ ){
                String value = valueList.get(i);
                String left = leftStr(sql, key).trim();
                if( left.equals( sql ) ){
                    return left;
                }
                // 递归调用比如有 group by abc order by这样要判断多个by
                left = sqlSyntaxRight(left, sqlSyntax);
                String right = rightStr(sql, key).trim();
                if( right.equals( sql ) ){
                    return right;
                }
                right = sqlSyntaxRight(right, sqlSyntax);
                int index = right.indexOf( value );
                // 如果没有要去除的就返回原sql
                if( index < 0 ){
                    return sql;
                }
                if( index == 0 ){
                    right = right.substring( index + value.length() ).trim();
                    System.out.println(right);
                    sql = left + " " + key + " " + right;
                }
            }
        }
        return sql;
    }

    /**
     * 参数解析
     * @param sql
     * @param field
     * @param value
     * @return
     */
    public String parser( String sql, String field, String value ){
        if( value == null || sql.indexOf( field.substring(1) ) < 0 ){
            return sql;
        }
        String leftStr = leftStr(sql, field);
        int leftIndex = leftStr.lastIndexOf( LEFT_TAG );
        if( leftIndex < 0 ){
            return leftStr;
        }
        String rightStr = rightStr(sql, field);
        int rightIndex = rightStr.indexOf( RIGHT_TAG);
        if( rightIndex < 0 ){
            return rightStr;
        }
        if( value.length() > 0 ){ //值不为空那么就拼接起来
            leftStr = removeLastSubStr(leftStr, LEFT_TAG);
            rightStr = removeStartSubStr(rightStr, RIGHT_TAG);
            return leftStr + value + rightStr;
        }else{ // 为空那么就去掉括号的内容
            leftStr = leftStr.substring(0, leftIndex);
            rightStr = rightStr.substring( rightIndex + 1, rightStr.length() );
            return leftStr + rightStr;
        }
    }

    public String removeStartSubStr(String sql, String tag){
        int index = sql.indexOf( tag );
        if( index < 0 ){
            return sql;
        }
        sql = removeSubStr( sql, tag, index );
        return sql;
    }
    public String removeLastSubStr(String sql, String tag){
        int index = sql.lastIndexOf(tag);
        if( index < 0 ){
            return sql;
        }
        sql = removeSubStr( sql, tag, index );
        return sql;
    }

    public String removeSubStr(String sql, String tag, int index){
        sql = sql.substring(0, index) + sql.substring( index + tag.length(), sql.length());
        return  sql;
    }

    private String leftStr(String sql, String tag){
        int index = sql.indexOf( tag );
        if( index < 0 ){
            return sql;
        }
        String leftStr = sql.substring(0, index);
        return leftStr;
    }
    private String rightStr(String sql, String tag){
        int index = sql.indexOf( tag );
        if( index < 0 ){
            return sql;
        }
        String rightStr = sql.substring(index+tag.length());
        return rightStr;
    }

    /**
     * 删除非法结尾词
     * @param sql
     * @param tailTags
     * @return
     */
    private String removeTailTag(String sql, ArrayList<String> tailTags){
        sql = sql.trim();
        int tagSize = tailTags.size();
        while ( true ){
            boolean flag = true; // 表识已经是规范语法
            for(int i = 0; i < tagSize; i++){
                String item = tailTags.get(i).trim();
                int index = sql.length() - item.length();
                String tail = sql.substring(index).trim();
                if( tail.equals( item ) ){
                    flag = false; //如果存在非法结尾
                    sql = sql.substring(0, index).trim();
                }
            }
            if( flag ){
                break;
            }
        }
        return sql;
    }
}
