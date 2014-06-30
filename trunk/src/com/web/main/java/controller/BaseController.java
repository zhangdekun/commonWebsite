/**
 * @author zhangdekun
 *
 */
package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhangdekun
 *
 */
@Controller
public class BaseController {
    @RequestMapping("/")
    public String redirect() {
        return "redirect:/index";
    }
    @RequestMapping("/loginIndex")
    public String beforeLogin() {
        return "login";
    }
    
}
