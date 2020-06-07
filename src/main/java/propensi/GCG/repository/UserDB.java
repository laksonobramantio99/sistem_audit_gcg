package propensi.GCG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.GCG.model.DivisiModel;
import propensi.GCG.model.UserModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDB extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findById(Long id);
    List<UserModel> findByUserDivisi(DivisiModel divisiModel);
    Optional<UserModel> findByUsernameAndActiveFlag(String username, Boolean activeFlag);
}
