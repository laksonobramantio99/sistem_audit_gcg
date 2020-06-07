package propensi.GCG.controller;

import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import propensi.GCG.model.DivisiModel;
import propensi.GCG.model.RoleModel;
import propensi.GCG.model.UserModel;
import propensi.GCG.service.DivisiService;
import propensi.GCG.service.PermissionConfigService;
import propensi.GCG.service.RoleService;
import propensi.GCG.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DivisiService divisiService;

    @Autowired
    private PermissionConfigService permissionConfigService;

    @GetMapping(value = "")
    public String manageUser(Model model) {
        permissionConfigService.checkPermission("Lihat Daftar Akun");
        List<UserModel> userList = userService.getAllUser();
        model.addAttribute("userList", userList);
        return "akun-mengelola";
    }

    @GetMapping(value = "/tambah")
    public String addUserForm(Model model) {
        permissionConfigService.checkPermission("Tambah Akun");

        DivisiModel divisiModel = new DivisiModel();
        divisiModel.setId(0L);
        RoleModel roleModel = new RoleModel();
        roleModel.setId(0L);

        UserModel userModel = new UserModel();
        userModel.setUserDivisi(divisiModel);
        userModel.setUserRole(roleModel);

        List<RoleModel> roleList = roleService.getRoleList();
        List<DivisiModel> divisiList = divisiService.getDivisiList();

        model.addAttribute("userModel", userModel);
        model.addAttribute("roleList", roleList);
        model.addAttribute("divisiList", divisiList);
        return "akun-form-tambah";
    }

    @PostMapping(value = "/tambah")
    public String addUserSubmit(@ModelAttribute UserModel userModel, Model model) {
        userModel.setActiveFlag(true);
        userService.addUser(userModel);
        return "redirect:/user";
    }

    @GetMapping(value = "/ubah/{id}")
    public String editUserForm(Model model, @PathVariable(value = "id") Long id) {
        permissionConfigService.checkPermission("Ubah Akun");

        UserModel user = userService.findUserById(id);
        List<RoleModel> roleList = roleService.getRoleList();
        List<DivisiModel> divisiList = divisiService.getDivisiList();

        model.addAttribute("user", user);
        model.addAttribute("roleList", roleList);
        model.addAttribute("divisiList", divisiList);
        return "akun-form-ubah";
    }

    @PostMapping(value = "/ubah")
    public String editUserSubmit(@ModelAttribute UserModel userModel, Model model) {
        userModel.setActiveFlag(true);
        userService.addUserWithoutEncryptPassword(userModel);
        return "redirect:/user";
    }

    @GetMapping(value = "/ubahPassword/{id}")
    public String editPasswordUserForm(Model model, @PathVariable(value = "id") Long id) {
        permissionConfigService.checkPermission("Ubah Akun");

        UserModel user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "akun-form-ubahpassword";
    }

    @PostMapping(value = "/ubahPassword")
    public String editPasswordUserSubmit(@ModelAttribute UserModel userModel, Model model) {
        userModel.setActiveFlag(true);
        userService.addUser(userModel);
        return "redirect:/user";
    }
}
