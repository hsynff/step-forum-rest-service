package com.forum.services.service;


import com.forum.services.dao.UserDao;
import com.forum.services.exceptions.UserException;
import com.forum.services.model.Action;
import com.forum.services.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserByEmail(String email) throws SQLException, UserException {
        return userDao.getUserByEmail(email);
    }

    @Override
    public User addUser(User user) throws SQLException, UserException {
       return userDao.addUser(user);
    }

    @Override
    public List<Action> getActionListByRoleId(int idRole) throws SQLException {
        return userDao.getActionListByRoleId(idRole);
    }
}
