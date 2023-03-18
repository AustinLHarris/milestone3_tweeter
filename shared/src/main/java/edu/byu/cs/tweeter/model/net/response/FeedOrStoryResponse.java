package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class FeedOrStoryResponse extends PagedResponse{
    private List<Status> statuses;

    public FeedOrStoryResponse(List<Status> statuses, boolean hasMorePages) {
        super(true, hasMorePages);
        this.statuses = statuses;
    }

    FeedOrStoryResponse(boolean success, String message, boolean hasMorePages) {
        super(false, message, false);
    }

    public List<Status> getStatuses() {
        return statuses;
    }
}
