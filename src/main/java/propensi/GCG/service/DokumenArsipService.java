package propensi.GCG.service;

import propensi.GCG.model.DokumenArsipModel;
import propensi.GCG.model.PeriodeTahunModel;

import java.util.List;

public interface DokumenArsipService {
    DokumenArsipModel addArsip(DokumenArsipModel dokumenArsipModel);
    DokumenArsipModel getById(long id);
    DokumenArsipModel getByPeriode(PeriodeTahunModel periodeTahunModel);
    List<DokumenArsipModel> getAllDokumenArsip();
    DokumenArsipModel findDokumenArsipByNamaDokumen(String namaDokumen);
    void deleteDokumenArsip(DokumenArsipModel dokumenArsip);
}
