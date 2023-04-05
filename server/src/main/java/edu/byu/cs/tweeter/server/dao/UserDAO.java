package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DynamoUser;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class UserDAO implements DatabaseDAO, UserDAOInterface{
    private static final String TableName = "user";
    private static final String UserAttr = "user_handle";

    /**
     * Retrieve an authtoken
     *
     * @param alias
     * @return
     */
    public DynamoUser getUser(String alias) {
        DynamoDbTable<DynamoUser> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoUser.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        DynamoUser user = table.getItem(key);
        System.out.println("Object returned by DynamoDB" + user.getUser_handle() + " " + user.getFirstName() + " " + user.getLastName() + " " + user.getImageURL());
        return user;
    }

     /**
     * @param updatedUser
     */
    public void putUser(DynamoUser updatedUser) {
        DynamoDbTable<DynamoUser> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoUser.class));
        Key key = Key.builder()
                .partitionValue(updatedUser.getUser_handle())
                .build();

//         load it if it exists
        DynamoUser user = table.getItem(key);
        user.setNumFolllowers(updatedUser.getNumFolllowers());
        user.setNumFollowing(updatedUser.getNumFollowing());
        table.updateItem(updatedUser);
    }


    public DynamoUser register(String firstName, String lastName, String imageURL, String userHandle, String password, String salt) {
        DynamoDbTable<DynamoUser> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoUser.class));
        Key key = Key.builder()
                .partitionValue(userHandle)
                .build();
        // load it if it exists
        DynamoUser user = table.getItem(key);
        if (user != null) {
            return null;
        } else {
            DynamoUser newUser = new DynamoUser();
            newUser.setUser_handle(userHandle);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setImageURL(imageURL);
            newUser.setPassword(password);
            newUser.setSalt(salt);
            newUser.setNumFolllowers(0);
            newUser.setNumFollowing(0);
            table.putItem(newUser);
            return newUser;
        }
    }

    //This code is for writing 10,000 users

    public void addUserBatch(List<User> users) {
        List<DynamoUser> batchToWrite = new ArrayList<>();
        for (User u : users) {
            DynamoUser dto = new DynamoUser(u);
            batchToWrite.add(dto);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfUserDTOs(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfUserDTOs(batchToWrite);
        }
    }
    private void writeChunkOfUserDTOs(List<DynamoUser> userDTOs) {
        if(userDTOs.size() > 25)
            throw new RuntimeException("Too many users to write");

        DynamoDbTable<DynamoUser> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoUser.class));
        WriteBatch.Builder<DynamoUser> writeBuilder = WriteBatch.builder(DynamoUser.class).mappedTableResource(table);
        for (DynamoUser item : userDTOs) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfUserDTOs(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}
