package propensi.GCG.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import propensi.GCG.model.UserModel;
import propensi.GCG.service.PermissionConfigService;
import propensi.GCG.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class    UserRestController {

    @Autowired
    UserService userService;

    @Autowired
    PermissionConfigService permissionConfigService;

    @GetMapping(value = "/isAvailable/{username}")
    private Map<String, Object> isAvailable(@PathVariable String username){
        UserModel user = userService.findUserByUserName(username);
        boolean isAvailable = false;
        if (user != null)
            isAvailable = true;

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("username", username);
        response.put("isAvailable", isAvailable);

        return response;
    }

    @PostMapping(value = "/aktivasi/{id}")
    public void activateUser(@PathVariable(value = "id") Long id) {
        UserModel user = userService.findUserById(id);
        user.setActiveFlag(true);
        userService.addUserWithoutEncryptPassword(user);
    }

    @PostMapping(value = "/deaktivasi/{id}")
    public void deactivateUser(@PathVariable(value = "id") Long id) {
        UserModel user = userService.findUserById(id);
        user.setActiveFlag(false);
        userService.addUserWithoutEncryptPassword(user);
    }

    @GetMapping(value = "/validatePassword/{pw}")
    public Map<String, Object> validatePassword(@PathVariable(value = "pw") String pw) {
        UserModel user = userService.getCurrentUser();
        Boolean matches = userService.validatePassword(pw, user.getPassword());

        HashMap<String, Object> response =  new HashMap<>();
        response.put("isMatches", matches);

        return response;
    }
}
