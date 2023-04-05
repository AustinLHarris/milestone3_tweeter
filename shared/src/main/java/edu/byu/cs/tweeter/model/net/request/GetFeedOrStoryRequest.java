package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class GetFeedOrStoryRequest extends AuthorizedRequest{
    private String targetUser;
    private int limit;
    private String lastItem;

    public GetFeedOrStoryRequest() {
        super();
    }

    public GetFeedOrStoryRequest(AuthToken authToken, String targetUser, int limit, String lastStatus) {
        super(authToken);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastStatus;
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

    public String getLastItem() {
        return lastItem;
    }

    public void setLastItem(String lastStatus) {
        this.lastItem = lastStatus;
    }
}
