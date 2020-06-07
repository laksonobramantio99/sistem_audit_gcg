package propensi.GCG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import propensi.GCG.model.MasterParameterModel;
import propensi.GCG.repository.MasterParameterDB;
import propensi.GCG.service.MasterParameterService;
import propensi.GCG.service.PermissionConfigService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/parameter")
public class MasterParameterController {
    @Autowired
    MasterParameterService masterParameterService;

    @Autowired
    MasterParameterDB masterParameterDB;

    @Autowired
    PermissionConfigService permissionConfigService;

    @GetMapping(value = "/ubah")
    public String ubahParameter(Model model){
        permissionConfigService.checkPermission("Ubah Parameter");

        double nilai = 0;
        List<MasterParameterModel> listAspek = masterParameterService.getMasterParameterByTipe("aspek");
        List<List<MasterParameterModel>> listIndikator = new ArrayList<>();
        List<List<List<MasterParameterModel>>> listPerspektif = new ArrayList<>();
        List<List<List<List<MasterParameterModel>>>> listSubperspektif = new ArrayList<>();
        for(MasterParameterModel aspek : listAspek){
            List<MasterParameterModel> tempIndikator = masterParameterService.getMasterParamaterByAspekandTipe(aspek.getAspek(), "indikator");
            listIndikator.add(tempIndikator);
        }

        for(List<MasterParameterModel> listPerspektifByIndikator : listIndikator){
            List<List<MasterParameterModel>> perspektifByIndikator = new ArrayList<>();
            for(MasterParameterModel indikator : listPerspektifByIndikator){
                List<MasterParameterModel> tempPerspektif = masterParameterService.getMasterParamaterByIndikatorandTipe(indikator.getIndikator(), "perspektif");
                perspektifByIndikator.add(tempPerspektif);
            }
            listPerspektif.add(perspektifByIndikator);
        }
        for(List<List<MasterParameterModel>> listSubperspektifByPerspektif : listPerspektif){
            List<List<List<MasterParameterModel>>> subperspektifByPerspektif = new ArrayList<>();
            for(List<MasterParameterModel> Listperspektif: listSubperspektifByPerspektif){
                List<List<MasterParameterModel>> ListsSubperspektif = new ArrayList<>();
                for(MasterParameterModel pespektif: Listperspektif){
                    List<MasterParameterModel> tempSubperspektif = masterParameterService.getMasterParamaterByPerspektifandTipe(pespektif.getPerspektif(), "subperspektif");
                    ListsSubperspektif.add(tempSubperspektif);
                }
                subperspektifByPerspektif.add(ListsSubperspektif);
            }
            listSubperspektif.add(subperspektifByPerspektif);
        }
        for(MasterParameterModel aspek : listAspek){
            nilai += aspek.getBobot();
        }
        model.addAttribute("nilai", nilai);
        model.addAttribute("listAspek", listAspek);
        model.addAttribute("listIndikator", listIndikator);
        model.addAttribute("listPerspektif", listPerspektif);
        model.addAttribute("listSubperspektif", listSubperspektif);
        return "masterparameter-ubah-form";
    }

    @GetMapping(value = "/ubah/subperspektif/{id}")
    public String ubahSubperspektif(@PathVariable long id, Model model){
        permissionConfigService.checkPermission("Ubah Subperspektif");
        MasterParameterModel subperspektif = masterParameterService.getMasterParameterById(id);
        List<MasterParameterModel> listSubFaktor = masterParameterService.getMasterParameterByParentParameter(subperspektif);
        model.addAttribute("listSubFaktor", listSubFaktor);
        model.addAttribute("subperspektif", subperspektif);
        return "masterparameter-ubah-subperspektif";
    }

    @GetMapping(value = "/tambah")
    public String tambahParameter(Model model){
        permissionConfigService.checkPermission("Tambah Parameter");
        MasterParameterModel masterParameterModel = new MasterParameterModel();
        List<MasterParameterModel> listAspek = masterParameterService.getMasterParameterByTipe("aspek");
        List<MasterParameterModel> listIndikator = masterParameterService.getMasterParameterByTipe("indikator");
        List<MasterParameterModel> listPerspektif = masterParameterService.getMasterParameterByTipe("perspektif");
        model.addAttribute("parameter", masterParameterModel);
        model.addAttribute("listAspek", listAspek);
        model.addAttribute("listIndikator", listIndikator);
        model.addAttribute("listPerspektif", listPerspektif);
        return "masterparameter-tambah-form";
    }
}
