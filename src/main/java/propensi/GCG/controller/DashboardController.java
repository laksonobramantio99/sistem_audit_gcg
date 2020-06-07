package propensi.GCG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.PeriodeTahunModel;
import propensi.GCG.service.LembarKerjaService;
import propensi.GCG.service.PeriodeTahunService;
import propensi.GCG.service.PermissionConfigService;
import propensi.GCG.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/dashboard")
@Controller
public class DashboardController {

    @Autowired
    private LembarKerjaService lembarKerjaService;

    @Autowired
    private UserService userService;

    @Autowired
    private PeriodeTahunService periodeTahunService;

    @Autowired
    private PermissionConfigService permissionConfigService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String viewDashboard(Model model){
        permissionConfigService.checkPermission("Melihat Dashboard");

        PeriodeTahunModel periodeBerjalan = periodeTahunService.getPeriodeBerjalan();
        List<LembarKerjaModel> listAspekPeriodeBerjalan = lembarKerjaService.getListAspek(periodeBerjalan);
        List<Double> listPersentasePenyelesaianDokumen = new ArrayList<>();
        List<Integer> listJumlahSubperspektifDiselesaikanPerAspek = new ArrayList<>();
        List<Integer> listJumlahSubperspektifPerAspek = new ArrayList<>();

        int totalSubperspektifPeriodeBerjalan = 0;
        int totalSubperspektifDiselesaikan = 0;

        for (LembarKerjaModel aspek : listAspekPeriodeBerjalan){
            if (aspek.getNilai() == null){
                aspek.setNilai((double) 0);
            }
            if (aspek.getTertimbang() == null){
                aspek.setTertimbang((double) 0);
            }
            // getPersentase.. = [jumlah subperspektif dilengkapi, jumlah semua subperspektif, persentase]
            List<Double> listInformasiPenyelesaianDokumen = lembarKerjaService.getPersentaseSubperspektifTanpaDokumen(aspek);
            Double persentase = listInformasiPenyelesaianDokumen.get(2);

            listPersentasePenyelesaianDokumen.add(persentase);
            listJumlahSubperspektifDiselesaikanPerAspek.add(listInformasiPenyelesaianDokumen.get(0).intValue());
            listJumlahSubperspektifPerAspek.add(listInformasiPenyelesaianDokumen.get(1).intValue());

            totalSubperspektifPeriodeBerjalan+=listInformasiPenyelesaianDokumen.get(1);
            totalSubperspektifDiselesaikan += listInformasiPenyelesaianDokumen.get(0);
        }
        Double persentaseKelengkapanDokumen = (double) totalSubperspektifDiselesaikan/ (double) totalSubperspektifPeriodeBerjalan;

        System.out.println("dokumen selesai : " + totalSubperspektifDiselesaikan);
        System.out.println("dokumen semua : " + totalSubperspektifPeriodeBerjalan);

        List<PeriodeTahunModel> listPeriodeBerakhir = periodeTahunService.getListPeriodeBerakhir();
        List<PeriodeTahunModel> listPeriodeAktif = periodeTahunService.getListPeriodeAktif();

        List<List<LembarKerjaModel>> listAspekPeriode = new ArrayList<>();



        for (PeriodeTahunModel periode : listPeriodeBerakhir){
            List<LembarKerjaModel> listAspek = lembarKerjaService.getListAspek(periode);
            listAspekPeriode.add(listAspek);

        }

        model.addAttribute("listJumlahSubperspektifPerAspek",listJumlahSubperspektifPerAspek);
        model.addAttribute("listJumlahSubperspektifDiselesaikanPerAspek",listJumlahSubperspektifDiselesaikanPerAspek);
        model.addAttribute("listPeriodeAktif",listPeriodeAktif);
        model.addAttribute("listPeriodeBerakhir", listPeriodeBerakhir);
        model.addAttribute("listAspekByPeriode", listAspekPeriode);
        model.addAttribute("totalSubperspektifPeriode", totalSubperspektifPeriodeBerjalan);
        model.addAttribute("totalSubperspektifDiselesaikan", totalSubperspektifDiselesaikan);
            model.addAttribute("persentaseKelengkapanDokumen", persentaseKelengkapanDokumen);
        model.addAttribute("listPersentasePenyelesaianAspek", listPersentasePenyelesaianDokumen);
        model.addAttribute("listAspekPeriodeBerjalan",listAspekPeriodeBerjalan);
        model.addAttribute("periodeBerjalan",periodeBerjalan);

        return "dashboard";
    }

}
