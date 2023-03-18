package edu.byu.cs.tweeter.model.net.response;

public class FollowUserResponse extends Response{
    public FollowUserResponse() {
        super(true);
    }

    public FollowUserResponse(String message) {
        super(false, message);
    }
}
