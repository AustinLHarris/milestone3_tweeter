package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowCountRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowCountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getFollowingDAO().getFollowees(request);
    }


    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowers(FollowersRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getFollowingDAO().getFollowers(request);
    }

    public FollowUserResponse followUser(FollowUserRequest request) {
        if(request.getAliasToToggleFollow() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user to follow");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request missing authtoken");
        }
        return getFollowingDAO().followUser(request);
    }

    public FollowUserResponse unfollowUser(FollowUserRequest request) {
        if(request.getAliasToToggleFollow() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user to unfollow");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request missing authtoken");
        }
        return getFollowingDAO().unfollowUser(request);
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }


    public GetFollowCountResponse getFollowersCount(GetFollowCountRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request missing authtoken");
        }
        return getFollowingDAO().getFollowerCount(request.getTargetUser());
    }

    public GetFollowCountResponse getFollowingCount(GetFollowCountRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request missing authtoken");
        }
        return getFollowingDAO().getFolloweeCount(request.getTargetUser());
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if(request.getFollower() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower to check");
        }else if(request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have followed user");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request missing authtoken");
        }
        return getFollowingDAO().isFollower(request);
    }
}
