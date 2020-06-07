package propensi.GCG.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.PeriodeTahunModel;
import propensi.GCG.model.SubmisiModel;
import propensi.GCG.model.UserModel;
import propensi.GCG.service.LembarKerjaService;
import propensi.GCG.service.PeriodeTahunService;
import propensi.GCG.service.SubmisiService;
import propensi.GCG.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/api/lembarkerja")
public class LembarKerjaRestController {

    @Autowired
    LembarKerjaService lembarKerjaService;

    @Autowired
    PeriodeTahunService periodeTahunService;

    @Autowired
    SubmisiService submisiService;

    @Autowired
    UserService userService;

    @GetMapping(value = "/{tahun}/getAllAspek")
    private LinkedHashMap<String,Object> getAllAspek(@PathVariable int tahun){
        PeriodeTahunModel periode = periodeTahunService.getPeriodeByTahun(tahun);
        UserModel user = userService.getCurrentUser();

        List<LembarKerjaModel> listAspek = lembarKerjaService.getListAspek(periode);
        List<LembarKerjaModel> listAspekUntukDisimpan = new ArrayList<>();
        listAspekUntukDisimpan.addAll(listAspek);
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

        for (LembarKerjaModel aspek : listAspek){
            if(!(user.getUserRole().getNama().equals("Admin") || user.getUserRole().getNama().equals("Auditor Internal") || user.getUserRole().getNama().equals("Auditor Eksternal"))){
                System.out.println("user role " + user.getUserRole().getNama());
                List<LembarKerjaModel> listIndikator = lembarKerjaService.getListIndikatorBerdasarkanDelegasiDanAspek(user, aspek, periode);
                if (listIndikator.isEmpty()){
                    listAspekUntukDisimpan.remove(aspek);
                    continue;
                }
            }
            String nomorAspekRomawi = IntegerToRomanNumeral(aspek.getNoUrut());
            Double nilaiAspek = aspek.getNilai();
            if (nilaiAspek == null){
                nilaiAspek = 0d;
            }
            String nilai = ((int) Math.round(nilaiAspek*100)) + "% " + aspek.getNoUrut();
            String namaAspek = "Aspek " + nomorAspekRomawi;
            response.put(namaAspek, nilai);
        }
        return response;
    }

    @GetMapping(value = "/{tahun}/getAOI/{idSubperspektif}")
    private LinkedHashMap<String,Object> getLastSubmission(@PathVariable int tahun, @PathVariable int idSubperspektif) {
        LembarKerjaModel subperspektif = lembarKerjaService.getSubperspektifById(idSubperspektif);
        PeriodeTahunModel periode = periodeTahunService.getPeriodeByTahun(tahun);
        UserModel user = userService.getCurrentUser();
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        SubmisiModel submisiAktif = submisiService.getAktifSubmisiBySubperspektif(subperspektif);

        System.out.println("idsubmisi : " + submisiAktif);
        response.put("idSubmisi",submisiAktif.getId());
        return response;

    };

    public static String IntegerToRomanNumeral(int input) {
        if (input < 1 || input > 3999)
            return "Invalid Roman Number Value";
        String s = "";
        while (input >= 1000) {
            s += "M";
            input -= 1000;        }
        while (input >= 900) {
            s += "CM";
            input -= 900;
        }
        while (input >= 500) {
            s += "D";
            input -= 500;
        }
        while (input >= 400) {
            s += "CD";
            input -= 400;
        }
        while (input >= 100) {
            s += "C";
            input -= 100;
        }
        while (input >= 90) {
            s += "XC";
            input -= 90;
        }
        while (input >= 50) {
            s += "L";
            input -= 50;
        }
        while (input >= 40) {
            s += "XL";
            input -= 40;
        }
        while (input >= 10) {
            s += "X";
            input -= 10;
        }
        while (input >= 9) {
            s += "IX";
            input -= 9;
        }
        while (input >= 5) {
            s += "V";
            input -= 5;
        }
        while (input >= 4) {
            s += "IV";
            input -= 4;
        }
        while (input >= 1) {
            s += "I";
            input -= 1;
        }
        return s;
    }
}
