package propensi.GCG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import propensi.GCG.model.*;
import propensi.GCG.repository.DokumenArsipDB;
import propensi.GCG.repository.DokumenDB;
import propensi.GCG.service.DokumenArsipService;
import propensi.GCG.service.DokumenService;
import propensi.GCG.service.PeriodeTahunService;
import propensi.GCG.service.PermissionConfigService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/arsip")
public class DokumenArsipController {
    @Autowired
    PeriodeTahunService periodeTahunService;

    @Autowired
    DokumenArsipService dokumenArsipService;

    @Autowired
    DokumenService dokumenService;

    @Autowired
    private DokumenDB dokumenDB;

    @Autowired
    private DokumenArsipDB dokumenArsipDB;

    @Autowired
    private PermissionConfigService permissionConfigService;

    @RequestMapping(value = "/upload/{id}", method = RequestMethod.GET)
    private String subperspektifUploadDok(@PathVariable long id, Model model){
        PeriodeTahunModel periode = periodeTahunService.getPeriodeById(id);
//        DokumenArsipModel arsip = dokumenArsipService.getByPeriode(periode);
        List<PeriodeTahunModel> listPeriode = periodeTahunService.getAllPeriode();
        List<DokumenArsipModel> listArsip = dokumenArsipDB.findByDokumenRef("aoi");
        DokumenArsipModel dokumenArsipModel = null;
        if(listPeriode.size() != listArsip.size()) {
            dokumenArsipModel = new DokumenArsipModel();
        }
        else{
            for(int i = 0; i < listPeriode.size(); i++){
                if(listPeriode.get(i).getTahun() == periode.getTahun()){
                    dokumenArsipModel = listArsip.get(i);
                    break;
                }
            }
        }
        model.addAttribute("arsip", dokumenArsipModel);
        model.addAttribute("periode", periode);
        return "periode-upload-form";
    }

    @GetMapping("")
    public String allDokumen(Model model) {
        permissionConfigService.checkPermission("Melihat Dokumen Arsip");
        List<DokumenArsipModel> listDokumenArsip = dokumenArsipService.getAllDokumenArsip();

        List<Integer> listTahunDokumen = new ArrayList<>();
        for (DokumenArsipModel arsip : listDokumenArsip) {
            if (!listTahunDokumen.contains(arsip.getTahun())) {
                listTahunDokumen.add(arsip.getTahun());
            }
        }

        Collections.sort(listTahunDokumen);

        List<List<DokumenArsipModel>> listDokumenArsipPerTahun = new ArrayList<>();
        for (Integer tahun : listTahunDokumen) {
            List<DokumenArsipModel> tempListDokumenArsip = new ArrayList<>();
            for (DokumenArsipModel arsip : listDokumenArsip) {
                if (arsip.getTahun() == tahun) {
                    tempListDokumenArsip.add(arsip);
                }
            } listDokumenArsipPerTahun.add(tempListDokumenArsip);
        }

        model.addAttribute("listDokumenArsip", listDokumenArsipPerTahun);
        return "dokumenArsip-lihat-semua";
    }

    @GetMapping(value = "/tambah")
    public String addDokumenFormPage(Model model) {
        permissionConfigService.checkPermission("Menambah Dokumen Arsip");
        PeriodeTahunModel periode = periodeTahunService.getPeriodeAktif();
        DokumenArsipModel dokumenArsipBaru = new DokumenArsipModel();

        model.addAttribute("periode", periode);
        model.addAttribute("dokumenArsipBaru", dokumenArsipBaru);
        return "dokumenArsip-tambah-form";
    }

    @PostMapping(value = "/hapus-arsip/{idArsip}")
    public String deleteDokumenArsip(@PathVariable (value = "idArsip") long idArsip) {
        DokumenArsipModel dokumenArsip = dokumenArsipService.getById(idArsip);
        dokumenService.deleteDokumen(dokumenArsip);
        dokumenArsipService.deleteDokumenArsip(dokumenArsip);
        return "redirect:/arsip";
    }
}
