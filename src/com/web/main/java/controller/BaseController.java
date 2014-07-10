/**
 * @author zhangdekun
 *
 */
package controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.PTService;
import dao.remoteDomain.model.PtsvDim;

/**
 * @author zhangdekun
 *
 */
@Controller
public class BaseController {
	@Autowired
	private PTService service;
    @RequestMapping(" ")
    public String redirect() {
        return "redirect:/loginIndex";
    }
    @RequestMapping("/loginIndex")
    public String beforeLogin() {
        return "login";
    }
    @RequestMapping("/login")
    public String login(@RequestParam Map<String,Object> map,Model model) {
    	String ptsv_code = (String)map.get("ptsv_code");
    	String select_type = (String)map.get("select_type");
    	PtsvDim ptsv_dim = service.getPtsv(ptsv_code);
    	Integer period_id = service.getLastPeriodId(map);
    	model.addAttribute("last_period_id", period_id);
    	model.addAttribute("ptsv", ptsv_dim);
    	model.addAttribute("select_type",select_type);
        return "index";
    }
    
}
