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
public class CommonController {
    @RequestMapping("/list")
    public String list(Model model) {
        SqlLogic sqlLogic = new SqlLogic();
        List res = sqlLogic.getAllSql();
        model.addAttribute("sqlList", res );
        return "list";
    }

    @RequestMapping("/history")
    public String hisotry(Model model) {
        SqlLogic sqlLogic = new SqlLogic();
        List res = sqlLogic.getAllHistory();
        model.addAttribute("hisotryList", res );
        return "history";
    }
    @RequestMapping("/statistics")
    public String statistics(Model model) {
        SqlLogic sqlLogic = new SqlLogic();
        List reqCountList = sqlLogic.getReqCount();
        List statList = sqlLogic.statHistoryList();
        List dateGroupList = sqlLogic.dateGroupHistory();

        model.addAttribute("reqCountList", reqCountList );
        model.addAttribute("statList", statList );
        model.addAttribute("dateGroupList", dateGroupList );
        return "statistics";
    }

    @RequestMapping("/")
    public String home(Model model) {
        return "index";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        return "index";
    }
}
