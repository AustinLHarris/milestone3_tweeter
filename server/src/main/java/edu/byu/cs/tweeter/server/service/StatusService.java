package edu.byu.cs.tweeter.server.service;

import static java.lang.Long.parseLong;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFeedOrStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FeedOrStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DynamoFeedStatus;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DynamoStoryStatus;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService extends Service{

    public StatusService(DAOFactory factory) {
        super(factory);
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if(request.getStatus().getPost() == null){
            throw new RuntimeException("[Bad Request] Missing message");
        } else if(request.getStatus().getUser() == null) {
            throw new RuntimeException("[Bad Request] Missing user who posted");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing authtoken");
        }
        checkToken(request);
        Status status = request.getStatus();
        User poster = status.getUser();
        Gson gson = new Gson();

        sendMessageToQueue(gson.toJson(status));
        factory.getStoryDAO().postStatus(poster, status.getMentions(), status.getPost(), status.getTimestamp().toString(), status.getUrls());
        return new PostStatusResponse();

    }

    private void sendMessageToQueue(String messageBody){
        String queueUrl = "https://sqs.us-east-2.amazonaws.com/921853240226/postStatusQueue";

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

    }

    public void postSingleFeed(String followerAlias, Status status){
        factory.getFeedDAO().postStatus(followerAlias, status.getUser(), status.getMentions(),status.getPost(), status.getTimestamp().toString(), status.getUrls());
    }



    public FeedOrStoryResponse getStory(GetFeedOrStoryRequest request) {
        checkErrors(request);
        Pair<List<Status>, Boolean> response = getStoryHelper(request);
        System.out.println("An example post" + response.getFirst().get(0).toString());
        return new FeedOrStoryResponse(response.getFirst(), response.getSecond());
    }

    public Pair<List<Status>, Boolean> getStoryHelper(GetFeedOrStoryRequest request) {
        assert request.getLimit() > 0;
        assert request.getTargetUser() != null;

        DataPage<DynamoStoryStatus> story =  factory.getStoryDAO().getPageOfStory(request.getTargetUser(),request.getLimit(),request.getLastItem());
        List<Status> returnStory = new ArrayList<>(request.getLimit());
        for(DynamoStoryStatus status: story.getValues()){
            User returnUser = new User(status.getFirstName(),status.getLastName(),status.getUser(),status.getImageURL());
            Status newStatus = new Status(status.getPost(), returnUser, parseLong(status.getTimestamp()), status.getUrls(), status.getMentions());
            returnStory.add(newStatus);
        }

        return new Pair<>(returnStory, story.isHasMorePages());
    }


    public FeedOrStoryResponse getFeed(GetFeedOrStoryRequest request) {
        checkErrors(request);
        Pair<List<Status>, Boolean> response = getFeedHelper(request);
        return new FeedOrStoryResponse(response.getFirst(), response.getSecond());
    }

    private void checkErrors(GetFeedOrStoryRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing authtoken");
        }
        checkToken(request);
    }

    public Pair<List<Status>, Boolean> getFeedHelper(GetFeedOrStoryRequest request) {
        assert request.getLimit() > 0;
        assert request.getTargetUser() != null;

        DataPage<DynamoFeedStatus> story =  factory.getFeedDAO().getPageOfFeed(request.getTargetUser(),request.getLimit(),request.getLastItem());
        List<Status> returnStory = new ArrayList<>(request.getLimit());
        for(DynamoFeedStatus status: story.getValues()){
            User returnUser = new User(status.getFirstName(),status.getLastName(),status.getUser(),status.getImageURL());
            Status newStatus = new Status(status.getPost(), returnUser, parseLong(status.getTimestamp()), status.getUrls(), status.getMentions());
            returnStory.add(newStatus);
        }

        return new Pair<>(returnStory, story.isHasMorePages());
    }
}
