package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class GetFeedOrStoryRequest {
    private AuthToken authToken;
    private String targetUser;
    private int limit;
    private String lastItem;

    public GetFeedOrStoryRequest() {
    }

    public GetFeedOrStoryRequest(AuthToken authToken, String targetUser, int limit, String lastStatus) {
        this.authToken = authToken;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastStatus;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getLastStatus() {
        return lastItem;
    }

    public void setLastStatus(String lastStatus) {
        this.lastItem = lastStatus;
    }
}
