package edu.byu.cs.tweeter.server.dao;

import java.time.LocalDateTime;
import java.util.UUID;

import edu.byu.cs.tweeter.server.dao.dynamoclasses.DynamoAuthToken;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class AuthDAO implements DatabaseDAO, AuthDAOInterface {

    private static final String TableName = "authtoken";
    private static final String FollowerAttr = "user_handle";



        /**
     * Retrieve an authtoken
     *
     * @param authtoken
     * @return
     */
    public DynamoAuthToken getAuth(String authtoken) {
        DynamoDbTable<DynamoAuthToken> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoAuthToken.class));
        Key key = Key.builder()
                .partitionValue(authtoken)
                .build();
        return table.getItem(key);
    }

    public void deleteAuth(String authtoken) {
        DynamoDbTable<DynamoAuthToken> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoAuthToken.class));
        Key key = Key.builder()
                .partitionValue(authtoken)
                .build();
        table.deleteItem(key);
    }


    public DynamoAuthToken createAuth(String alias) {
        String datetime = LocalDateTime.now().toString();
        String token = UUID.randomUUID().toString();
        DynamoDbTable<DynamoAuthToken> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoAuthToken.class));
        Key key = Key.builder()
                .partitionValue(token)
                .build();

        DynamoAuthToken authToken = new DynamoAuthToken();
        authToken.setAuthtoken(token);
        authToken.setUser_handle(alias);
        authToken.setTimestamp(datetime);
        table.putItem(authToken);
        return authToken;
    }

    public void updateAuth(String token){
        DynamoDbTable<DynamoAuthToken> table = enhancedClient.table(TableName, TableSchema.fromBean(DynamoAuthToken.class));
        Key key = Key.builder()
                .partitionValue(token)
                .build();
        DynamoAuthToken dbToken = table.getItem(key);
        dbToken.setTimestamp(LocalDateTime.now().toString());
        table.updateItem(dbToken);
    }

    public void deleteTokens(){

    }
}
