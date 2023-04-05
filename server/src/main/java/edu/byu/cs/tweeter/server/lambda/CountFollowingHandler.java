package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFollowCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowCountResponse;
import edu.byu.cs.tweeter.server.dao.DynamoFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class CountFollowingHandler implements RequestHandler<GetFollowCountRequest, GetFollowCountResponse> {
    @Override
    public GetFollowCountResponse handleRequest(GetFollowCountRequest input, Context context) {
        FollowService service = new FollowService(new DynamoFactory());
        return service.getFollowingCount(input);
    }
}
