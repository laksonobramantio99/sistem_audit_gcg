package propensi.GCG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.GCG.model.DivisiModel;

import java.util.Optional;

@Repository
public interface DivisiDB extends JpaRepository<DivisiModel, Long> {
    Optional<DivisiModel> findById(Long id);

}
