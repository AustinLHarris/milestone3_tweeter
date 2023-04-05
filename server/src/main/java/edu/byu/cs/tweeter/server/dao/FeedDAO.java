package edu.byu.cs.tweeter.server.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DynamoFeedStatus;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class FeedDAO implements DatabaseDAO, FeedDAOInterface {

    private static final String TableName = "feed";
    private static final String aliasAttr = "user";
    private static final String timestampAttr = "timestamp";



    public void postStatus(String viewer, User poster, List<String> mentions, String post, String timestamp, List<String> urls) {
        DynamoDbTable<DynamoFeedStatus> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoFeedStatus.class));
        Key key = Key.builder()
                .partitionValue(viewer).sortValue(timestamp)
                .build();

        // load it if it exists
        DynamoFeedStatus status = table.getItem(key);
        if(status != null) {
            return;
        } else {
            DynamoFeedStatus newStatus = new DynamoFeedStatus();
            newStatus.setUser(viewer);
            newStatus.setPoster_handle(poster.getAlias());
            newStatus.setImageURL(poster.getImageUrl());
            newStatus.setFirstName(poster.getFirstName());
            newStatus.setLastName(poster.getLastName());
            newStatus.setMentions(mentions);
            newStatus.setPost(post);
            newStatus.setTimestamp(timestamp);
            newStatus.setUrls(urls);
            table.putItem(newStatus);
        }
    }




    public DataPage<DynamoFeedStatus> getPageOfFeed(String alias, int pageSize, String lastStatusTimestamp) {
        DynamoDbTable<DynamoFeedStatus> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoFeedStatus.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(DatabaseDAO.isNonEmptyString(lastStatusTimestamp)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(aliasAttr, AttributeValue.builder().s(alias).build());
            startKey.put(timestampAttr, AttributeValue.builder().s(lastStatusTimestamp).build());

            requestBuilder.exclusiveStartKey(startKey);
            requestBuilder.scanIndexForward(true);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<DynamoFeedStatus> result = new DataPage<DynamoFeedStatus>();

        PageIterable<DynamoFeedStatus> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<DynamoFeedStatus> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(Follows -> result.getValues().add(Follows));
                });

        return result;
    }


}
