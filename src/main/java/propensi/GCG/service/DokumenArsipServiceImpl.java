package propensi.GCG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.GCG.model.DokumenArsipModel;
import propensi.GCG.model.PeriodeTahunModel;
import propensi.GCG.repository.DokumenArsipDB;

import java.util.List;
import java.util.Optional;

@Service
public class DokumenArsipServiceImpl implements DokumenArsipService {
    @Autowired
    DokumenArsipDB dokumenArsipDB;
    @Override
    public DokumenArsipModel addArsip(DokumenArsipModel dokumenArsipModel) {
        return dokumenArsipDB.save(dokumenArsipModel);
    }

    @Override
    public DokumenArsipModel getById(long id) {
        return dokumenArsipDB.findById(id);
    }

    @Override
    public DokumenArsipModel getByPeriode(PeriodeTahunModel periodeTahunModel) {
        return dokumenArsipDB.findByPeriodeDokumenArsip(periodeTahunModel);
    }

    @Override
    public List<DokumenArsipModel> getAllDokumenArsip() {
        return dokumenArsipDB.findByDokumenRef("arsip");
    }

    @Override
    public DokumenArsipModel findDokumenArsipByNamaDokumen(String namaDokumen) {
        Optional<DokumenArsipModel> dokumen = dokumenArsipDB.findByNama(namaDokumen);
        if (dokumen.isPresent()) {
            return dokumen.get();
        }
        return null;
    }

    @Override
    public void deleteDokumenArsip(DokumenArsipModel dokumenArsip) {
        dokumenArsipDB.delete(dokumenArsip);
    }
}
