package propensi.GCG.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import propensi.GCG.model.MasterParameterModel;
import propensi.GCG.repository.MasterParameterDB;
import propensi.GCG.service.MasterParameterService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/parameter")
public class MasterParameterRestController {
    @Autowired
    MasterParameterDB masterParameterDB;

    @Autowired
    MasterParameterService masterParameterService;

    @PostMapping(value = "hapus/{id}")
    public void deleteParameter(@PathVariable(value = "id") long id){
        MasterParameterModel parameter = masterParameterService.getMasterParameterById(id);
        masterParameterService.deleteParameter(parameter);
    }

    @PostMapping(value = "ubah/{id}")
    public void changeParameter(@PathVariable long id, String nama, String bobot, Model model){
        double nilai = 0;
        MasterParameterModel parameter = masterParameterService.getMasterParameterById(id);
        switch (parameter.getTipe()) {
            case "aspek": {
                List<MasterParameterModel> listParam = masterParameterService.getMasterParameterByAspek(parameter.getAspek());
                for(MasterParameterModel param : listParam){
                    param.setAspek(nama);
                    masterParameterDB.save(param);
                }
                break;
            }
            case "indikator": {
                List<MasterParameterModel> listParam = masterParameterService.getMasterParameterByIndikator(parameter.getIndikator());
                for(MasterParameterModel param : listParam){
                    param.setIndikator(nama);
                    masterParameterDB.save(param);
                }
                break;
            }
            case "perspektif": {
                double bobotIndikator = 0;
                double bobotAspek = 0;
                MasterParameterModel indikator = parameter.getParentParameter();
                MasterParameterModel aspek = indikator.getParentParameter();

                if(nama.length() != 0) {
                    List<MasterParameterModel> listParam = masterParameterService.getMasterParameterByPerspektif(parameter.getPerspektif());
                    for (MasterParameterModel param : listParam) {
                        param.setPerspektif(nama);
                        masterParameterDB.save(param);
                    }
                }
                if(bobot.length() != 0) {
                    nilai = Double.parseDouble(bobot);
                    parameter.setBobot(nilai);
                    List<MasterParameterModel> listPers = masterParameterService.getMasterParameterByParentParameter(indikator);
                    for (MasterParameterModel pers : listPers) {
                        bobotIndikator += pers.getBobot();
                    }
                    indikator.setBobot(bobotIndikator);
                    masterParameterDB.save(indikator);

                    List<MasterParameterModel> listInd = masterParameterService.getMasterParameterByParentParameter(aspek);
                    for (MasterParameterModel ind : listInd) {
                        bobotAspek += ind.getBobot();
                    }
                    aspek.setBobot(bobotAspek);
                    masterParameterDB.save(aspek);
                }
                break;
            }
            case "subperspektif": {
                List<MasterParameterModel> listParam = masterParameterService.getMasterParameterBySubperspektif(parameter.getSubperspektif());
                for(MasterParameterModel param : listParam){
                    param.setSubperspektif(nama);
                    masterParameterDB.save(param);
                }
                break;
            }
        }
    }

    @PostMapping(value = "/ubah/subperspektif/{id}")
    public void changeSubperspektif(@PathVariable long id, String subperspektif, String subfaktor0,
                                    String subfaktor1, String subfaktor2, String subfaktor3, String subfaktor4,
                                    String subfaktor5, String subfaktor6, String subfaktor7, String subfaktor8,
                                    String subfaktor9, String subfaktor10, String subfaktor11, String subfaktor12,
                                    String subfaktor13, String subfaktor14, String subfaktor15, String subfaktor16,
                                    String subfaktor17, String subfaktor18, String subfaktor19, String subfaktor20){
        MasterParameterModel parameter = masterParameterService.getMasterParameterById(id);
        List<MasterParameterModel> listChild = masterParameterService.getMasterParameterByParentParameter(parameter);
        List<String> listString = new ArrayList<>();
        listString.add(subfaktor0);listString.add(subfaktor1);listString.add(subfaktor2);listString.add(subfaktor3);
        listString.add(subfaktor4);listString.add(subfaktor5);listString.add(subfaktor6);listString.add(subfaktor7);
        listString.add(subfaktor8);listString.add(subfaktor9);listString.add(subfaktor10);listString.add(subfaktor11);
        listString.add(subfaktor12);listString.add(subfaktor13);listString.add(subfaktor14);listString.add(subfaktor15);
        listString.add(subfaktor16);listString.add(subfaktor17);listString.add(subfaktor18);listString.add(subfaktor19);
        listString.add(subfaktor20);
        for(int i = 0; i<listChild.size(); i++){
            listChild.get(i).setSubfaktorUji(listString.get(i));
            listChild.get(i).setSubperspektif(subperspektif);
            masterParameterDB.save(listChild.get(i));
        }
        parameter.setSubperspektif(subperspektif);
        masterParameterDB.save(parameter);
    }

    @PostMapping(value = "/tambah")
    public void addParameter(@ModelAttribute MasterParameterModel parameter,
                             @ModelAttribute MasterParameterModel aspek,
                             @ModelAttribute MasterParameterModel indikator,
                             @ModelAttribute MasterParameterModel perspektif, String nama, int tipe, String bobot){
        switch (tipe) {
            case 1:
                parameter.setTipe("aspek");
                parameter.setAspek(nama);
                parameter.setIndikator(null);
                parameter.setPerspektif(null);
                parameter.setBobot(Double.parseDouble(bobot));
                List<MasterParameterModel> listAspek = masterParameterService.getMasterParameterByTipe("aspek");
                parameter.setNoUrut(listAspek.size()+1);
                break;
            case 2:
                MasterParameterModel parentAs = masterParameterService.getMasterParameterById(Long.parseLong(aspek.getAspek()));
                List<MasterParameterModel> listIndikator = masterParameterService.getMasterParameterByParentParameter(parentAs);
                parameter.setTipe("indikator");
                parameter.setIndikator(nama);
                parameter.setAspek(parentAs.getAspek());
                parameter.setPerspektif(null);
                parameter.setBobot(Double.parseDouble(bobot));
                parameter.setParentParameter(parentAs);
                parameter.setNoUrut(listIndikator.size()+1);
                break;
            case 3:
                MasterParameterModel parentInd = masterParameterService.getMasterParameterById(Long.parseLong(indikator.getIndikator()));
                List<MasterParameterModel> listPers = masterParameterService.getMasterParameterByParentParameter(parentInd);
                parameter.setTipe("perspektif");
                parameter.setPerspektif(nama);
                parameter.setIndikator(parentInd.getIndikator());
                parameter.setAspek(parentInd.getAspek());
                parameter.setBobot(Double.parseDouble(bobot));
                parameter.setParentParameter(parentInd);
                parameter.setNoUrut(listPers.size()+1);
                break;
            case 4:
                MasterParameterModel parentPer = masterParameterService.getMasterParameterById(Long.parseLong(perspektif.getPerspektif()));
                List<MasterParameterModel> listSubp = masterParameterService.getMasterParameterByParentParameter(parentPer);
                parameter.setTipe("subperspektif");
                parameter.setSubperspektif(nama);
                parameter.setPerspektif(parentPer.getPerspektif());
                parameter.setIndikator(parentPer.getIndikator());
                parameter.setAspek(parentPer.getAspek());
                parameter.setBobot(0d);
                parameter.setParentParameter(parentPer);
                parameter.setNoUrut(listSubp.size()+1);
                break;
        }
        parameter.setActiveFlag(true);
        List<MasterParameterModel> all = masterParameterService.getAllMasterParameter();
        parameter.setIdFixed(all.get(all.size()-1).getIdFixed()+1);
        masterParameterDB.save(parameter);
    }

    @PostMapping(value = "/tambah/subfaktor/{id}")
    public void addSubfaktor(@PathVariable long id, String nama){
        MasterParameterModel subperspektif = masterParameterService.getMasterParameterById(id);
        List<MasterParameterModel> all = masterParameterService.getAllMasterParameter();
        List<MasterParameterModel> listSubFaktor = masterParameterService.getMasterParameterByParentParameter(subperspektif);
        MasterParameterModel subFaktor = new MasterParameterModel();
        subFaktor.setActiveFlag(true);
        subFaktor.setBobot(0d);
        subFaktor.setIdFixed(all.get(all.size()-1).getIdFixed()+1);
        subFaktor.setNoUrut(listSubFaktor.size()+1);
        subFaktor.setTipe("subfaktor_uji");
        subFaktor.setParentParameter(subperspektif);
        subFaktor.setAspek(subperspektif.getAspek());
        subFaktor.setIndikator(subperspektif.getIndikator());
        subFaktor.setPerspektif(subperspektif.getPerspektif());
        subFaktor.setSubperspektif(subperspektif.getSubperspektif());
        subFaktor.setSubfaktorUji(nama);
        masterParameterDB.save(subFaktor);
    }
}