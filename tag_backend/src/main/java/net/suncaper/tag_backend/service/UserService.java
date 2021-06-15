package net.suncaper.tag_backend.service;

import net.suncaper.tag_backend.dao.UserDAO;
import net.suncaper.tag_backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public User get(String username, String password){
        return userDAO.getByUsernameAndPassword(username, password);
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
}
