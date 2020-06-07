package propensi.GCG.service;

import propensi.GCG.model.DivisiModel;
import propensi.GCG.model.UserModel;

import java.util.List;

public interface UserService {
    UserModel addUser(UserModel user);
    UserModel addUserWithoutEncryptPassword(UserModel user);
    String encrypt(String password);
    UserModel findUserByUserName(String username);
    Boolean validatePassword(String oldPassword, String newPassword);
    List<UserModel> getAllUser();
    UserModel findUserById(Long id);
    UserModel getCurrentUser();
    UserModel getUserByUsername(String username);
    List<UserModel> getListUserByDivisi(DivisiModel divisiModel);
}
