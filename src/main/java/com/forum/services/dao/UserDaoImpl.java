package com.forum.services.dao;

import com.forum.services.constants.MessageConstants;
import com.forum.services.exceptions.UserException;
import com.forum.services.model.Action;
import com.forum.services.model.Role;
import com.forum.services.model.User;
import com.forum.services.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private final String GET_USER_COUNT_BY_EMAIL = "select count(*) from user where email = ?";
    private final String INSERT_NEW_USER_SQL = "insert into user(email, password, token, status, id_role, first_name, last_name, img) values (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String GET_USER_BY_EMAIL_SQL = "select * from user where email = ?";
    private final String GET_ACTION_LIST_BY_ROLE_ID_SQL = "select * from action a inner join role_action ra on a.id_action=ra.id_action where ra.id_role=?";

    @Override
    public User getUserByEmail(String email) throws UserException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(GET_USER_BY_EMAIL_SQL);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id_user"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setImagePath(rs.getString("img"));
                user.setStatus(rs.getInt("status"));
                Role role = new Role();
                role.setId(rs.getInt("id_role"));
                user.setRole(role);
            } else {
                throw new UserException(MessageConstants.ERROR_USER_NOT_FOUND);
            }
        } finally {
            DbUtil.closeAll(con, ps, rs);
        }
        return user;
    }

    @Override
    public User addUser(User user) throws UserException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (!isEmailValid(user.getEmail())) {
                throw new UserException(MessageConstants.ERROR_DUPLICATE_EMAIL);
            }
            con = DbUtil.getConnection();
            ps = con.prepareStatement(INSERT_NEW_USER_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getToken());
            ps.setInt(4, user.getStatus());
            ps.setInt(5, user.getRole().getId());
            ps.setString(6, user.getFirstName());
            ps.setString(7, user.getLastName());
            ps.setString(8, user.getImagePath());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()){
                user.setId(rs.getInt(1));
            }
        } finally {
            DbUtil.closeAll(con, ps);
        }

        return user;
    }

    @Override
    public List<Action> getActionListByRoleId(int idRole) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Action> list = new ArrayList<>();
        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(GET_ACTION_LIST_BY_ROLE_ID_SQL);
            ps.setInt(1, idRole);
            rs = ps.executeQuery();
            while (rs.next()) {
                Action action = new Action();
                action.setId(rs.getInt("id_action"));
                action.setActionType(rs.getString("action_type"));
                list.add(action);

            }

        } finally {
            DbUtil.closeAll(con, ps, rs);
        }
        return list;
    }

    private boolean isEmailValid(String email) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(GET_USER_COUNT_BY_EMAIL);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()){
                result = rs.getInt(1)==0;
            }
        } finally {
            DbUtil.closeAll(con, ps);
        }
        return result;
    }

}
