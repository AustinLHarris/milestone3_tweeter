package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowUserRequest extends AuthorizedRequest{

    private String aliasToToggleFollow;

    public FollowUserRequest() {
        super();
    }

    public FollowUserRequest(AuthToken authToken, String aliasToFollow) {
        super(authToken);
        this.aliasToToggleFollow = aliasToFollow;
    }


    public String getAliasToToggleFollow() {
        return aliasToToggleFollow;
    }

    public void setAliasToToggleFollow(String aliasToFollow) {
        this.aliasToToggleFollow = aliasToFollow;
    }
}
