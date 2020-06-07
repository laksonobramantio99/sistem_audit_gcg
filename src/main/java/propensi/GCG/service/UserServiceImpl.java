package propensi.GCG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import propensi.GCG.model.DivisiModel;
import propensi.GCG.model.UserModel;
import propensi.GCG.repository.UserDB;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDB userDB;

    @Override
    public UserModel addUser(UserModel user) {
        String pass = encrypt(user.getPassword());
        user.setPassword(pass);
        return userDB.save(user);
    }

    @Override
    public UserModel addUserWithoutEncryptPassword(UserModel user) {
        return userDB.save(user);
    }

    @Override
    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public UserModel findUserByUserName(String username) {
        Optional<UserModel> user = userDB.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public Boolean validatePassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(rawPassword, encodedPassword)) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<UserModel> getAllUser() {
        return userDB.findAll();
    }

    @Override
    public UserModel getCurrentUser() {
        String currentUsername = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUsername = authentication.getName();
        }
        return findUserByUserName(currentUsername);
    }

    @Override
    public UserModel findUserById(Long id) {
        Optional<UserModel> user = userDB.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public UserModel getUserByUsername(String username) {
        return userDB.findByUsername(username).get();
    }

    @Override
    public List<UserModel> getListUserByDivisi(DivisiModel divisiModel) {
        return userDB.findByUserDivisi(divisiModel);
    }


}
