package propensi.GCG.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import propensi.GCG.model.*;

import propensi.GCG.model.wrapper.DaftarLembarKerja;
import propensi.GCG.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value = "/lembarkerja")
@Controller
public class LembarKerjaController {

    @Autowired
    private LembarKerjaService lembarKerjaService;

    @Autowired
    private PeriodeTahunService periodeTahunService;

    @Autowired
    private SubmisiService submisiService;

    @Autowired
    private DelegasiService delegasiService;

    @Autowired
    private DivisiService divisiService;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionConfigService permissionConfigService;

    @RequestMapping("/")
    public String periodeAktif(Model model) {
        PeriodeTahunModel periodeAktif = periodeTahunService.getPeriodeBerjalan();
        if (periodeAktif == null){
            model.addAttribute("message","Saat ini tidak terdapat periode yang sedang berjalan");
            model.addAttribute("message","Saat ini tidak terdapat periode yang sedang berjalan");

            return "periodeNotFound";
        }
        return "redirect:/lembarkerja/" + periodeAktif.getTahun() + "/1";

    }


    @RequestMapping(value = "/{tahun}/{no_aspek}", method = RequestMethod.GET)
    private String TableLembarKerja(@PathVariable int tahun, @PathVariable int no_aspek, Model model){
        permissionConfigService.checkPermission("Melihat Lembar Kerja");

        UserModel user = userService.getCurrentUser();
        String role = user.getUserRole().getNama();
        PeriodeTahunModel periode = periodeTahunService.getPeriodeByTahun(tahun);
        LembarKerjaModel aspek = lembarKerjaService.getLembarKerja(periode, no_aspek);
        List<PeriodeTahunModel> listPeriodeAktif = periodeTahunService.getListPeriodeAktif();
        List<LembarKerjaModel>  listIndikator = new ArrayList<>();
        List<List<LembarKerjaModel>>  listPerspektif = new ArrayList<>();
        List<List<List<LembarKerjaModel>>>  listSubperspektif = new ArrayList<>();

        if (periode == null){
            return "periodeNotFound";
        }

        if (aspek == null){
            return "aspekNotFound";
        }

        if(role.equals("Auditor Internal") || role.equals("Admin") || role.equals("Auditor Eksternal")){

            listIndikator = lembarKerjaService.getListIndikatorByAspek(periode, aspek.getAspek());

            // getListPerspektif
            for (LembarKerjaModel indikator : listIndikator) {
                List<LembarKerjaModel> tempListPerspektif = lembarKerjaService.getListPerspektifByIndikator(periode,indikator.getIndikator());
                listPerspektif.add(tempListPerspektif);
            }


        }

        // Role lain selain auditor menampilkan perspektif hanya untuk yang didelegasikan.
        else{
            listIndikator = lembarKerjaService.getListIndikatorBerdasarkanDelegasiDanAspek(user, aspek, periode);
            DivisiModel divisi = user.getUserDivisi();

            List<DelegasiModel> listDelegasiDivsi = divisi.getDelegasiDivisiList();



            for (LembarKerjaModel indikator : listIndikator) {

                List<LembarKerjaModel> tempListPerspektif = lembarKerjaService.getListPerspektifByIndikator(periode,indikator.getIndikator());
                List<LembarKerjaModel> listPerspekttifUntukDisimpan = new ArrayList<>();
                listPerspekttifUntukDisimpan.addAll(tempListPerspektif);
                

                for (LembarKerjaModel perspektifTemp : tempListPerspektif){
                    if(perspektifTemp != null){
                        System.out.println("perspektifTemp : " + perspektifTemp.getId());
                    }
                    List<DelegasiModel> listDelegasiPerspektif = perspektifTemp.getDelegasiLembarKerjaList();
                    List<DelegasiModel> persamaanDelegasiPadaPerspektifDanDivisi = listDelegasiDivsi.stream()
                            .distinct()
                            .filter(listDelegasiPerspektif::contains)
                            .collect(Collectors.toList());
                    if (persamaanDelegasiPadaPerspektifDanDivisi.isEmpty()){
                        listPerspekttifUntukDisimpan.remove(perspektifTemp);
                    }
                }
                listPerspektif.add(listPerspekttifUntukDisimpan);
            }



        }

        // getListSubperspektif
        for (List<LembarKerjaModel> listSubperspektifByIndikator : listPerspektif) {
            List<List<LembarKerjaModel>> subperspektifByIndikator = new ArrayList<>();
            for(LembarKerjaModel perspektif : listSubperspektifByIndikator){
                List<LembarKerjaModel> listSubTemp = lembarKerjaService.getListSubperspektifByPerspektif(periode, perspektif.getPerspektif());
                subperspektifByIndikator.add(listSubTemp);
            }
            listSubperspektif.add(subperspektifByIndikator);
        }
        model.addAttribute("listPeriode", listPeriodeAktif);
        model.addAttribute("periode", periode);
        model.addAttribute("tahun", tahun);
        model.addAttribute("role", role);
        model.addAttribute("aspek", aspek);
        model.addAttribute("listIndikator", listIndikator);
        model.addAttribute("listPerspektif", listPerspektif);
        model.addAttribute("listSubperspektif", listSubperspektif);

        return "lembarkerja";
    }

    @RequestMapping(value = "/{tahun}/penilaian/{idSubperspektif}", method = RequestMethod.GET)
    private String PenilaianDokumen(@PathVariable long idSubperspektif,@PathVariable int tahun,  Model model){
        permissionConfigService.checkPermission("Melakukan Penilaian");

        DaftarLembarKerja daftarLembarKerjaUntukDinilai = new DaftarLembarKerja();
        daftarLembarKerjaUntukDinilai.setDaftarLembarKerja(new ArrayList<LembarKerjaModel>());

        PeriodeTahunModel periode = periodeTahunService.getPeriodeByTahun(tahun);
        LembarKerjaModel subperspektif = lembarKerjaService.getSubperspektifById(idSubperspektif);

        List<LembarKerjaModel> listSubfaktoruji  = lembarKerjaService.getListSubfaktorUjiBySubperspektif(subperspektif.getPeriodeLembarKerja(), subperspektif.getSubperspektif());
        SubmisiModel submisi = submisiService.getAktifSubmisiBySubperspektif(subperspektif);

        System.out.println("subperspektif : " + subperspektif);
        System.out.println("daftarLembarKerja : " + daftarLembarKerjaUntukDinilai.getDaftarLembarKerja());

        if (listSubfaktoruji.isEmpty()){
            daftarLembarKerjaUntukDinilai.addLembarKerja(subperspektif);
        }
        else {
            daftarLembarKerjaUntukDinilai.setDaftarLembarKerja(listSubfaktoruji);
        }
        model.addAttribute("daftarLembarKerjaUntukDinilai", daftarLembarKerjaUntukDinilai);
        model.addAttribute("tahun", periode.getTahun());
        model.addAttribute("submisi", submisi);
        model.addAttribute("listsubfaktoruji", listSubfaktoruji);
        model.addAttribute("subperspektif",subperspektif);
        return "form-penilaian";
    }

    //POST untuk subperspektif
    @RequestMapping(value = "/{tahun}/penilaian", method = RequestMethod.POST, params = "penilaian=subperspektif")
    private String PenilaianSubperspektifSubmit(@PathVariable int tahun, @ModelAttribute DaftarLembarKerja daftarLembarKerjaUntukDinilai, Model model){

        List<LembarKerjaModel> daftarPenilaianYangDinilai = daftarLembarKerjaUntukDinilai.getDaftarLembarKerja();
        LembarKerjaModel subperspektifInput = daftarPenilaianYangDinilai.get(0);

        Double nilaiInput = subperspektifInput.getNilai();
        if (subperspektifInput.getNilai() == 0.01) {
            subperspektifInput.setNilai(null);
        }

        LembarKerjaModel subperspektifDinilai = lembarKerjaService.createNilai(subperspektifInput);

        SubmisiModel submisiSebelumDinilai = submisiService.getAktifSubmisiBySubperspektif(subperspektifDinilai);
        SubmisiModel submisiSetelahDinilai = submisiService.createNilai(submisiSebelumDinilai, subperspektifDinilai);

        LembarKerjaModel perspektif = subperspektifDinilai.getLembarKerjaParent();
        LembarKerjaModel indikator = perspektif.getLembarKerjaParent();
        LembarKerjaModel aspek = indikator.getLembarKerjaParent();

        perspektif = lembarKerjaService.perbaharuiNilai(perspektif);
        indikator = lembarKerjaService.perbaharuiNilai(indikator);
        aspek = lembarKerjaService.perbaharuiNilai(aspek);

        System.out.println( submisiService.getAktifSubmisiBySubperspektif(subperspektifDinilai).getId()+ " : submisi id");

        model.addAttribute("submisi", submisiSetelahDinilai);
        model.addAttribute("tahun", tahun);


        //return
        if (subperspektifDinilai.getNilai() == null){
            return "form-penilaian-feedback";
        }

        else if (subperspektifDinilai.getNilai() == 1){
            String url = "redirect:/lembarkerja/" + tahun + "/1";
            return url;
        }

        else{
            return "form-penilaian-feedback";
        }

    };

    // POST ketika ada penilaian ada subfaktoruji
    @RequestMapping(value = "/{tahun}/penilaian", method = RequestMethod.POST, params = "penilaian=subfaktoruji")
    private String PenilaianSubfaktorujiSubmit(@PathVariable int tahun, @ModelAttribute DaftarLembarKerja daftarLembarKerjaUntukDinilai, Model model){

        List<LembarKerjaModel> daftarPenilaianYangDinilai = daftarLembarKerjaUntukDinilai.getDaftarLembarKerja();
        LembarKerjaModel subfaktor = lembarKerjaService.getSubperspektifById(daftarPenilaianYangDinilai.get(0).getId());

        LembarKerjaModel subperspektif = subfaktor.getLembarKerjaParent();


        for (LembarKerjaModel subfaktoruji : daftarPenilaianYangDinilai){
            if (subfaktoruji.getNilai() == 0.01){
                subfaktoruji.setNilai(null);
            }
            LembarKerjaModel subfaktorSetelahDinilai = lembarKerjaService.createNilai(subfaktoruji);
        }

        LembarKerjaModel perspektif = subperspektif.getLembarKerjaParent();
        LembarKerjaModel indikator = perspektif.getLembarKerjaParent();
        LembarKerjaModel aspek = indikator.getLembarKerjaParent();

        subperspektif = lembarKerjaService.perbaharuiNilai(subperspektif);
        perspektif = lembarKerjaService.perbaharuiNilai(perspektif);
        indikator = lembarKerjaService.perbaharuiNilai(indikator);
        aspek = lembarKerjaService.perbaharuiNilai(aspek);

        SubmisiModel submisiSebelumDinilai = submisiService.getAktifSubmisiBySubperspektif(subperspektif);
        SubmisiModel submisiSetelahDinilai = submisiService.createNilai(submisiSebelumDinilai, subperspektif);

        model.addAttribute("submisi",submisiSetelahDinilai);

        if (subperspektif.getNilai()==1){
            return "redirect:/lembarkerja/2019/1";
        }
        else {
            return "form-penilaian-feedback";
        }


    };

    @RequestMapping(value = "/{tahun}/penilaian/feedback", method = RequestMethod.POST)
    private String FeedbackPenilaianDokumen(@PathVariable int tahun, @ModelAttribute SubmisiModel submisi, Model model){

        System.out.println("submisi id feedback : " + submisi.getId());

        System.out.println("submisi kelebihan : " + submisi.getKelebihan());
        SubmisiModel submisiDinilai = submisiService.simpanFeedback(submisi);

        model.addAttribute("statusModal","active");
        return "form-penilaian-sukses";
    }

    @RequestMapping(value = "/{tahun}/AOI/{no_aspek}", method = RequestMethod.GET)
    private String TableAOIPeriodeBerjalan(@PathVariable int tahun, @PathVariable int no_aspek, Model model){
        UserModel user = userService.getCurrentUser();
        String role = user.getUserRole().getNama();
        PeriodeTahunModel periode = periodeTahunService.getPeriodeByTahun(tahun);
        LembarKerjaModel aspek = lembarKerjaService.getLembarKerja(periode, no_aspek);
        List<LembarKerjaModel> listAspek = lembarKerjaService.getListAspek(periode);
        List<LembarKerjaModel>  listIndikator = new ArrayList<>();
        List<List<LembarKerjaModel>>  listPerspektif = new ArrayList<>();
        List<List<List<LembarKerjaModel>>>  listSubperspektif = new ArrayList<>();

        if (periode == null){
            return "periodeNotFound";
        }

        if (aspek == null){
            return "aspekNotFound";
        }

        listIndikator = lembarKerjaService.getListIndikatorByAspek(periode, aspek.getAspek());
        List<LembarKerjaModel> listIndikatorCopy = new ArrayList<>();

        listIndikatorCopy.addAll(listIndikator);

        for (LembarKerjaModel indikator : listIndikatorCopy){
            List<LembarKerjaModel> listPerspektifTemp = lembarKerjaService.getListPerspektifByIndikator(periode,indikator.getIndikator());
            List<LembarKerjaModel> listPerspektifUntukDisimpan = new ArrayList<>();
            List<List<LembarKerjaModel>> listSubperspektifUntukDisimpan = new ArrayList<>();

            listPerspektifUntukDisimpan.addAll(listPerspektifTemp);

            for (LembarKerjaModel perspektifTemp : listPerspektifTemp){
                List<LembarKerjaModel> listSubperspektifTemp = lembarKerjaService.getListSubperspektifByPerspektif(periode, perspektifTemp.getPerspektif());
                List<LembarKerjaModel> listSubperspektifTempCopy = new ArrayList<>();

                listSubperspektifTempCopy.addAll(listSubperspektifTemp);

                for (LembarKerjaModel subperspektif : listSubperspektifTemp) {
                    List<SubmisiModel> listSubmisi = subperspektif.getSubmisiLembarKerjaList();
                    if (listSubmisi.isEmpty()){
                        System.out.println("submisi null");
                        listSubperspektifTempCopy.remove(subperspektif);
                    }
                }
                if (listSubperspektifTempCopy.isEmpty()){
                    listPerspektifUntukDisimpan.remove(perspektifTemp);
                }
                else {
                    listSubperspektifUntukDisimpan.add(listSubperspektifTempCopy);
                }


            }
            if (listPerspektifUntukDisimpan.isEmpty()){
                listIndikator.remove(indikator);
            }
            else {
                listPerspektif.add(listPerspektifUntukDisimpan);
                listSubperspektif.add(listSubperspektifUntukDisimpan);
            }
        }



        model.addAttribute("periode", periode);
        model.addAttribute("tahun", tahun);
        model.addAttribute("role", role);
        model.addAttribute("aspek", aspek);
        model.addAttribute("listAspek",listAspek);
        model.addAttribute("listIndikator", listIndikator);
        model.addAttribute("listPerspektif", listPerspektif);
        model.addAttribute("listSubperspektif", listSubperspektif);
        return "daftarAOI";
    }
}
