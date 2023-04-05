package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.dynamoclasses.DynamoAuthToken;

public interface AuthDAOInterface {
    public DynamoAuthToken getAuth(String authtoken);
    public void deleteAuth(String authtoken);
    public DynamoAuthToken createAuth(String alias);
    public void updateAuth(String token);
    public void deleteTokens();
}
