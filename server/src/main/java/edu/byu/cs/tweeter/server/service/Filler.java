package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.DynamoFactory;

public class Filler{

    // How many follower users to add
    // We recommend you test this with a smaller number first, to make sure it works for you
    private final static int NUM_USERS = 10000;
    static DAOFactory factory = new DynamoFactory();


    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static User FOLLOW_TARGET = new User("tom", "holland","@spiderman","https://cs340imagebucket.s3.us-east-2.amazonaws.com/%40scott");


    public static void main(String[] args) {

        // Get instance of DAOs by way of the Abstract Factory Pattern


        List<Follow> followers = new ArrayList<>();
        List<User> users = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 1; i <= NUM_USERS; i++) {

            String name = "Guy " + i;
            String alias = "guy" + i;

            // Note that in this example, a UserDTO only has a name and an alias.
            // The url for the profile image can be derived from the alias in this example
            User user = new User();
            user.setAlias(alias);
            user.setFirstName(name);
            user.setLastName("Fake");
            user.setImageUrl("s3://cs340imagebucket/@betsy");
            users.add(user);

            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            Follow follow = new Follow(user,FOLLOW_TARGET);
            followers.add(follow);
        }

        // Call the DAOs for the database logic
        if (users.size() > 0) {
            factory.getUserDAO().addUserBatch(users);
        }
        if (followers.size() > 0) {
            factory.getFollowingDAO().addFollowersBatch(followers);
        }
    }
}
