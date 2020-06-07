package propensi.GCG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import propensi.GCG.model.*;
import propensi.GCG.service.AclService;
import propensi.GCG.service.MenuService;
import propensi.GCG.service.PermissionConfigService;
import propensi.GCG.service.RoleService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private AclService aclService;

    @Autowired
    private PermissionConfigService permissionConfigService;

    @GetMapping(value = "")
    public String manageRole(Model model) {
        permissionConfigService.checkPermission("Lihat Daftar Role");

        List<RoleModel> roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        return "role-mengelola";
    }

    @GetMapping(value = "/tambah")
    public String addRoleForm(Model model) {
        permissionConfigService.checkPermission("Tambah Role");

        RoleModel roleModel = new RoleModel();
        List<MenuModel> parentMenu = menuService.getParentMenu();
        List<MenuModel> childMenu = menuService.getChildMenu();

        model.addAttribute("roleModel", roleModel);
        model.addAttribute("parentMenu", parentMenu);
        model.addAttribute("childMenu", childMenu);
        return "role-form-tambah";
    }

    @PostMapping(value = "/tambah")
    public String addRoleSubmit(@ModelAttribute RoleModel roleModel, @RequestParam List<Long> aclCheckbox) {
        RoleModel role = roleService.addRole(roleModel);

        if (aclCheckbox != null) {
            for (Long id : aclCheckbox) {
                ACLModel acl = new ACLModel();
                acl.setAclRole(role);
                acl.setAclMenu(menuService.getById(id).get());
                aclService.addAcl(acl);
            }
        }

        return "redirect:/role";
    }

    @GetMapping(value = "/ubah/{id}")
    public String editRoleForm(Model model, @PathVariable(value = "id") Long id) {
        permissionConfigService.checkPermission("Ubah Role dan Konfigurasi Izin Akses");
        RoleModel roleModel = roleService.getById(id).get();
        List<MenuModel> parentMenu = menuService.getParentMenu();
        List<MenuModel> childMenu = menuService.getChildMenu();

        List<ACLModel> aclByRole = aclService.getByRole(roleModel);
        List<MenuModel> selectedMenu = new ArrayList<>();
        for (ACLModel acl: aclByRole) {
            selectedMenu.add(acl.getAclMenu());
        }

        List<List<?>> finalChildMenu = new ArrayList();
        for (MenuModel x : childMenu) {
            List temp = new ArrayList();
            temp.add(x);
            if (selectedMenu.contains(x)) {
                temp.add(true);
            }
            else {
                temp.add(false);
            }
            finalChildMenu.add(temp);
        }

        model.addAttribute("roleModel", roleModel);
        model.addAttribute("parentMenu", parentMenu);
        model.addAttribute("childMenu", finalChildMenu);
        return "role-form-ubah";
    }

    @PostMapping(value = "/ubah")
    public String editRoleSubmit(@ModelAttribute RoleModel roleModel, @RequestParam(required = false) List<Long> aclCheckbox) {
        List<ACLModel> pastAcl = aclService.getByRole(roleModel);
        aclService.deleteSomeAcl(pastAcl);

        RoleModel role = roleService.addRole(roleModel);

        if (aclCheckbox != null) {
            for (Long id: aclCheckbox) {
                ACLModel acl = new ACLModel();
                acl.setAclRole(role);
                acl.setAclMenu(menuService.getById(id).get());
                aclService.addAcl(acl);
            }
        }
        return "redirect:/role";
    }
}
