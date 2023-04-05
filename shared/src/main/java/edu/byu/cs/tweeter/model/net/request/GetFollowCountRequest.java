package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class GetFollowCountRequest extends AuthorizedRequest{
    private String targetUser;

    public GetFollowCountRequest() {
        super();
    }

    public GetFollowCountRequest(AuthToken authToken, String targetUser) {
        super(authToken);
        this.targetUser = targetUser;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }
}
