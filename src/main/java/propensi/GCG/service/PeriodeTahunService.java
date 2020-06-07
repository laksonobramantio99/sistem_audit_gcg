package propensi.GCG.service;

import propensi.GCG.model.PeriodeTahunModel;

import java.util.List;

public interface PeriodeTahunService {
    PeriodeTahunModel getPeriodeByTahun(int tahun);
    PeriodeTahunModel getPeriodeById(Long id);
    PeriodeTahunModel getPeriodeBerjalan();
    PeriodeTahunModel getPeriodeAktif();
    PeriodeTahunModel kalkulasiTotalCapaian(PeriodeTahunModel periodeTahunModel);
    void addPeriode(PeriodeTahunModel periodeTahunModel);
    void deactivatedPeriode(PeriodeTahunModel periodeTahunModel, boolean active);
    String tutupPeriode(PeriodeTahunModel periodeTahunModel, boolean isEnded);
    List<PeriodeTahunModel> getAllPeriode();
    List<PeriodeTahunModel> getListPeriodeAktif();
    List<PeriodeTahunModel> getListPeriodeBerakhir();
    boolean cekPeriodeDiTutup();
}
