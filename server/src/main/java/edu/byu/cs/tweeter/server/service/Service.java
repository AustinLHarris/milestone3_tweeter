package edu.byu.cs.tweeter.server.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import edu.byu.cs.tweeter.model.net.request.AuthorizedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DynamoAuthToken;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.Follows;
import edu.byu.cs.tweeter.util.Pair;

public class Service {

    DAOFactory factory;

    public Service(DAOFactory factory) {
        this.factory = factory;
    }

    public DynamoAuthToken validateToken(String token){
        DynamoAuthToken dbToken = factory.getAuthDAO().getAuth(token);
        if(dbToken == null){ return null; }
        LocalDateTime start = LocalDateTime.parse(dbToken.getTimestamp());
        LocalDateTime end = LocalDateTime.now();
        long elapsedMinutes = ChronoUnit.MINUTES.between(start, end);
        if((int)elapsedMinutes >= 30){
            factory.getAuthDAO().deleteAuth(token);
            factory.getAuthDAO().deleteTokens();
            return null;
        }else {
            dbToken.setTimestamp(end.toString());
            factory.getAuthDAO().updateAuth(token);
            return dbToken;
        }
    }

    public Pair<List<String>, Boolean> getFollowersHelper(FollowersRequest request) {
        assert request.getLimit() > 0;
        assert request.getFollowerAlias() != null;


        DataPage<Follows> followers =  factory.getFollowingDAO().getPageOfFollowers(request.getFollowerAlias(),request.getLimit(),request.getLastFollowerAlias());
        List<String> returnFollowers = new ArrayList<>(request.getLimit());
        for(Follows follower: followers.getValues()){
            returnFollowers.add(follower.getFollower_handle());
        }
        return new Pair<>(returnFollowers, followers.isHasMorePages());
    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public  Pair<List<String>, Boolean> getFolloweesHelper(FollowingRequest request) {
        assert request.getLimit() > 0;
        assert request.getFollowerAlias() != null;

        DataPage<Follows> followees =  factory.getFollowingDAO().getPageOfFollowees(request.getFollowerAlias(),request.getLimit(),request.getLastFolloweeAlias());
        List<String> returnFollowees = new ArrayList<>(request.getLimit());
        for(Follows followee: followees.getValues()){
            returnFollowees.add(followee.getFollowee_handle());
        }

        return new Pair<>(returnFollowees, followees.isHasMorePages());
    }

    public String postImageToS3(String image_string, String alias){
        byte[] byteArray = Base64.getDecoder().decode(image_string);

        ObjectMetadata data = new ObjectMetadata();

        data.setContentLength(byteArray.length);

        data.setContentType("image/jpeg");

        PutObjectRequest request = new PutObjectRequest("cs340imagebucket", alias, new ByteArrayInputStream(byteArray), data).withCannedAcl(CannedAccessControlList.PublicRead);

        // Create AmazonS3 object for doing S3 operations
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-2")
                .build();

        try {
            s3.putObject(request);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

        return "https://cs340imagebucket.s3.us-east-2.amazonaws.com/" + alias;
    }


    protected void checkToken(AuthorizedRequest request) {
        if(validateToken(request.getAuthToken().getToken()) == null){
            throw new RuntimeException("[Missing Authorization] Bad authtoken");
        }
    }
}
