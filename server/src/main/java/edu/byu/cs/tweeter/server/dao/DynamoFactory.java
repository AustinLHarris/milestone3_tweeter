package edu.byu.cs.tweeter.server.dao;

public class DynamoFactory implements DAOFactory {
    @Override
    public FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }

    @Override
    public AuthDAO getAuthDAO() {
        return new AuthDAO();
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDAO();
    }

    @Override
    public FeedDAO getFeedDAO() {
        return new FeedDAO();
    }

    @Override
    public StoryDAO getStoryDAO() {
        return new StoryDAO();
    }
}
