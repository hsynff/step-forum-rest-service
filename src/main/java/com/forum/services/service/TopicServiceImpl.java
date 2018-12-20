package com.forum.services.service;

import com.forum.services.dao.TopicDao;
import com.forum.services.exceptions.TopicException;
import com.forum.services.model.Comment;
import com.forum.services.model.Topic;

import java.sql.SQLException;
import java.util.List;

public class TopicServiceImpl implements TopicService {

    private TopicDao topicDao;

    public TopicServiceImpl(TopicDao topicDao){
        this.topicDao = topicDao;
    }


    @Override
    public List<Topic> getAllTopic() throws SQLException {
        return topicDao.getAllTopic();
    }

    @Override
    public Topic getTopicById(int id) throws SQLException, TopicException {
        return topicDao.getTopicById(id);
    }

    @Override
    public List<Topic> getPopularTopics()throws SQLException {
        return topicDao.getPopularTopics();
    }

    @Override
    public Topic addTopic(Topic topic)throws SQLException {
        return topicDao.addTopic(topic);
    }

    @Override
    public void updateTopicViewCount(int topicId)throws SQLException {
        topicDao.updateTopicViewCount(topicId);
    }

    @Override
    public List<Topic> getSimilarTopics(String[] keywords)throws SQLException {
        return topicDao.getSimilarTopics(keywords);
    }

    @Override
    public List<Comment> getCommentsByTopicId(int id)throws SQLException {
        return topicDao.getCommentsByTopicId(id);
    }

    @Override
    public Comment addComment(Comment comment)throws SQLException {
        return topicDao.addComment(comment);
    }

    @Override
    public List<Topic> getTopicByUserId(int idUser)throws SQLException {
        return topicDao.getTopicByUserId(idUser);
    }
}
