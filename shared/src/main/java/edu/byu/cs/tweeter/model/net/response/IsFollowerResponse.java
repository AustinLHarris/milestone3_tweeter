package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends Response{
    private boolean isFollower;

    public IsFollowerResponse(boolean isFollower) {
        super(true);
        this.isFollower = isFollower;
    }

    IsFollowerResponse(String message) {
        super(false, message);
    }

    public boolean isFollower() {
        return isFollower;
    }

    public void setFollower(boolean follower) {
        isFollower = follower;
    }
}
