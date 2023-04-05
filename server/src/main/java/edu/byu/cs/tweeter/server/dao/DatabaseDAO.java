package edu.byu.cs.tweeter.server.dao;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public interface DatabaseDAO {

    static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build();

    static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

}
