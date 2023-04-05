package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DynamoStoryStatus;

public interface StoryDAOInterface {
    public void postStatus(User poster, List<String> mentions, String post, String timestamp, List<String> urls);


    public DataPage<DynamoStoryStatus> getPageOfStory(String alias, int pageSize, String lastStatusTimestamp);
}
