package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFeedOrStoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedOrStoryResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetFeedHandler implements RequestHandler<GetFeedOrStoryRequest, FeedOrStoryResponse> {
    @Override
    public FeedOrStoryResponse handleRequest(GetFeedOrStoryRequest request, Context context) {
        StatusService service = new StatusService();
        return service.getFeed(request);
    }
}
