package propensi.GCG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propensi.GCG.model.DokumenArsipModel;
import propensi.GCG.model.PeriodeTahunModel;
import propensi.GCG.repository.DokumenArsipDB;
import propensi.GCG.repository.DokumenDB;
import propensi.GCG.service.LembarKerjaService;
import propensi.GCG.service.PeriodeTahunService;
import propensi.GCG.service.PermissionConfigService;
import propensi.GCG.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/periode")
@Controller
public class PeriodeTahunController {
    @Autowired
    private PeriodeTahunService periodeTahunService;

    @Autowired
    private LembarKerjaService lembarKerjaService;

    @Autowired
    private UserService userService;

    @Autowired
    private DokumenArsipDB dokumenArsipDB;

    @Autowired
    private DokumenDB dokumenDB;

    @Autowired
    private PermissionConfigService permissionConfigService;

    @GetMapping(value = "")
    public String lihatPeriode(Model model){
        permissionConfigService.checkPermission("Pengelolaan Periode");
        List<PeriodeTahunModel> listPeriode = periodeTahunService.getAllPeriode();
        List<PeriodeTahunModel> listEnded = new ArrayList<PeriodeTahunModel>();
        List<DokumenArsipModel> arsipList = dokumenArsipDB.findByDokumenRef("aoi");
        List<String> dokumen = new ArrayList<String>();
        for(PeriodeTahunModel periode : listPeriode){
            if(periode.isEnded()){
                listEnded.add(periode);
            }
        }
        if(arsipList.size() == 0){
            for(int i = 0; i < listPeriode.size(); i++){
                dokumen.add("#");
            }
        }
        for(DokumenArsipModel arsip : arsipList){
            dokumen.add(dokumenDB.findByArsipDokumen(arsip).getId());
        }
        if(!(dokumen.size() == listPeriode.size())){
            for(int i = 0; i <= listPeriode.size()-dokumen.size(); i++){
                dokumen.add("#");
            }
        }
        model.addAttribute("listEnded", listEnded);
        model.addAttribute("listPeriode", listPeriode);
        model.addAttribute("dokumen", dokumen);
        return "periode-lihat-semua";
    }

    @GetMapping(value = "/tambah")
    public String tambahPeriode(RedirectAttributes red, Model model){
        permissionConfigService.checkPermission("Tambah Periode");
        if(!periodeTahunService.cekPeriodeDiTutup()){
            red.addFlashAttribute("pesan", "Tidak bisa menambah periode karena ada periode yang belum ditutup");
            return "redirect:/periode";
        }
        PeriodeTahunModel periodeBaru = new PeriodeTahunModel();
        model.addAttribute("periode", periodeBaru);
        return "periode-tambah-form";
    }

    @PostMapping(value = "/tambah")
    public String submitTambahPeriode(@ModelAttribute PeriodeTahunModel periodeTahunModel,
                                      @RequestParam("inlineRadioOptions") String pilihan,
                                      Authentication auth, Model model){
        periodeTahunModel.setCreatorPeriode(userService.findUserByUserName(auth.getName()));
        periodeTahunService.addPeriode(periodeTahunModel);
        if(pilihan.equals("option1")) {
            lembarKerjaService.addLembarKerja(periodeTahunModel);
        }
        return "redirect:/periode";
    }
}
