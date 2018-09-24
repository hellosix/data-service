package cn.hellosix.control;

import cn.hellosix.logic.SqlLogic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by lzz on 17/4/26.
 */
@Controller
public class SqlController {
    @RequestMapping("/sql/addSql")
    public String addSql(Model model) {
        SqlLogic sqlLogic = new SqlLogic();
        List hosts = sqlLogic.getAllHost();
        model.addAttribute("hosts", hosts);
        return "add_sql";
    }

    @RequestMapping("/sql/mixQuery")
    public String mixQuery(){
        return "mix_query";
    }
}
