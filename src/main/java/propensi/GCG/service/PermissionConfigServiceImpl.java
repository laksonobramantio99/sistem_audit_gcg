package propensi.GCG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.GCG.exception.ForbiddenException;
import propensi.GCG.model.*;

import java.util.List;

@Service
public class PermissionConfigServiceImpl implements PermissionConfigService {
    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @Autowired
    private AclService aclService;

    @Override
    public void checkPermission(String namaMenu) {
        System.out.println("masuk method checkPermission");
        MenuModel menu = menuService.getByNama(namaMenu).get();

        UserModel user = userService.getCurrentUser();
        RoleModel role = user.getUserRole();
        List<ACLModel> listAcl = aclService.getByRole(role);

        boolean isForbidden = true;
        for (ACLModel acl: listAcl) {
            if (acl.getAclMenu() == menu) {
                isForbidden = false;
                break;
            }
        }

        if (isForbidden) {
            throw new ForbiddenException();
        }
    }
}
