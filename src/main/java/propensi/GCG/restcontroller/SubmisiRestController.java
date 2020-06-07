package propensi.GCG.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.SubmisiModel;
import propensi.GCG.model.UserModel;
import propensi.GCG.service.LembarKerjaService;
import propensi.GCG.service.SubmisiService;
import propensi.GCG.service.UserService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping ("/api/submisi")
public class SubmisiRestController {
    @Autowired
    SubmisiService submisiService;

    @Autowired
    UserService userService;

    @Autowired
    LembarKerjaService lembarKerjaService;

    @PostMapping(value = "/tambah/{idSubperspektif}")
    private Map<String,Object> addSubmisi(@ModelAttribute SubmisiModel submisiModel, @PathVariable (value = "idSubperspektif")long idSubperspektif){
        submisiModel.setActiveFlag(true);

        LocalDateTime date = LocalDateTime.now();
        submisiModel.setUploadDate(date);

        UserModel userByName = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        submisiModel.setUploaderSubmisi(userByName);

        LembarKerjaModel subperspektif = lembarKerjaService.getSubperspektifById(idSubperspektif);
        submisiModel.setSubmisiLembarKerja(subperspektif);

        SubmisiModel newSubmisi = submisiService.addSubmisi(submisiModel);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("idSubmisi", newSubmisi.getId());

        return response;
    }

    @PostMapping(value = "/edit/{idSubperspektif}")
    private Map<String,Object> editSubmisi(@ModelAttribute SubmisiModel submisiModel, @PathVariable (value = "idSubperspektif")long idSubperspektif){
        submisiModel.setActiveFlag(true);

        LocalDateTime date = LocalDateTime.now();
        submisiModel.setUploadDate(date);

        UserModel userByName = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        submisiModel.setUploaderSubmisi(userByName);

        LembarKerjaModel subperspektif = lembarKerjaService.getSubperspektifById(idSubperspektif);
        submisiModel.setSubmisiLembarKerja(subperspektif);

        SubmisiModel newSubmisi = submisiService.editSubmisi(submisiModel);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("idSubmisi", newSubmisi.getId());

        return response;
    }

    @GetMapping(value = "/getAoi/{idSubmisi}")
    private Map<String,Object> getAoi(@PathVariable (value = "idSubmisi") long idSubmisi) {
        SubmisiModel submisi = submisiService.findById(idSubmisi);
        Map<String, Object> response = new HashMap<String, Object>();
        if (submisi.getKelebihan() == null || submisi.getKelebihan() ==""){
            response.put("kelebihan", "Tidak ada kelebihan");
        }
        else {
            response.put("kelebihan", submisi.getKelebihan());
        }

        if (submisi.getKekurangan() == null || submisi.getKekurangan() ==""){
            response.put("kekurangan", "Tidak ada kekurangan");
        }
        else {
            response.put("kekurangan", submisi.getKekurangan());
        }

        if (submisi.getHambatan() == null || submisi.getHambatan() ==""){
            response.put("hambatan", "Tidak ada hambatan");
        }
        else {
            response.put("hambatan", submisi.getHambatan());
        }

        if (submisi.getRekomendasi() == null || submisi.getRekomendasi() == ""){
            response.put("rekomendasi", "Tidak ada rekomendasi");
        }
        else {
            response.put("rekomendasi", submisi.getRekomendasi());
        }

        if (submisi.getReferensi() == null || submisi.getReferensi() == ""){
            response.put("referensi", "Tidak ada referensi");
        }
        else {
            response.put("referensi", submisi.getReferensi());
        }



        return response;
    }


}
