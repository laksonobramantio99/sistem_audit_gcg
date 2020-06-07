package propensi.GCG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.GCG.model.MenuModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuDB extends JpaRepository<MenuModel, Long> {
    Optional<MenuModel> findById(Long id);
    List<MenuModel> findByIsParent(Boolean isParent);
    Optional<MenuModel> findByNama(String nama);
}
