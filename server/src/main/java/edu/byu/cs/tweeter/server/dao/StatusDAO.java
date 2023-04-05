//package edu.byu.cs.tweeter.server.dao;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import edu.byu.cs.tweeter.model.domain.Status;
//import edu.byu.cs.tweeter.model.net.request.GetFeedOrStoryRequest;
//import edu.byu.cs.tweeter.model.net.response.FeedOrStoryResponse;
//import edu.byu.cs.tweeter.util.FakeData;
//
//public class StatusDAO implements DatabaseDAO{
//
//
//
//    public FeedOrStoryResponse getStory(GetFeedOrStoryRequest request) {
//        // TODO: Generates dummy data. Replace with a real implementation.
//        assert request.getLimit() > 0;
//        assert request.getTargetUser() != null;
//
//        List<Status> allStatuses = getDummyStory();
//        List<Status> responseStory = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if(request.getLimit() > 0) {
//            if (allStatuses != null) {
//                int statusIndex = getStatusStartingIndex(request.getLastItem(), allStatuses);
//
//                for(int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
//                    responseStory.add(allStatuses.get(statusIndex));
//                }
//
//                hasMorePages = statusIndex < allStatuses.size();
//            }
//        }
//        return new FeedOrStoryResponse(responseStory, hasMorePages);
//    }
//
//    List<Status> getDummyStory() {
//        return getFakeData().getFakeStatuses();
//    }
//
//    FakeData getFakeData() {
//        return FakeData.getInstance();
//    }
//
//    private int getStatusStartingIndex(String lastStatusKey, List<Status> allStatuses) {
//        int statusIndex = 0;
//        if(lastStatusKey != null) {
//            for (int i = 0; i < allStatuses.size(); i++) {
//                if(lastStatusKey.equals(allStatuses.get(i).getPost())) {
//                    statusIndex = i + 1;
//                    break;
//                }
//            }
//        }
//        return statusIndex;
//    }
//
//    public FeedOrStoryResponse getFeed(GetFeedOrStoryRequest request) {
//        // TODO: Generates dummy data. Replace with a real implementation.
//        assert request.getLimit() > 0;
//        assert request.getTargetUser() != null;
//
//        List<Status> allStatuses = getDummyStory();
//        List<Status> responseStory = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if(request.getLimit() > 0) {
//            if (allStatuses != null) {
//                int statusIndex = getStatusStartingIndex(request.getLastItem(), allStatuses);
//
//                for(int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
//                    responseStory.add(allStatuses.get(statusIndex));
//                }
//
//                hasMorePages = statusIndex < allStatuses.size();
//            }
//        }
//
//        return new FeedOrStoryResponse(responseStory, hasMorePages);
//    }
//    private static boolean isNonEmptyString(String value) {
//        return (value != null && value.length() > 0);
//    }
//
//
////    public DataPage<DynamoFeedStatus> getPageOfStatus(String followee, int pageSize, String lastFollower) {
////        DynamoDbTable<Follows> table = enhancedClient.table(TableName, TableSchema.fromBean(Follows.class));
////        Key key = Key.builder()
////                .partitionValue(followee)
////                .build();
////
////        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
////                .queryConditional(QueryConditional.keyEqualTo(key))
////                .limit(pageSize);
////
////        if(isNonEmptyString(lastFollower)) {
////            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
////            Map<String, AttributeValue> startKey = new HashMap<>();
////            startKey.put(FolloweeAttr, AttributeValue.builder().s(followee).build());
////            startKey.put(FollowerAttr, AttributeValue.builder().s(lastFollower).build());
////
////            requestBuilder.exclusiveStartKey(startKey);
////            requestBuilder.scanIndexForward(true);
////        }
////
////        QueryEnhancedRequest request = requestBuilder.build();
////
////        DataPage<Follows> result = new DataPage<Follows>();
////
////        PageIterable<Follows> pages = table.query(request);
////        pages.stream()
////                .limit(1)
////                .forEach((Page<Follows> page) -> {
////                    result.setHasMorePages(page.lastEvaluatedKey() != null);
////                    page.items().forEach(Follows -> result.getValues().add(Follows));
////                });
////
////        return result;
////    }
//
//}
