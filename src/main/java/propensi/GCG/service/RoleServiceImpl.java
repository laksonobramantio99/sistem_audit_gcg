package propensi.GCG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.GCG.model.RoleModel;
import propensi.GCG.repository.RoleDB;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDB roleDB;

    @Override
    public List<RoleModel> getRoleList() {
        return roleDB.findAll();
    }

    @Override
    public RoleModel addRole(RoleModel roleModel) {
        return roleDB.save(roleModel);
    }

    @Override
    public Optional<RoleModel> getById(Long id) {
        return roleDB.findById(id);
    }
}
