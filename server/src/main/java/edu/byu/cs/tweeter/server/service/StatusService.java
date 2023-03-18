package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.GetFeedOrStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FeedOrStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class StatusService {

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if(request.getStatus().getPost() == null){
            throw new RuntimeException("[Bad Request] Missing message");
        } else if(request.getStatus().getUser() == null) {
            throw new RuntimeException("[Bad Request] Missing user who posted");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing authtoken");
        }
        //TODO: Replace with actual connection to the database
        return new PostStatusResponse();

    }

    public FeedOrStoryResponse getStory(GetFeedOrStoryRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getStatusDAO().getStory(request);
    }

    StatusDAO getStatusDAO() {return new StatusDAO();}

    public FeedOrStoryResponse getFeed(GetFeedOrStoryRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getStatusDAO().getFeed(request);
    }
}
