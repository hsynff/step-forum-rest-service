package com.forum.services.dao;


import com.forum.services.exceptions.UserException;
import com.forum.services.model.Action;
import com.forum.services.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    User getUserByEmail(String email) throws UserException, SQLException;
    User addUser(User user) throws UserException, SQLException;
    List<Action> getActionListByRoleId(int idRole) throws SQLException;
}
