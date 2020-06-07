package propensi.GCG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import propensi.GCG.model.DelegasiModel;
import propensi.GCG.model.DivisiModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface DelegasiDB extends JpaRepository<DelegasiModel, Long> {
    Optional<DelegasiModel> findById(Long id);

    List<DelegasiModel> findByDelegasiDivisi(DivisiModel divisi);
}
