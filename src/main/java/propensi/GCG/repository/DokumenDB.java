package propensi.GCG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import propensi.GCG.model.DivisiModel;
import propensi.GCG.model.DokumenArsipModel;
import propensi.GCG.model.DokumenModel;

import java.util.Optional;

public interface DokumenDB extends JpaRepository<DokumenModel, String> {
    Optional<DokumenModel> findById(String id);
    DokumenModel findByArsipDokumen(DokumenArsipModel dokumenArsipModel);
//    Optional<DokumenModel> findByName(String name);
}
