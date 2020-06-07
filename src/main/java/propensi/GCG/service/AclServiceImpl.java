package propensi.GCG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.GCG.model.ACLModel;
import propensi.GCG.model.RoleModel;
import propensi.GCG.repository.AclDB;

import java.util.List;

@Service
public class AclServiceImpl implements AclService {
    @Autowired
    private AclDB aclDB;

    @Override
    public ACLModel addAcl(ACLModel aclModel) {
        return aclDB.save(aclModel);
    }

    @Override
    public List<ACLModel> getByRole(RoleModel roleModel) {
        return aclDB.findByAclRole(roleModel);
    }

    @Override
    public void deleteSomeAcl(List<ACLModel> aclModels) {
        aclDB.deleteAll(aclModels);
    }
}
