package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.Follows;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowDAO implements DatabaseDAO, FollowDAOInterface {

    private static final String TableName = "follows";
    public static final String IndexName = "follows_index";

    private static final String FollowerAttr = "follower_handle";
    private static final String FolloweeAttr = "followee_handle";


    /**
     * Retrieve a follow
     *
     * @param follower
     * @param followee
     * @return
     */
    public Follows getFollow(String follower, String followee) {
        DynamoDbTable<Follows> table = enhancedClient.table(TableName, TableSchema.fromBean(Follows.class));
        Key key = Key.builder()
                .partitionValue(follower).sortValue(followee)
                .build();

        Follows follows = table.getItem(key);
        return follows;
    }

    /**
     *
     * @param follower
     * @param followee
     */
    public void recordFollow(String follower, String followee) {
        DynamoDbTable<Follows> table = enhancedClient.table(TableName, TableSchema.fromBean(Follows.class));
        Key key = Key.builder()
                .partitionValue(follower).sortValue(followee)
                .build();

        // load it if it exists
        Follows follows = table.getItem(key);
        if(follows != null) {
            return;
        } else {
            Follows newFollow = new Follows();
            newFollow.setFollower_handle(follower);
            newFollow.setFollowee_handle(followee);
            table.putItem(newFollow);
        }
    }

    /**
     *
     * @param follower
     * @param followee
     */
    public void putFollow(String follower, String followee, String newFollowee) {
        DynamoDbTable<Follows> table = enhancedClient.table(TableName, TableSchema.fromBean(Follows.class));
        Key key = Key.builder()
                .partitionValue(follower).sortValue(followee)
                .build();

        // load it if it exists
        Follows follows = table.getItem(key);
        follows.setFollowee_handle(newFollowee);
        table.updateItem(follows);

    }

    /**
     * Delete follow
     *
     * @param follower
     * @param followee
     */
    public void deleteFollow(String follower, String followee) {
        DynamoDbTable<Follows> table = enhancedClient.table(TableName, TableSchema.fromBean(Follows.class));
        Key key = Key.builder()
                .partitionValue(follower).sortValue(followee)
                .build();
        table.deleteItem(key);
    }

    /**
     * Fetch the next page of follows
     *
     * @param followee The follower of interest
     * @param pageSize The maximum number of locations to include in the result
     * @param lastFollower The last location returned in the previous page of results
     * @return The next page of locations visited by visitor
     */
    public DataPage<Follows> getPageOfFollowees(String followee, int pageSize, String lastFollower) {
        DynamoDbTable<Follows> table = enhancedClient.table(TableName, TableSchema.fromBean(Follows.class));
        Key key = Key.builder()
                .partitionValue(followee)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(DatabaseDAO.isNonEmptyString(lastFollower)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeAttr, AttributeValue.builder().s(followee).build());
            startKey.put(FollowerAttr, AttributeValue.builder().s(lastFollower).build());

            requestBuilder.exclusiveStartKey(startKey);
            requestBuilder.scanIndexForward(true);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<Follows> result = new DataPage<Follows>();

        PageIterable<Follows> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<Follows> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(Follows -> result.getValues().add(Follows));
                });

        return result;
    }


    /**
     * Fetch the next page of visitors who have visited location
     *
     * @param follower The location of interest
     * @param pageSize The maximum number of visitors to include in the result
     * @param lastFollowee The last visitor returned in the previous page of results
     * @return The next page of visitors who have visited location
     */
    public DataPage<Follows>  getPageOfFollowers(String follower, int pageSize, String lastFollowee) {
        DynamoDbIndex<Follows> index = enhancedClient.table(TableName, TableSchema.fromBean(Follows.class)).index(IndexName);
        Key key = Key.builder()
                .partitionValue(follower)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(DatabaseDAO.isNonEmptyString(lastFollowee)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeAttr, AttributeValue.builder().s(follower).build());
            startKey.put(FollowerAttr, AttributeValue.builder().s(lastFollowee).build());

            requestBuilder.exclusiveStartKey(startKey);
            requestBuilder.scanIndexForward(true);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<Follows> result = new DataPage<Follows>();

        SdkIterable<Page<Follows>> sdkIterable = index.query(request);
        PageIterable<Follows> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<Follows> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(Follows -> result.getValues().add(Follows));
                });

        return result;
    }



    // Below this is code for adding 10,000 Followers

    public void addFollowersBatch(List<Follow> followList) {
        List<Follows> batchToWrite = new ArrayList<>();
        for (Follow f : followList) {
            Follows dto = new Follows(f);
            batchToWrite.add(dto);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfFollowsDTOs(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfFollowsDTOs(batchToWrite);
        }
    }
    private void writeChunkOfFollowsDTOs(List<Follows> followsList) {
        if(followsList.size() > 25)
            throw new RuntimeException("Too many follows to write");

        DynamoDbTable<Follows> table = enhancedClient.table(TableName, TableSchema.fromBean(Follows.class));
        WriteBatch.Builder<Follows> writeBuilder = WriteBatch.builder(Follows.class).mappedTableResource(table);
        for (Follows item : followsList) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfFollowsDTOs(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}
