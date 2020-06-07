package propensi.GCG.service;

import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.repository.LembarKerjaDB;
import propensi.GCG.repository.PeriodeTahunDB;
import propensi.GCG.model.PeriodeTahunModel;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PeriodeTahunServiceImpl implements PeriodeTahunService{
    @Autowired
    PeriodeTahunDB periodeTahunDB;

    @Autowired
    LembarKerjaDB lembarKerjaDB;

    @Autowired
    LembarKerjaService lembarKerjaService;

    @Override
    public PeriodeTahunModel getPeriodeByTahun(int tahun){
        return periodeTahunDB.findByTahunAndActiveFlag(tahun, true);
    }

    @Override
    public List<PeriodeTahunModel> getAllPeriode() {
        return periodeTahunDB.findAll();
    }

    @Override
    public void addPeriode(PeriodeTahunModel periodeTahunModel) {
        periodeTahunModel.setCreatedDate(new Date());
        periodeTahunModel.setEnded(false);
        periodeTahunModel.setActiveFlag(true);
        periodeTahunModel.setCapaian(0d);
        periodeTahunDB.save(periodeTahunModel);
    }

    @Override
    public void deactivatedPeriode(PeriodeTahunModel periodeTahunModel, boolean active) {
        periodeTahunModel.setActiveFlag(!active);
        periodeTahunDB.save(periodeTahunModel);
    }

    @Override
    public String tutupPeriode(PeriodeTahunModel periodeTahunModel, boolean isEnded) {
        List<LembarKerjaModel> lembarKerjaList = lembarKerjaDB.findByPeriodeLembarKerja(periodeTahunModel);
        boolean cekNilai = true;
        for(LembarKerjaModel lembarKerja : lembarKerjaList){
            if(lembarKerja.getNilai() == null){
                cekNilai = false;
                break;
            }
        }
        if(cekNilai) {
//            periodeTahunModel.setEnded(!isEnded);
//            periodeTahunDB.save(periodeTahunModel);
            return "berhasil";
        }
        return "gagal";
    }

    @Override
    public List<PeriodeTahunModel> getListPeriodeAktif() {
        return periodeTahunDB.findByActiveFlag(true);
    }

    @Override
    public List<PeriodeTahunModel> getListPeriodeBerakhir() {
        return periodeTahunDB.findByActiveFlagAndIsEnded(true,true);
    }

    @Override
    public PeriodeTahunModel getPeriodeById(Long id) {
        return periodeTahunDB.findById(id).get();
    }

    @Override
    public PeriodeTahunModel getPeriodeBerjalan() {
        List<PeriodeTahunModel> listPeriodeAktif = periodeTahunDB.findByActiveFlagAndIsEnded(true, false);
        if (listPeriodeAktif.isEmpty()){
            return null;
        }
        return listPeriodeAktif.get(listPeriodeAktif.size()-1);
    }

    @Override
    public PeriodeTahunModel getPeriodeAktif() {
        List<PeriodeTahunModel> listPeriodeAktif = periodeTahunDB.findByActiveFlag(true);
        if (listPeriodeAktif.size() == 0) {
            return null;
        }
        return listPeriodeAktif.get(listPeriodeAktif.size() - 1);
    }

    @Override
    public PeriodeTahunModel kalkulasiTotalCapaian(PeriodeTahunModel periodeTahunModel) {
        PeriodeTahunModel periode = periodeTahunDB.findById(periodeTahunModel.getId()).get();
        List<LembarKerjaModel> listAspek = lembarKerjaService.getListAspek(periode);
        Double nilai = Double.valueOf(0);
        for (LembarKerjaModel aspek : listAspek){
            if (aspek.getTertimbang() == null){
                aspek.setTertimbang((double) 0);
            }
            nilai += aspek.getTertimbang();
        }
        periode.setCapaian(nilai);
        periodeTahunDB.save(periode);
        return periode;
    }

    @Override
    public boolean cekPeriodeDiTutup() {
        List<PeriodeTahunModel> listPeriode = periodeTahunDB.findAll();
        for (PeriodeTahunModel periodeTahunModel : listPeriode) {
            if (!periodeTahunModel.isEnded()) {
                return false;
            }
        }
        return true;
    }
}
