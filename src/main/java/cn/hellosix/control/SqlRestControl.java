package cn.hellosix.control;

import cn.hellosix.logic.SqlLogic;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lzz on 17/5/1.
 */
@RestController
public class SqlRestControl {
    @RequestMapping(value="/sql/add_sql_ajax", method = RequestMethod.POST)
    public JSONObject addSqlAjax(@RequestBody JSONObject requestBody){
        SqlLogic sqlLogic = new SqlLogic();
        int restfulId = sqlLogic.addSql(requestBody);
        JSONObject res = new JSONObject();
        res.put("id", restfulId);
        return res;
    }

    @RequestMapping(value = "/sql/test_jdbc_ajax", method = RequestMethod.POST)
    public JSONObject addTestJdbcAjax(@RequestBody JSONObject requestBody){
        SqlLogic sqlLogic = new SqlLogic();
        HashMap res = sqlLogic.testJdbc(requestBody);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", res);
        return jsonObject;
    }

    @RequestMapping(value="/sql/run_by_id", method = RequestMethod.POST)
    public JSONObject RunById(@RequestBody JSONObject requestBody){
        SqlLogic sqlLogic = new SqlLogic();
        List result = sqlLogic.runById( requestBody );
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @RequestMapping(value="/sql/run_by_client", method = RequestMethod.POST)
    public JSONObject RunByClient(@RequestBody JSONObject requestBody){
        SqlLogic sqlLogic = new SqlLogic();
        HashMap resHm = sqlLogic.runByClient( requestBody );
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", resHm.get("result"));
        jsonObject.put("sql", resHm.get("sql"));
        return jsonObject;
    }

    /**
     {
     "id":5,
     "param":[{"key":"id", "value":"1"},{"key":"name", "value":"3"}]
     }
     */
}
