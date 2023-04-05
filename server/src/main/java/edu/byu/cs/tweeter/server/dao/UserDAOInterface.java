package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DynamoUser;

public interface UserDAOInterface {
    public void putUser(DynamoUser updatedUser);
    public DynamoUser getUser(String alias);
    public DynamoUser register(String firstName, String lastName, String imageURL, String userHandle, String password, String salt);

    void addUserBatch(List<User> users);
}
