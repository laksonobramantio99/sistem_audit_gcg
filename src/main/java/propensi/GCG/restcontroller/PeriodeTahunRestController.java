package propensi.GCG.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import propensi.GCG.model.PeriodeTahunModel;
import propensi.GCG.model.UserModel;
import propensi.GCG.repository.PeriodeTahunDB;
import propensi.GCG.service.LembarKerjaService;
import propensi.GCG.service.PeriodeTahunService;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/periode")
public class PeriodeTahunRestController {
    @Autowired
    PeriodeTahunService periodeTahunService;

    @Autowired
    PeriodeTahunDB periodeTahunDB;

    @Autowired
    LembarKerjaService lembarKerjaService;

    @PostMapping(value = "/aktivasi/{id}")
    public void activePeriode(@PathVariable(value = "id") long id){
        PeriodeTahunModel periodeTahunModel = periodeTahunService.getPeriodeById(id);
        periodeTahunService.deactivatedPeriode(periodeTahunModel, periodeTahunModel.isActiveFlag());
    }

    @GetMapping(value = "/tutup/{id}")
    public LinkedHashMap<String, Object> getHasilTutup(@PathVariable(value = "id") long id){
        PeriodeTahunModel periodeTahunModel = periodeTahunService.getPeriodeById(id);
        LinkedHashMap<String, Object> respon = new LinkedHashMap<String, Object>();
        String hasil = periodeTahunService.tutupPeriode(periodeTahunModel, periodeTahunModel.isEnded());
        respon.put("hasil", hasil);
        return respon;
    }

    @PostMapping(value = "/buka/{id}")
    public void bukaPeriode(@PathVariable(value = "id") long id){
        PeriodeTahunModel periodeTahunModel = periodeTahunService.getPeriodeById(id);
        periodeTahunModel.setEnded(!periodeTahunModel.isEnded());
        periodeTahunDB.save(periodeTahunModel);
    }

    @GetMapping(value = "/isAvailable/{tahun}")
    private Map<String, Object> isAvailable(@PathVariable int tahun){
        PeriodeTahunModel periode = periodeTahunService.getPeriodeByTahun(tahun);
        boolean isAvailable = false;
        if (periode != null)
            isAvailable = true;

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("tahun", tahun);
        response.put("isAvailable", isAvailable);

        return response;
    }

    @GetMapping(value = "/listPeriode")
    private LinkedHashMap<String,Object> getAllPeriode(){
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        List<PeriodeTahunModel> listPeriode = periodeTahunService.getListPeriodeAktif();
        for (PeriodeTahunModel periode : listPeriode){
            if (periode.getCapaian() == null){
                periode.setCapaian((double) 0);
            }
            DecimalFormat df = new DecimalFormat("#.#");
            periode.setCapaian(Double.valueOf(df.format(periode.getCapaian())));
            response.put(String.valueOf(periode.getTahun()),periode.getCapaian());
        }

        return response;
    }

    @PostMapping(value = "/tambah")
    private void addLembarKerja(){
        List<PeriodeTahunModel> listPeriode = periodeTahunService.getAllPeriode();
        lembarKerjaService.addLembarKerja(listPeriode.get(listPeriode.size()-1));
    }

    @PostMapping(value = "/hapus")
    private void deletePeriode(){
        List<PeriodeTahunModel> listPeriode = periodeTahunService.getAllPeriode();
        periodeTahunDB.delete(listPeriode.get(listPeriode.size()-1));
    }
}
