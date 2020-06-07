package propensi.GCG.service;

import propensi.GCG.model.RoleModel;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<RoleModel> getRoleList();
    RoleModel addRole(RoleModel roleModel);
    Optional<RoleModel> getById(Long id);
}
