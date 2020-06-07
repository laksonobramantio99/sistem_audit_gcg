package propensi.GCG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.GCG.model.DelegasiModel;
import propensi.GCG.model.DivisiModel;
import propensi.GCG.model.PeriodeTahunModel;
import propensi.GCG.repository.DelegasiDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DelegasiServiceImpl implements DelegasiService {
    @Autowired
    DelegasiDB delegasiDB;

    @Override
    public void addDelegasi(DelegasiModel delegasi) {
        delegasiDB.save(delegasi);
    }

    @Override
    public void deleteDelegasi(DelegasiModel delegasi) {
        delegasiDB.delete(delegasi);
    }

    @Override
    public List<DelegasiModel> getDelegasiByDivisi(DivisiModel divisi) {
        return delegasiDB.findByDelegasiDivisi(divisi);
    }

    @Override
    public void deactivatedDelegasi(List<DelegasiModel> listDelegasi, boolean active) {
        for (DelegasiModel delegasi : listDelegasi) {
            delegasi.setActiveFlag(!active);
            delegasiDB.save(delegasi);
        }
    }

    @Override
    public List<DelegasiModel> getDelegasiByPeriode(List<DelegasiModel> existingListDelegasi, PeriodeTahunModel periode) {
        List<DelegasiModel> listDelegasi = new ArrayList<>();
        for (DelegasiModel delegasi : existingListDelegasi) {
            if (delegasi.getDelegasiLembarKerja().getPeriodeLembarKerja().getTahun() == periode.getTahun()) {
                listDelegasi.add(delegasi);
            }
        }
        return listDelegasi;
    }

    @Override
    public List<DelegasiModel> getAllDelegasi() {
        return delegasiDB.findAll();
    }
}
