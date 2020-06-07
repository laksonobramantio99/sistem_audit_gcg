package propensi.GCG.service;

import propensi.GCG.model.DelegasiModel;
import propensi.GCG.model.DivisiModel;
import propensi.GCG.model.PeriodeTahunModel;

import java.util.List;
import java.util.Optional;

public interface DelegasiService {
    void addDelegasi(DelegasiModel delegasi);

    void deleteDelegasi(DelegasiModel delegasi);

    List<DelegasiModel> getDelegasiByDivisi(DivisiModel divisi);

    void deactivatedDelegasi(List<DelegasiModel> listDelegasi, boolean active);

    List<DelegasiModel> getDelegasiByPeriode(List<DelegasiModel> existingListdelegasi, PeriodeTahunModel periode);

    List<DelegasiModel> getAllDelegasi();
}
