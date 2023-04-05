package edu.byu.cs.tweeter.server.dao.dynamoclasses;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;


@DynamoDbBean
public class Follows {
    private String follower_handle;
    private String followee_handle;

    public Follows(Follow f) {
        this.setFollower_handle(f.getFollower().getAlias());
        this.setFollowee_handle(f.getFollowee().getAlias());
    }

    public Follows() {

    }


    @DynamoDbPartitionKey
    @DynamoDbAttribute("follower_handle")
    @DynamoDbSecondarySortKey(indexNames = FollowDAO.IndexName)
    public String getFollower_handle() {
        return follower_handle;
    }

    public void setFollower_handle(String follower) {
        this.follower_handle = follower;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("followee_handle")
    @DynamoDbSecondaryPartitionKey(indexNames = FollowDAO.IndexName)
    public String getFollowee_handle() {
        return followee_handle;
    }

    public void setFollowee_handle(String followee) {
        this.followee_handle = followee;
    }



    @Override
    public String toString() {
        return "Follows{" +
                "follower='" + follower_handle + '\'' +
                ", followee='" + followee_handle +
                '}';
    }
}
