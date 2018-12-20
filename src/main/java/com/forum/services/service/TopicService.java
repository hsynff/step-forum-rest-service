package com.forum.services.service;

import com.forum.services.exceptions.TopicException;
import com.forum.services.model.Comment;
import com.forum.services.model.Topic;

import java.sql.SQLException;
import java.util.List;

public interface TopicService {
    List<Topic> getAllTopic() throws SQLException;
    Topic getTopicById(int id) throws SQLException, TopicException;
    List<Topic> getPopularTopics() throws SQLException;
    Topic addTopic(Topic topic) throws SQLException;
    void updateTopicViewCount(int topicId) throws SQLException;
    List<Topic> getSimilarTopics(String[] keywords) throws SQLException;
    List<Comment> getCommentsByTopicId(int id) throws SQLException;
    Comment addComment(Comment comment) throws SQLException;
    List<Topic> getTopicByUserId(int idUser) throws SQLException;
}
