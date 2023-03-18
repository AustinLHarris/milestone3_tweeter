package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedOrStoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedOrStoryResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusDAO {

    public FeedOrStoryResponse getStory(GetFeedOrStoryRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getTargetUser() != null;

        List<Status> allStatuses = getDummyStory();
        List<Status> responseStory = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allStatuses != null) {
                int statusIndex = getStatusStartingIndex(request.getLastStatus(), allStatuses);

                for(int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
                    responseStory.add(allStatuses.get(statusIndex));
                }

                hasMorePages = statusIndex < allStatuses.size();
            }
        }

        return new FeedOrStoryResponse(responseStory, hasMorePages);
    }

    List<Status> getDummyStory() {
        return getFakeData().getFakeStatuses();
    }

    FakeData getFakeData() {
        return FakeData.getInstance();
    }

    private int getStatusStartingIndex(String lastStatusKey, List<Status> allStatuses) {
        int statusIndex = 0;
        if(lastStatusKey != null) {
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatusKey.equals(allStatuses.get(i).getPost())) {
                    statusIndex = i + 1;
                    break;
                }
            }
        }
        return statusIndex;
    }

    public FeedOrStoryResponse getFeed(GetFeedOrStoryRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getTargetUser() != null;

        List<Status> allStatuses = getDummyStory();
        List<Status> responseStory = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allStatuses != null) {
                int statusIndex = getStatusStartingIndex(request.getLastStatus(), allStatuses);

                for(int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
                    responseStory.add(allStatuses.get(statusIndex));
                }

                hasMorePages = statusIndex < allStatuses.size();
            }
        }

        return new FeedOrStoryResponse(responseStory, hasMorePages);
    }
}
