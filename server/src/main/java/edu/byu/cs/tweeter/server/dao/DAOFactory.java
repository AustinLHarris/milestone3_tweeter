package edu.byu.cs.tweeter.server.dao;

public interface DAOFactory {
    public FollowDAOInterface getFollowingDAO();
    public AuthDAOInterface getAuthDAO();
    public UserDAOInterface getUserDAO();
    public FeedDAOInterface getFeedDAO();
    public StoryDAOInterface getStoryDAO();
}
