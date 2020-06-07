package propensi.GCG.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import propensi.GCG.model.*;
import propensi.GCG.service.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/submisi")
@Controller
public class SubmisiController {
    @Autowired
    private UserService userService;

    @Autowired
    private PeriodeTahunService periodeTahunService;

    @Autowired
    private LembarKerjaService lembarKerjaService;

    @Autowired
    private DelegasiService delegasiService;

    @Autowired
    private SubmisiService submisiService;

    @Autowired
    private DokumenService dokumenService;

    @Autowired
    private PermissionConfigService permissionConfigService;

    @RequestMapping(value = "/{tahun}/upload/{idSubperspektif}", method = RequestMethod.GET)
    private String subperspektifUploadDok(@PathVariable long idSubperspektif, @PathVariable int tahun,  Model model){
        permissionConfigService.checkPermission("Upload Dokumen");

        PeriodeTahunModel periode = periodeTahunService.getPeriodeByTahun(tahun);

        LembarKerjaModel subperspektif = lembarKerjaService.getSubperspektifById(idSubperspektif);

        LembarKerjaModel perspektif = lembarKerjaService.getSubperspektifById(idSubperspektif).getLembarKerjaParent();

        List<DelegasiModel> delegasiList = perspektif.getDelegasiLembarKerjaList();
        List<DivisiModel> divisiList = new ArrayList<>();
        for (DelegasiModel d : delegasiList) {
            if (!divisiList.contains(d.getDelegasiDivisi())) {
                divisiList.add(d.getDelegasiDivisi());
            }
        }

        List<LembarKerjaModel> listSubfaktoruji  = lembarKerjaService.getListSubfaktorUjiBySubperspektif(subperspektif.getPeriodeLembarKerja(), subperspektif.getSubperspektif());
        model.addAttribute("listsubfaktoruji", listSubfaktoruji);
        model.addAttribute("periode", periode.getTahun());
        model.addAttribute("subperspektif",subperspektif);
        model.addAttribute("divisiDelegasi", divisiList);
        return "dokumen-upload-form";
    }


    @RequestMapping(value = "/submisi/tambah", method = RequestMethod.POST)
    private String submitSubmisi(@ModelAttribute SubmisiModel submisiModel, Model model) {
        submisiModel.setActiveFlag(true);

        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String newDate = date.format(formatDate);
        submisiModel.setUploadDate(date);

        UserModel userByName = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        submisiModel.setUploaderSubmisi(userByName);

        model.addAttribute(newDate);
        return "lembarkerja";
    }

    @RequestMapping(value = "/{tahun}/riwayat/{idSubperspektif}", method = RequestMethod.GET)
    private String riwayatSubmisi(@ModelAttribute SubmisiModel submisiModel,@ModelAttribute DokumenModel dokumenModel, @PathVariable int tahun,
                                  @PathVariable long idSubperspektif, Model model){
        permissionConfigService.checkPermission("Lihat Riwayat Submisi");
        PeriodeTahunModel periode = periodeTahunService.getPeriodeByTahun(tahun);

        LembarKerjaModel subperspektif = lembarKerjaService.getSubperspektifById(idSubperspektif);

        SubmisiModel submisi = submisiService.getAktifSubmisiBySubperspektif(subperspektif);

        //DokumenModel namaDok = dokumenService.getFileName(fileName);
        //nama dokumen
        List <DokumenModel> listFile = submisi.getDokumenList();
        List <String> namaFileSubmisi = new ArrayList<>();
        //id lembar kerja == id subperspektif

        for (DokumenModel d : listFile) {
            if (submisi.getSubmisiLembarKerja().equals(idSubperspektif)) {
                namaFileSubmisi.add(d.getFileName());
            }
        }
        List <SubmisiModel> subs = subperspektif.getSubmisiLembarKerjaList();
        List <DokumenModel> dok = submisi.getDokumenList();

        LocalDateTime uploadDate = submisi.getUploadDate();
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
        String newDate = uploadDate.format(formatDate);

        //komentar
        String komentar = submisi.getKomentar();
        if (submisi.getKomentar() == null || submisi.getKomentar() == ""){
            komentar = "Tidak ada komentar dari penanggung jawab";
        }


        //aoi
        String kelebihan = submisi.getKelebihan();
        if (submisi.getKelebihan() == null || submisi.getKelebihan() == ""){
            kelebihan = "Tidak ada kelebihan";
        }
        String kekurangan = submisi.getKekurangan();
        if (submisi.getKekurangan() == null || submisi.getKekurangan() == ""){
            kekurangan = "Tidak ada kekurangan";
        }
        String referensi = submisi.getReferensi();
        if (submisi.getReferensi() == null || submisi.getReferensi() == ""){
            referensi = "Tidak ada referensi";
        }
        String rekomendasi = submisi.getRekomendasi();
        if (submisi.getRekomendasi() == null || submisi.getRekomendasi() == ""){
            rekomendasi = "Tidak ada rekomendasi";
        }
        String hambatan = submisi.getHambatan();
        if (submisi.getHambatan()== null || submisi.getHambatan() == ""){
            hambatan = "Tidak ada hambatan";
        }

        model.addAttribute("subperspektif", subperspektif);
        model.addAttribute("submisi", submisi);
        model.addAttribute("namaFileSubmisi", namaFileSubmisi);
        model.addAttribute("subs", subs);
        model.addAttribute("dok", dok);
        model.addAttribute("newDate", newDate);
        model.addAttribute("komentar", komentar);
        model.addAttribute("hambatan", hambatan);
        model.addAttribute("kekurangan", kekurangan);
        model.addAttribute("kelebihan", kelebihan);
        model.addAttribute("referensi", referensi);
        model.addAttribute("rekomendasi", rekomendasi);

        return "list-dokumen-riwayat";
    }

    @RequestMapping(value = "/{tahun}/lihat/{idSubperspektif}", method = RequestMethod.GET)
    public String editSubmisiDokumen(@ModelAttribute SubmisiModel submisiModel,  @PathVariable long idSubperspektif ,@PathVariable int tahun, Model model){
        permissionConfigService.checkPermission("Lihat Dokumen");

        LembarKerjaModel subperspektif = lembarKerjaService.getSubperspektifById(idSubperspektif);
        List<LembarKerjaModel> listSubfaktoruji  = lembarKerjaService.getListSubfaktorUjiBySubperspektif(subperspektif.getPeriodeLembarKerja(), subperspektif.getSubperspektif());

        //DokumenService file = dokumenService.getFile();

        //mau ambil status penilaian
        SubmisiModel submisi = submisiService.getAktifSubmisiBySubperspektif(subperspektif);
        String statusPenilaian = "Sudah dinilai";
        if (submisi.getNilai() == null){
            statusPenilaian = "Belum dinilai";
        }

        //waktu perubahan terakhir
        LocalDateTime uploadDate = submisi.getUploadDate();
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
        String newDate = uploadDate.format(formatDate);


        //ambil file akhir
        DokumenModel file = submisi.getDokumenList().get(0);

        //komentar submisi (langsung html)

        //ambil periode
        PeriodeTahunModel periode = periodeTahunService.getPeriodeByTahun(tahun);
        boolean period = false;
        if (periode.isEnded() == true){
            period = true;
        }

        model.addAttribute("listsubfaktoruji", listSubfaktoruji);
        model.addAttribute("period", period);
        model.addAttribute("subperspektif",subperspektif);
        model.addAttribute("submisi", submisi);
        model.addAttribute("newDate", newDate);
        model.addAttribute("file", file);
        model.addAttribute("statusPenilaian", statusPenilaian);


        return "view-dokumen-terupload";
    }

    @RequestMapping(value = "/{tahun}/edit/{idSubperspektif}", method = RequestMethod.GET)
    private String editUploadedFile(@PathVariable long idSubperspektif, @PathVariable int tahun,  Model model){
        permissionConfigService.checkPermission("Edit Dokumen");
        //PeriodeTahunModel periode = periodeTahunService.getPeriodeByTahun(tahun);

        LembarKerjaModel subperspektif = lembarKerjaService.getSubperspektifById(idSubperspektif);

        LembarKerjaModel perspektif = lembarKerjaService.getSubperspektifById(idSubperspektif).getLembarKerjaParent();

        SubmisiModel submisi = submisiService.getAktifSubmisiBySubperspektif(subperspektif);

        List<DelegasiModel> delegasiList = perspektif.getDelegasiLembarKerjaList();
        List<DivisiModel> divisiList = new ArrayList<>();
        for (DelegasiModel d : delegasiList) {
            if (!divisiList.contains(d.getDelegasiDivisi())) {
                divisiList.add(d.getDelegasiDivisi());
            }
        }

        List<LembarKerjaModel> listSubfaktoruji  = lembarKerjaService.getListSubfaktorUjiBySubperspektif(subperspektif.getPeriodeLembarKerja(), subperspektif.getSubperspektif());
        //submisi.setNilai(0.0);

        subperspektif =lembarKerjaService.setNilaiNull(subperspektif);

        perspektif = subperspektif.getLembarKerjaParent();
        LembarKerjaModel indikator = perspektif.getLembarKerjaParent();
        LembarKerjaModel aspek = indikator.getLembarKerjaParent();

        perspektif = lembarKerjaService.perbaharuiNilai(perspektif);
        indikator = lembarKerjaService.perbaharuiNilai(indikator);
        aspek = lembarKerjaService.perbaharuiNilai(aspek);

        for (LembarKerjaModel sub : listSubfaktoruji){
            sub = lembarKerjaService.setNilaiNull(sub);
        }
        System.out.println(listSubfaktoruji);


        //model.addAttribute("listsubfaktoruji", listSubfaktoruji);
        //model.addAttribute("periode", periode.getTahun());
        model.addAttribute("subperspektif",subperspektif);
        model.addAttribute("divisiDelegasi", divisiList);
        return "new-dokumen-upload";
    }

}
