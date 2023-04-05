package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import edu.byu.cs.tweeter.server.dao.DynamoFactory;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.FeedBatch;
import edu.byu.cs.tweeter.server.service.StatusService;

public class UpdateFeeds implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        StatusService service = new StatusService(new DynamoFactory());
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            Gson gson = new Gson();
            FeedBatch batch;
            Type collectionType = new TypeToken<FeedBatch>(){}.getType();
            batch = gson.fromJson(msg.getBody(),collectionType);
            for(String follower: batch.getTargetAliases()){
                service.postSingleFeed(follower,batch.getStatus());
            }
        }
        return null;
    }
}
