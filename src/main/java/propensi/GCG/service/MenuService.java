package propensi.GCG.service;

import propensi.GCG.model.MenuModel;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<MenuModel> getMenuList();
    List<MenuModel> getParentMenu();
    List<MenuModel> getChildMenu();
    Optional<MenuModel> getById(Long id);
    Optional<MenuModel> getByNama(String nama);
}
