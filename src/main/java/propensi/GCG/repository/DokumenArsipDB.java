package propensi.GCG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.GCG.model.DokumenArsipModel;
import propensi.GCG.model.PeriodeTahunModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface DokumenArsipDB extends JpaRepository<DokumenArsipModel, Long> {
    DokumenArsipModel findById(long id);
    DokumenArsipModel findByPeriodeDokumenArsip(PeriodeTahunModel periodeTahunModel);
    List<DokumenArsipModel> findByDokumenRef(String dokumenRef);
    Optional<DokumenArsipModel> findByNama(String namaDokumen);
}
