package edu.byu.cs.tweeter.model.net.response;

public class GetFollowCountResponse extends Response{

    private int count;


    public GetFollowCountResponse(int count) {
        super(true);
        this.count = count;
    }

    GetFollowCountResponse(String message) {
        super(false, message);
    }

    public int getCount() {
        return count;
    }
}
