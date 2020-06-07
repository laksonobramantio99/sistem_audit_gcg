package propensi.GCG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.GCG.model.MenuModel;
import propensi.GCG.repository.MenuDB;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDB menuDB;

    @Override
    public List<MenuModel> getMenuList() {
        return menuDB.findAll();
    }

    @Override
    public List<MenuModel> getParentMenu() {
        return menuDB.findByIsParent(true);
    }

    @Override
    public List<MenuModel> getChildMenu() {
        return menuDB.findByIsParent(false);
    }

    @Override
    public Optional<MenuModel> getById(Long id) {
        return menuDB.findById(id);
    }

    @Override
    public Optional<MenuModel> getByNama(String nama) {
        return menuDB.findByNama(nama);
    }
}
