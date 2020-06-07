package propensi.GCG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propensi.GCG.model.DelegasiModel;
import propensi.GCG.model.DivisiModel;
import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.PeriodeTahunModel;
import propensi.GCG.service.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/delegasi")
public class DelegasiController {
    @Autowired
    private UserService userService;

    @Autowired
    private DelegasiService delegasiService;

    @Autowired
    private DivisiService divisiService;

    @Autowired
    private PeriodeTahunService periodeTahunService;

    @Autowired
    private LembarKerjaService lembarKerjaService;

    @Autowired
    private PermissionConfigService permissionConfigService;

    @GetMapping(value = "/tambah")
    public String addDelegasiFormPage(Model model) {
        permissionConfigService.checkPermission("Menambah Delegasi");
        DelegasiModel newDelegasi = new DelegasiModel();
        DivisiModel divisiModel = new DivisiModel();
        newDelegasi.setDelegasiDivisi(divisiModel);
        PeriodeTahunModel periode = periodeTahunService.getPeriodeBerjalan();

        List<DivisiModel> listDivisi = divisiService.getDivisiList();
        List<DivisiModel> listDivisiWithoutDelegasi = new ArrayList<>();
        for (DivisiModel divisi : listDivisi) {
            List<DelegasiModel> existingListDelegasi = delegasiService.getDelegasiByDivisi(divisi);
            List<DelegasiModel> listDelegasi = new ArrayList<>();
            for (DelegasiModel delegasi : existingListDelegasi) {
                if (delegasi.getDelegasiLembarKerja().getPeriodeLembarKerja().getTahun() == periode.getTahun()) {
                    listDelegasi.add(delegasi);
                }
            }
            if (listDelegasi.size() == 0) {
                listDivisiWithoutDelegasi.add(divisi);
            }
        }

        List<LembarKerjaModel> listAspek = lembarKerjaService.getListAspek(periode);

        if (listAspek.size() == 0) {
            return "perspektifNotFound";
        }

        List<List<LembarKerjaModel>> listPerspektif = new ArrayList<>();
        for (LembarKerjaModel aspek : listAspek) {
            List<LembarKerjaModel> tempListPerspektif = lembarKerjaService.getListPerspektifByAspek(periode, aspek.getAspek());
            listPerspektif.add(tempListPerspektif);
        }

        model.addAttribute("listDivisi", listDivisiWithoutDelegasi);
        model.addAttribute("listPerspektif", listPerspektif);
        model.addAttribute("listAspek", listAspek);
        model.addAttribute("divisiModel", divisiModel);
        model.addAttribute("delegasi", newDelegasi);
        model.addAttribute("tahun", periode.getTahun());

        return "delegasi-tambah-form";
    }

    @GetMapping("")
    public String allDelegasi(Model model) {
        permissionConfigService.checkPermission("Melihat Daftar Delegasi");

        List<DivisiModel> listDivisi = divisiService.getDivisiList();
        PeriodeTahunModel periode = periodeTahunService.getPeriodeAktif();

        if (periode == null) {
            model.addAttribute("message","Saat ini tidak terdapat periode yang sedang berjalan");
            return "periodeNotFound";
        }

        if (periode.isEnded() == true) {
            model.addAttribute("message","Saat ini tidak terdapat periode yang sedang berjalan");
            return "periodeNotFound";
        }

        List<DelegasiModel> listAllDelegasi = delegasiService.getAllDelegasi();
        List<DelegasiModel> listDelegasiPerPeriode = delegasiService.getDelegasiByPeriode(listAllDelegasi, periode);

        List<List<DelegasiModel>> listDelegasiPerDivisi = new ArrayList<>();
        for (DivisiModel divisi : listDivisi) {
            List<DelegasiModel> tempListDelegasi = new ArrayList<>();
            for (DelegasiModel delegasi : listDelegasiPerPeriode) {
                if (delegasi.getDelegasiDivisi().getId() == divisi.getId()) {
                    tempListDelegasi.add(delegasi);
                }
            } listDelegasiPerDivisi.add(tempListDelegasi);
        }

        model.addAttribute("listDivisi", listDivisi);
        model.addAttribute("listDelegasi", listDelegasiPerDivisi);
        model.addAttribute("tahun", periode.getTahun());
        return "delegasi-lihat-semua";
    }

    @PostMapping(value = "/tambah")
    public String addDelegasiSubmit(@ModelAttribute DelegasiModel delegasiModel, @RequestParam List<Long> lembarKerjaCheckbox) {
        for (Long idFixedLK : lembarKerjaCheckbox) {
            DelegasiModel delegasi = new DelegasiModel();
            delegasi.setDelegasiDivisi(delegasiModel.getDelegasiDivisi());
            delegasi.setActiveFlag(true);
            LembarKerjaModel lembarKerja = lembarKerjaService.getLembarKerjaByIdFixed(idFixedLK).get();
            delegasi.setDelegasiLembarKerja(lembarKerja);
            delegasiService.addDelegasi(delegasi);
        }

        return "delegasi-lihat-semua";
    }

    @GetMapping(value = "/lihat")
    public String viewDelegasi(@RequestParam(value = "id") Long id, Model model) {
        permissionConfigService.checkPermission("Lihat Detail Delegasi Perspektif");
        DivisiModel existingDivisi = divisiService.getDivisiByIdDivisi(id).get();
        List<DelegasiModel> existingListDelegasi = existingDivisi.getDelegasiDivisiList();

        PeriodeTahunModel periode = periodeTahunService.getPeriodeBerjalan();
        List<LembarKerjaModel> listAspek = lembarKerjaService.getListAspek(periode);
        List<DelegasiModel> listDelegasi = delegasiService.getDelegasiByPeriode(existingListDelegasi, periode);

        List<LembarKerjaModel> newListAspek = new ArrayList<>();
        for (LembarKerjaModel aspek : listAspek) {
            for (DelegasiModel delegasi : listDelegasi) {
                if (aspek.getAspek().equals(delegasi.getDelegasiLembarKerja().getAspek()) && !newListAspek.contains(aspek)) {
                    newListAspek.add(aspek);
                }
            }
        }

        List<List<LembarKerjaModel>> listPerspektif = new ArrayList<>();
        for (LembarKerjaModel aspek : newListAspek) {
            List<LembarKerjaModel> tempListPerspektif = lembarKerjaService.getListPerspektifByAspek(periode, aspek.getAspek());
            List<LembarKerjaModel> tempListDelegasi = new ArrayList<>();
            for (LembarKerjaModel perspektif : tempListPerspektif) {
                for (DelegasiModel delegasi : listDelegasi) {
                    if (perspektif.getPerspektif().equals(delegasi.getDelegasiLembarKerja().getPerspektif())) {
                        tempListDelegasi.add(perspektif);
                    }
                }
            } listPerspektif.add(tempListDelegasi);
        }

        model.addAttribute("divisi", existingDivisi);
        model.addAttribute("listDelegasi", listPerspektif);
        model.addAttribute("listAspek", newListAspek);
        model.addAttribute("tahun", periode.getTahun());
        return "delegasi-lihat-detail";
    }

    @GetMapping(value = "/ubah")
    public String changeDelegasiFormPage(@RequestParam(value = "id") Long id, Model model) {
        permissionConfigService.checkPermission("Ubah Delegasi");
        DivisiModel existingDivisi = divisiService.getDivisiByIdDivisi(id).get();
        List<DelegasiModel> existingListDelegasi = existingDivisi.getDelegasiDivisiList();

        PeriodeTahunModel periode = periodeTahunService.getPeriodeBerjalan();
        List<DelegasiModel> listDelegasi = delegasiService.getDelegasiByPeriode(existingListDelegasi, periode);

        if (listDelegasi.size() == 0) {
            return "delegasiCantEdit";
        }

        List<LembarKerjaModel> listDelegasiPerspektif = new ArrayList<>();
        for (DelegasiModel delegasi : listDelegasi) {
            listDelegasiPerspektif.add(delegasi.getDelegasiLembarKerja());
        }

        List<LembarKerjaModel> listAspek = lembarKerjaService.getListAspek(periode);

        List<List<LembarKerjaModel>> listAllPerspektif = new ArrayList<>();
        for (LembarKerjaModel aspek : listAspek) {
            List<LembarKerjaModel> tempListPerspektif = lembarKerjaService.getListPerspektifByAspek(periode, aspek.getAspek());
            listAllPerspektif.add(tempListPerspektif);
        }

        List<List<List<?>>>  listAddedPerspektif = new ArrayList<>();
        for (List<LembarKerjaModel> listPerspektif : listAllPerspektif) {
            List<List<?>> tempList = new ArrayList<List<?>>();
            for (LembarKerjaModel perspektif : listPerspektif) {
                List tempList2 = new ArrayList();
                boolean value;
                tempList2.add(perspektif);
                if (listDelegasiPerspektif.contains(perspektif)) {
                    value = true;
                    tempList2.add(value);
                } else {
                    value = false;
                    tempList2.add(value);
                }
                tempList.add(tempList2);
            }
            listAddedPerspektif.add(tempList);
        }

        model.addAttribute("divisi", existingDivisi);
        model.addAttribute("listPerspektif", listAddedPerspektif);
        model.addAttribute("listAspek", listAspek);
        model.addAttribute("listDelegasi", listDelegasi);
        model.addAttribute("tahun", periode.getTahun());

        return "delegasi-ubah-form";
    }

    @PostMapping(value = "/ubah")
    public String changeDelegasiFormSubmit(@RequestParam(value = "id") Long id,
                                           @ModelAttribute DelegasiModel delegasiModel,
                                           @ModelAttribute DivisiModel divisiModel,
                                           @RequestParam List<Long> lembarKerjaCheckbox) {
        List<DelegasiModel> existingListDelegasi = delegasiService.getDelegasiByDivisi(divisiModel);
        PeriodeTahunModel periode = periodeTahunService.getPeriodeBerjalan();
        List<DelegasiModel> listDelegasi = delegasiService.getDelegasiByPeriode(existingListDelegasi, periode);

        for (DelegasiModel delegasi : listDelegasi) {
            delegasiService.deleteDelegasi(delegasi);
        }

        for (Long idFixedLK : lembarKerjaCheckbox) {
            DelegasiModel newDelegasi = new DelegasiModel();
            newDelegasi.setDelegasiDivisi(divisiModel);
            newDelegasi.setActiveFlag(true);
            LembarKerjaModel lembarKerja = lembarKerjaService.getLembarKerjaByIdFixed(idFixedLK).get();
            newDelegasi.setDelegasiLembarKerja(lembarKerja);
            delegasiService.addDelegasi(newDelegasi);
        }

        return "delegasi-lihat-semua";
    }

    @PostMapping(value = "")
    public String submitAktivasi(@RequestParam(value = "id") Long id,
                                 @ModelAttribute DivisiModel divisiModel,
                                 @RequestParam(value = "perintah") String perintah,
                                 RedirectAttributes red, Model model) {
        DivisiModel divisi = divisiService.getDivisiByIdDivisi(id).get();
        List<DelegasiModel> existingListDelegasi = delegasiService.getDelegasiByDivisi(divisiModel);
        PeriodeTahunModel periode = periodeTahunService.getPeriodeBerjalan();
        List<DelegasiModel> listDelegasi = delegasiService.getDelegasiByPeriode(existingListDelegasi, periode);

        boolean activeFlag = listDelegasi.get(0).isActiveFlag();

        if (perintah.equals("aktif")) {
            delegasiService.deactivatedDelegasi(listDelegasi, activeFlag);
        }

        model.addAttribute("divisi", divisi);
        return "redirect:/delegasi";
    }
}
