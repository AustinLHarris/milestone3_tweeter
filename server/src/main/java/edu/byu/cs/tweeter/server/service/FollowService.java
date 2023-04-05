package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowCountRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowCountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DynamoUser;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.Follows;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends Service{

    public FollowService(DAOFactory factory) {
        super(factory);
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
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        checkToken(request);
        return createFollowingResponse(getFolloweesHelper(request));
    }

    private FollowingResponse createFollowingResponse(Pair<List<String>, Boolean> page) {
        List<User> responseUsers = new ArrayList<>(page.getFirst().size());
        for(String alias: page.getFirst()){
            DynamoUser dbUser = factory.getUserDAO().getUser(alias);
            responseUsers.add(new User(dbUser.getFirstName(),dbUser.getLastName(),dbUser.getUser_handle(),dbUser.getImageURL()));
        }
        return new FollowingResponse(responseUsers, page.getSecond());
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
        }else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Missing Authorization] Missing authtoken");
        }
        checkToken(request);

        return createFollowingResponse(getFollowersHelper(request));
    }


    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */


    public FollowUserResponse followUser(FollowUserRequest request) {
        if(request.getAliasToToggleFollow() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user to follow");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Missing Authorization] Missing authtoken");
        }
        checkToken(request);
        String user = getUserFromToken(request.getAuthToken());
        factory.getFollowingDAO().recordFollow(user, request.getAliasToToggleFollow());
        // Adjust following count for follower
        DynamoUser dbFollower = factory.getUserDAO().getUser(user);
        dbFollower.setNumFollowing(dbFollower.getNumFollowing() + 1);
        factory.getUserDAO().putUser(dbFollower);
        // Adjust follower count for followee
        DynamoUser dbFollowee = factory.getUserDAO().getUser(request.getAliasToToggleFollow());
        dbFollowee.setNumFolllowers(dbFollowee.getNumFolllowers() + 1);
        factory.getUserDAO().putUser(dbFollowee);
        return new FollowUserResponse();
    }

    public FollowUserResponse unfollowUser(FollowUserRequest request) {
        if(request.getAliasToToggleFollow() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user to unfollow");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing authtoken");
        }
        checkToken(request);
        String user = getUserFromToken(request.getAuthToken());
        factory.getFollowingDAO().deleteFollow(user, request.getAliasToToggleFollow());
        // Adjust following count for follower
        DynamoUser dbFollower = factory.getUserDAO().getUser(user);
        dbFollower.setNumFollowing(dbFollower.getNumFollowing() - 1);
        factory.getUserDAO().putUser(dbFollower);
        // Adjust follower count for followee
        DynamoUser dbFollowee = factory.getUserDAO().getUser(request.getAliasToToggleFollow());
        dbFollowee.setNumFolllowers(dbFollowee.getNumFolllowers() - 1);
        factory.getUserDAO().putUser(dbFollowee);
        return new FollowUserResponse();
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */

    private String getUserFromToken(AuthToken token){
        return factory.getAuthDAO().getAuth(token.getToken()).getUser_handle();
    }


    public GetFollowCountResponse getFollowersCount(GetFollowCountRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user");
        }else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing authtoken");
        }
        checkToken(request);

        return new GetFollowCountResponse(factory.getUserDAO().getUser(request.getTargetUser()).getNumFolllowers());
    }

    public GetFollowCountResponse getFollowingCount(GetFollowCountRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user");
        }else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing authtoken");
        }
        checkToken(request);
        return new GetFollowCountResponse(factory.getUserDAO().getUser(request.getTargetUser()).getNumFollowing());
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if(request.getFollower() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower to check");
        }else if(request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have followed user");
        }else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing authtoken");
        }
        checkToken(request);
        Follows follow = factory.getFollowingDAO().getFollow(request.getFollower().getAlias(),request.getFollowee().getAlias());
        if(follow != null){
            System.out.println("Sending that this person is a follower");
            return new IsFollowerResponse(true);
        }
        else { return new IsFollowerResponse(false); }

    }
}
