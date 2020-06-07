package propensi.GCG.service;

import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.PeriodeTahunModel;
import propensi.GCG.model.UserModel;

import java.util.List;
import java.util.Optional;

public interface LembarKerjaService {
    List<LembarKerjaModel> getListPerspektifByAspek(PeriodeTahunModel periode, String aspek);
    List<LembarKerjaModel> getListAspek(PeriodeTahunModel periode);
    Optional<LembarKerjaModel> getLembarKerjaByIdFixed(Long idFixed);

    LembarKerjaModel getLembarKerja(PeriodeTahunModel periode, int no_aspek);
    List<LembarKerjaModel> getListIndikatorByAspek(PeriodeTahunModel periode, String aspek);
    List<LembarKerjaModel> getListPerspektifByIndikator(PeriodeTahunModel periode,String indikator);
    List<LembarKerjaModel> getListSubperspektifByPerspektif(PeriodeTahunModel periode,String perspektif);
    List<LembarKerjaModel> getListSubfaktorUjiBySubperspektif(PeriodeTahunModel periode, String subfaktoruji);
    List<LembarKerjaModel> getListIndikatorBerdasarkanDelegasiDanAspek(UserModel user, LembarKerjaModel aspek, PeriodeTahunModel periode);

    List<Double> getPersentaseSubperspektifTanpaDokumen(LembarKerjaModel aspek);

    void addLembarKerja(PeriodeTahunModel periodeTahunModel);
    
    LembarKerjaModel getSubperspektifById(long idSubperspektif);
    LembarKerjaModel createNilai(LembarKerjaModel subperspektifInput);
    LembarKerjaModel perbaharuiNilai(LembarKerjaModel parameterYangDikalkulasi);

    List<LembarKerjaModel> getListChildLembarKerjaByParent(LembarKerjaModel parent);

    List<LembarKerjaModel> getListPerspektif(PeriodeTahunModel periode);
    LembarKerjaModel setNilaiNull(LembarKerjaModel subperspektif);
    List<LembarKerjaModel> getSubperspektifAktif(Integer tahun);

}
