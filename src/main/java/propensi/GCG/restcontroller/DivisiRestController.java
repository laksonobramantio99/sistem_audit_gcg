package propensi.GCG.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import propensi.GCG.model.DivisiModel;
import propensi.GCG.model.UserModel;
import propensi.GCG.service.DivisiService;
import propensi.GCG.service.PermissionConfigService;
import propensi.GCG.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/divisi")
public class DivisiRestController {
    @Autowired
    DivisiService divisiService;

    @Autowired
    UserService userService;

    @Autowired
    PermissionConfigService permissionConfigService;

    @GetMapping(value = "/hasUser/{id}")
    private Map<String, Object> hasUser(@PathVariable Long id){
        DivisiModel divisiModel = divisiService.getDivisiByIdDivisi(id).get();
        List<UserModel> listUser = userService.getListUserByDivisi(divisiModel);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("id", id);
        response.put("hasUser", !(listUser.isEmpty()));

        return response;
    }

    @PostMapping(value = "/hapus/{id}")
    public void deleteDivisi(@PathVariable(value = "id") Long id) {
        DivisiModel divisiModel = divisiService.getDivisiByIdDivisi(id).get();
        divisiService.deleteDivisi(divisiModel);
    }
}
