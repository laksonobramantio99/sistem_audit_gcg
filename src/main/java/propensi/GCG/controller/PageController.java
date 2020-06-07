package propensi.GCG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import propensi.GCG.model.PeriodeTahunModel;
import propensi.GCG.model.UserModel;
import propensi.GCG.service.PeriodeTahunService;
import propensi.GCG.service.RoleService;
import propensi.GCG.service.UserService;

@Controller
public class PageController {
    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Autowired
    PeriodeTahunService periodeTahunService;

    @RequestMapping("/")
    public String home(Model model) {
        return "redirect:/dashboard";
    }

    @RequestMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :) */
            return "redirect:/";
        }
        return "login";
    }

    @RequestMapping("/ubahPassword")
    public String changePassword(Model model) {
        UserModel user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "akun-form-ubahpassword-user";
    }
}
