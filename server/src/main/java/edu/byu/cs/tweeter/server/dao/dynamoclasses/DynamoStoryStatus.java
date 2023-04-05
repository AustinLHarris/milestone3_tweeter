package edu.byu.cs.tweeter.server.dao.dynamoclasses;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class DynamoStoryStatus {

    private String user;
    private String poster_handle;
    private String firstName;
    private String lastName;
    private String imageURL;
    private List<String> mentions;
    private String post;
    private String timestamp;
    private List<String> urls;


    @DynamoDbPartitionKey
    @DynamoDbAttribute("user")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

//    @DynamoDbAttribute("poster_handle")
//    public String getPoster_handle() {
//        return poster_handle;
//    }
//
//    public void setPoster_handle(String poster_handle) {
//        this.poster_handle = poster_handle;
//    }

    @DynamoDbAttribute("mentions")
    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    @DynamoDbAttribute("post")
    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @DynamoDbAttribute("urls")
    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @DynamoDbAttribute("firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @DynamoDbAttribute("lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @DynamoDbAttribute("imageURL")
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
