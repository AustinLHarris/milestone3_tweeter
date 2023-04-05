package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.Follows;

public interface FollowDAOInterface {

    public Follows getFollow(String follower, String followee);
    public void recordFollow(String follower, String followee);
    public void putFollow(String follower, String followee, String newFollowee);
    public void deleteFollow(String follower, String followee);
    public DataPage<Follows> getPageOfFollowees(String followee, int pageSize, String lastFollower);
    public DataPage<Follows>  getPageOfFollowers(String follower, int pageSize, String lastFollowee);

    void addFollowersBatch(List<Follow> followers);
}
