package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.server.dao.DynamoFactory;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.FeedBatch;
import edu.byu.cs.tweeter.server.service.Service;
import edu.byu.cs.tweeter.util.Pair;

public class postUpdateFeedMessages implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            Gson gson = new Gson();
            Status status;
            Type collectionType = new TypeToken<Status>(){}.getType();
            status = gson.fromJson(msg.getBody(),collectionType);

            FollowersRequest request = new FollowersRequest(null, status.getUser().getAlias(), 25,null);
            Service service = new Service(new DynamoFactory());
            boolean hasMoreFollowers = true;
            while (hasMoreFollowers){
                Pair<List<String>, Boolean> result =  service.getFollowersHelper(request);
                FeedBatch batch = new FeedBatch(status,result.getFirst());
                hasMoreFollowers = result.getSecond();
                request.setLastFollowerAlias(result.getFirst().get(result.getFirst().size() - 1));
                sendMessageToQueue(gson.toJson(batch));
            }
        }
        return null;
    }

    private void sendMessageToQueue(String messageBody){
        String queueUrl = "https://sqs.us-east-2.amazonaws.com/921853240226/updateFeedQueue";

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

    }
}
