package propensi.GCG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import propensi.GCG.model.DivisiModel;
import propensi.GCG.service.DivisiService;
import propensi.GCG.service.PermissionConfigService;

import java.util.List;

@Controller
@RequestMapping("/divisi")
public class DivisiController {
    @Autowired
    private DivisiService divisiService;

    @Autowired
    private PermissionConfigService permissionConfigService;

    @GetMapping(value = "")
    public String manageDivisi(Model model) {
        permissionConfigService.checkPermission("Lihat Daftar Divisi");

        List<DivisiModel> divisiList = divisiService.getDivisiList();
        model.addAttribute("divisiList", divisiList);
        return "divisi-mengelola";
    }

    @GetMapping(value = "/tambah")
    public String addDivisiForm(Model model) {
        permissionConfigService.checkPermission("Tambah Divisi");

        DivisiModel divisiModel = new DivisiModel();
        model.addAttribute("divisiModel", divisiModel);
        return "divisi-form-tambah";
    }

    @PostMapping(value = "/tambah")
    public String addDivisiSubmit(@ModelAttribute DivisiModel divisiModel) {
        divisiService.addDivisi(divisiModel);
        return "redirect:/divisi";
    }

    @GetMapping(value = "/ubah/{id}")
    public String editDivisiForm(Model model, @PathVariable(value = "id") Long id) {
        permissionConfigService.checkPermission("Ubah Divisi");

        DivisiModel divisiModel = divisiService.getDivisiByIdDivisi(id).get();
        model.addAttribute("divisiModel", divisiModel);
        return "divisi-form-ubah";
    }
}
