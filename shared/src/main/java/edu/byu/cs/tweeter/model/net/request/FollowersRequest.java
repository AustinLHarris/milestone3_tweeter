package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowersRequest extends AuthorizedRequest {

    private String followerAlias;
    private int limit;
    public String lastFollowerAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowersRequest() {
        super();
    }

    /**
     * Creates an instance.
     *
     * @param followerAlias the alias of the user whose followees are to be returned.
     * @param limit the maximum number of followees to return.
     * @param lastFollowerAlias the alias of the last followee that was returned in the previous request (null if
     *                     there was no previous request or if no followees were returned in the
     *                     previous request).
     */
    public FollowersRequest(AuthToken authToken, String followerAlias, int limit, String lastFollowerAlias) {
        super(authToken);
        this.followerAlias = followerAlias;
        this.limit = limit;
        this.lastFollowerAlias = lastFollowerAlias;
    }



    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public String getFollowerAlias() {
        return followerAlias;
    }

    /**
     * Sets the follower.
     *
     * @param followerAlias the follower.
     */
    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    /**
     * Returns the number representing the maximum number of followees to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit the limit.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Returns the last followee that was returned in the previous request or null if there was no
     * previous request or if no followees were returned in the previous request.
     *
     * @return the last followee.
     */
    public String getLastFollowerAlias() {
        return lastFollowerAlias;
    }

    /**
     * Sets the last followee.
     *
     * @param lastFollowerAlias the last followee.
     */
    public void setLastFollowerAlias(String lastFollowerAlias) {
        this.lastFollowerAlias = lastFollowerAlias;
    }
}
