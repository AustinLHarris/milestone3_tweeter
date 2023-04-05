package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFeedOrStoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedOrStoryResponse;
import edu.byu.cs.tweeter.server.dao.DynamoFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetStoryHandler implements RequestHandler<GetFeedOrStoryRequest, FeedOrStoryResponse> {
    @Override
    public FeedOrStoryResponse handleRequest(GetFeedOrStoryRequest request, Context context) {
        StatusService service = new StatusService(new DynamoFactory());
        return service.getStory(request);
    }
}
