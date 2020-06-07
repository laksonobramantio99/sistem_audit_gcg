package propensi.GCG.service;


import propensi.GCG.model.ACLModel;
import propensi.GCG.model.RoleModel;

import java.util.List;

public interface AclService {
    ACLModel addAcl(ACLModel aclModel);
    List<ACLModel> getByRole(RoleModel roleModel);
    void deleteSomeAcl(List<ACLModel> aclModels);
}
