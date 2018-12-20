package com.forum.services.dao;


import com.forum.services.constants.MessageConstants;
import com.forum.services.constants.TopicConstants;
import com.forum.services.exceptions.TopicException;
import com.forum.services.model.Comment;
import com.forum.services.model.Topic;
import com.forum.services.model.User;
import com.forum.services.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TopicDaoImpl implements TopicDao {
    private final String GET_ALL_TOPIC_SQL = "select t.id_topic, t.title, t.description, t.share_date, t.view_count, t.status, u.id_user, u.email, u.first_name, u.last_name, u.img, c.id_comment, c.description, c.write_date from topic t inner join user u on t.id_user = u.id_user left join comment c on c.id_topic = t.id_topic where t.status = ? order by t.share_date desc";
    private final String GET_TOPIC_BY_ID_SQL = "select t.id_topic, t.title, t.description as t_description, t.share_date, t.view_count, t.status, u.id_user as t_id_user, u.first_name as t_first_name, u.img as t_img, u.last_name as t_last_name  from topic t inner join user u on t.id_user=u.id_user where t.id_topic=? and t.status = ? ";
    private final String GET_POPULAR_TOPICS_SQL = "select t.id_topic, t.title, count(c.id_comment) as comments from topic t left join comment c on t.id_topic=c.id_topic where t.status = ? group by t.title having comments>0  order by comments desc limit 7";
    private final String ADD_TOPIC_SQL = "insert into topic(title, description, share_date, view_count, id_user, status) values(?, ?, ?, ?, ?, ?)";
    private final String UPDATE_TOPIC_VIEW_COUNT_SQL = "update topic set view_count=view_count+1 where id_topic=?";
    private final String GET_SIMILAR_TOPICS_SQL = "select t.id_topic, t.title, t.description, t.share_date, t.view_count, u.id_user, u.email, u.first_name, u.last_name, u.img, c.id_comment, c.description, c.write_date from topic t inner join user u on t.id_user = u.id_user left join comment c on c.id_topic = t.id_topic where t.status = ?";
    private final String GET_COMMENTS_BY_TOPIC_ID_SQL = "select * from comment c inner join user u on c.id_user=u.id_user where c.id_topic=? order by write_date asc";
    private final String ADD_COMMENT_SQL = "insert into comment(description, write_date, id_topic, id_user) values(?,?,?,?)";
    private final String GET_TOPICS_BY_USER_ID_SQL = "select id_topic, title from topic where id_user=? and status = ? order by share_date desc limit 7";

    @Override
    public List<Topic> getAllTopic() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Topic> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(GET_ALL_TOPIC_SQL);
            ps.setInt(1, TopicConstants.TOPIC_STATUS_ACTIVE);
            rs = ps.executeQuery();
            Map<Integer, Topic> map = new LinkedHashMap<>();
            while (rs.next()) {
                Topic t = map.get(rs.getInt("id_topic"));

                if (t == null) {
                    t = new Topic();
                    t.setId(rs.getInt("id_topic"));
                    t.setTitle(rs.getString("title"));
                    t.setDesc(rs.getString("description"));
                    t.setShareDate(rs.getTimestamp("share_date").toLocalDateTime());
                    t.setViewCount(rs.getInt("view_count"));
                    t.setStatus(rs.getInt("status"));

                    User u = new User();
                    u.setId(rs.getInt("id_user"));
                    u.setEmail(rs.getString("email"));
                    u.setFirstName(rs.getString("first_name"));
                    u.setLastName(rs.getString("last_name"));
                    u.setImagePath(rs.getString("img"));
                    t.setUser(u);
                    map.put(t.getId(), t);
                }

                if (rs.getInt("id_comment") != 0) {
                    Comment c = new Comment();
                    c.setId(rs.getInt("id_comment"));
                    c.setDesc(rs.getString("description"));
                    c.setWriteDate(rs.getTimestamp("write_date").toLocalDateTime());
                    t.addComment(c);
                }
            }

            list = new ArrayList<>(map.values());


        } finally {
            DbUtil.closeAll(con, ps, rs);
        }

        return list;
    }

    @Override
    public Topic getTopicById(int id) throws SQLException, TopicException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Topic topic = null;

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(GET_TOPIC_BY_ID_SQL);
            ps.setInt(1, id);
            ps.setInt(2, TopicConstants.TOPIC_STATUS_ACTIVE);
            rs = ps.executeQuery();
            if (rs.next()) {
                topic = new Topic();
                topic.setId(rs.getInt("id_topic"));
                topic.setTitle(rs.getString("title"));
                topic.setDesc(rs.getString("t_description"));
                topic.setShareDate(rs.getTimestamp("share_date").toLocalDateTime());
                topic.setViewCount(rs.getInt("view_count"));

                User user = new User();
                user.setId(rs.getInt("t_id_user"));
                user.setFirstName(rs.getString("t_first_name"));
                user.setLastName(rs.getString("t_last_name"));
                user.setImagePath(rs.getString("t_img"));
                topic.setUser(user);

            }else{
                throw new TopicException(MessageConstants.ERROR_TOPIC_NOT_FOUND);
            }

        } finally {
            DbUtil.closeAll(con, ps, rs);
        }
        return topic;
    }

    @Override
    public List<Topic> getPopularTopics() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Topic> listTopic = new ArrayList<>();
        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(GET_POPULAR_TOPICS_SQL);
            ps.setInt(1, TopicConstants.TOPIC_STATUS_ACTIVE);
            rs = ps.executeQuery();
            while (rs.next()) {
                Topic topic = new Topic();
                topic.setId(rs.getInt("id_topic"));
                topic.setTitle(rs.getString("title"));
                topic.setCommentCount(rs.getInt("comments"));
                listTopic.add(topic);
            }
        } finally {
            DbUtil.closeAll(con, ps, rs);
        }
        return listTopic;
    }

    @Override
    public Topic addTopic(Topic topic) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(ADD_TOPIC_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, topic.getTitle());
            ps.setString(2, topic.getDesc());
            ps.setString(3, topic.getShareDate().toString());
            ps.setInt(4, topic.getViewCount());
            ps.setInt(5, topic.getUser().getId());
            ps.setInt(6, topic.getStatus());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                topic.setId(rs.getInt(1));
            }
        } finally {
            DbUtil.closeAll(con, ps);
        }
        return topic;
    }

    @Override
    public void updateTopicViewCount(int topicId) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(UPDATE_TOPIC_VIEW_COUNT_SQL);
            ps.setInt(1, topicId);
            ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeAll(con, ps);
        }
    }

    @Override
    public List<Topic> getSimilarTopics(String[] keywords) throws SQLException {
        if (keywords.length == 0) {
            return new ArrayList<>();
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Topic> listTopic = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder(GET_SIMILAR_TOPICS_SQL);
        stringBuilder.append(" and (");
        for (int i = 0; i < keywords.length; i++) {
            stringBuilder.append(" t.title like ?");
            if (i < keywords.length - 1) {
                stringBuilder.append(" or");
            }
        }
        stringBuilder.append(")");
        stringBuilder.append(" order by t.share_date desc");
        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(stringBuilder.toString());
            ps.setInt(1, TopicConstants.TOPIC_STATUS_ACTIVE);
            for (int i = 0; i < keywords.length; i++) {
                ps.setString(i + 2, "%" + keywords[i] + "%");
            }
            rs = ps.executeQuery();
            Map<Integer, Topic> map = new LinkedHashMap<>();
            while (rs.next()) {
                Topic t = map.get(rs.getInt("id_topic"));

                if (t == null) {
                    t = new Topic();
                    t.setId(rs.getInt("id_topic"));
                    t.setTitle(rs.getString("title"));
                    t.setDesc(rs.getString("description"));
                    t.setShareDate(rs.getTimestamp("share_date").toLocalDateTime());
                    t.setViewCount(rs.getInt("view_count"));

                    User u = new User();
                    u.setId(rs.getInt("id_user"));
                    u.setEmail(rs.getString("email"));
                    u.setFirstName(rs.getString("first_name"));
                    u.setLastName(rs.getString("last_name"));
                    u.setImagePath(rs.getString("img"));
                    t.setUser(u);
                    map.put(t.getId(), t);
                }

                if (rs.getInt("id_comment") != 0) {
                    Comment c = new Comment();
                    c.setId(rs.getInt("id_comment"));
                    c.setDesc(rs.getString("description"));
                    c.setWriteDate(rs.getTimestamp("write_date").toLocalDateTime());
                    t.addComment(c);
                }
            }

            listTopic = new ArrayList<>(map.values());

        } finally {
            DbUtil.closeAll(con, ps, rs);
        }

        return listTopic;
    }

    @Override
    public List<Comment> getCommentsByTopicId(int id) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Comment> listComment = new ArrayList<>();
        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(GET_COMMENTS_BY_TOPIC_ID_SQL);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                Comment comment = new Comment();
                comment.setId(rs.getInt("id_comment"));
                comment.setDesc(rs.getString("description"));
                comment.setWriteDate(rs.getTimestamp("write_date").toLocalDateTime());
                User user = new User();
                user.setId(rs.getInt("id_user"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setImagePath(rs.getString("img"));
                comment.setUser(user);
                listComment.add(comment);
            }
        } finally {
            DbUtil.closeAll(con, ps, rs);
        }
        return listComment;
    }

    @Override
    public Comment addComment(Comment comment) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(ADD_COMMENT_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, comment.getDesc());
            ps.setString(2, comment.getWriteDate().toString());
            ps.setInt(3, comment.getTopic().getId());
            ps.setInt(4, comment.getUser().getId());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                comment.setId(rs.getInt(1));
            }
        } finally {
            DbUtil.closeAll(con, ps);
        }

        return comment;
    }

    @Override
    public List<Topic> getTopicByUserId(int idUser) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Topic> listTopics = new ArrayList<>();

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(GET_TOPICS_BY_USER_ID_SQL);
            ps.setInt(1, idUser);
            ps.setInt(2, TopicConstants.TOPIC_STATUS_ACTIVE);
            rs = ps.executeQuery();
            while (rs.next()) {
                Topic topic = new Topic();
                topic.setId(rs.getInt("id_topic"));
                topic.setTitle(rs.getString("title"));
                listTopics.add(topic);
            }


        } finally {
            DbUtil.closeAll(con, ps);
        }
        return listTopics;
    }

}
