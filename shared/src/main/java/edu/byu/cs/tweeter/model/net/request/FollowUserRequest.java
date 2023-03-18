package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowUserRequest {

    private AuthToken authToken;
    private String aliasToToggleFollow;

    public FollowUserRequest() {
    }

    public FollowUserRequest(AuthToken authToken, String aliasToFollow) {
        this.authToken = authToken;
        this.aliasToToggleFollow = aliasToFollow;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getAliasToToggleFollow() {
        return aliasToToggleFollow;
    }

    public void setAliasToToggleFollow(String aliasToFollow) {
        this.aliasToToggleFollow = aliasToFollow;
    }
}
