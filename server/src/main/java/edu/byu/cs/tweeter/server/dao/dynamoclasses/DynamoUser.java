package edu.byu.cs.tweeter.server.dao.dynamoclasses;

import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class DynamoUser {
    private String user_handle;
    private String firstName;
    private String lastName;
    private String imageURL;
    private String password;
    private String salt;
    private Integer numFolllowers;
    private Integer numFollowing;

    public DynamoUser(User u) {
        this.setUser_handle(u.getAlias());
        this.setFirstName(u.getFirstName());
        this.setLastName(u.getLastName());
        this.setImageURL(u.getImageUrl());
        this.setPassword("fake");
        this.setSalt("");
        this.setNumFolllowers(0);
        this.setNumFollowing(0);
    }

    public DynamoUser() {

    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("user_handle")
    public String getUser_handle() {
        return user_handle;
    }

    public void setUser_handle(String user_handle) {
        this.user_handle = user_handle;
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

    @DynamoDbAttribute("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DynamoDbAttribute("salt")
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @DynamoDbAttribute("numFollowers")
    public Integer getNumFolllowers() {
        return numFolllowers;
    }

    public void setNumFolllowers(Integer numFolllowers) {
        this.numFolllowers = numFolllowers;
    }

    @DynamoDbAttribute("numFollowing")
    public Integer getNumFollowing() {
        return numFollowing;
    }

    public void setNumFollowing(Integer numFollowing) {
        this.numFollowing = numFollowing;
    }


    @Override
    public String toString() {
        return "DynamoUser{" +
                "user_handle='" + user_handle + '\'' +
                ", firstName='" + firstName +
                ", lastName='" + lastName +
                ", imageURL='" + imageURL +
                '}';
    }

}
