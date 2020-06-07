package propensi.GCG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.GCG.model.RoleModel;

import java.util.Optional;

@Repository
public interface RoleDB extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> findById(Long id);
}
