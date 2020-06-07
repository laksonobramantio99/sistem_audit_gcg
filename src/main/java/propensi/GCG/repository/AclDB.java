package propensi.GCG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.GCG.model.ACLModel;
import propensi.GCG.model.RoleModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface AclDB extends JpaRepository<ACLModel, Long> {
    Optional<ACLModel> findById(Long id);
    List<ACLModel> findByAclRole(RoleModel roleModel);
}
