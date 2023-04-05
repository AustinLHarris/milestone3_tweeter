package edu.byu.cs.tweeter.server.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DynamoAuthToken;
import edu.byu.cs.tweeter.server.dao.dynamoclasses.DynamoUser;

public class UserService extends Service{

    public UserService(DAOFactory factory) {
        super(factory);
    }

    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }
        DynamoAuthToken dbToken = factory.getAuthDAO().createAuth(request.getUsername());
        AuthToken authToken = new AuthToken(dbToken.getAuthtoken(), dbToken.getTimestamp());
        DynamoUser dynamoUser = factory.getUserDAO().getUser(request.getUsername());
        if(dynamoUser == null){ return new LoginResponse("Incorrect password or username"); }
        if(verifyPassword(request.getPassword(),dynamoUser.getPassword(),dynamoUser.getSalt())){
            User user = createUserFromDB(dynamoUser);
            return new LoginResponse(user, authToken);
        } else{
            throw new RuntimeException("[Bad Request] Password or username is incorrect");
        }
    }




    public LoginResponse register(RegisterRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }else if(request.getFirstName() == null) {
            throw new RuntimeException("[Bad Request] Missing first name");
        }else if(request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing last name");
        }else if (request.getImage() == null) {
            throw new RuntimeException("[Bad Request] Missing profile picture");
        }
        DynamoAuthToken dbToken = factory.getAuthDAO().createAuth(request.getUsername());
        AuthToken authToken = new AuthToken(dbToken.getAuthtoken(),dbToken.getTimestamp());
        String salt = getSalt();
        String securePassword = getSecurePassword(request.getPassword(), salt);
        DynamoUser dbUser = factory.getUserDAO().register(request.getFirstName(),request.getLastName(),postImageToS3(request.getImage(),request.getUsername()),request.getUsername(), securePassword,salt);
        return new LoginResponse(createUserFromDB(dbUser), authToken);
    }

    public GetUserResponse getUser(GetUserRequest request){
        if(request.getAlias() == null){
            throw new RuntimeException("[Bad Request] Missing a user");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Missing Authorization] Missing authtoken");
        }
        checkToken(request);
        if(request.getAlias() == null){
            System.out.println("There is no alias in this request");
        }
        DynamoUser dbUser = factory.getUserDAO().getUser(request.getAlias());
        System.out.println(dbUser.toString());
//        User user = getFakeData().findUserByAlias(request.getAlias());
        User user = createUserFromDB(dbUser);
        return new GetUserResponse(user);
    }

    private User createUserFromDB(DynamoUser dbUser) {
        return new User(dbUser.getFirstName(),dbUser.getLastName(), dbUser.getUser_handle(), dbUser.getImageURL());
    }

    public LogoutResponse logout(LogoutRequest request) {
        if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing authtoken");
        }
        factory.getAuthDAO().deleteAuth(request.getAuthToken().getToken());
        return new LogoutResponse();
    }





    private boolean verifyPassword(String suppliedPassword, String securePassword, String salt) {
        String regeneratedPasswordToVerify = getSecurePassword(suppliedPassword, salt);
        return securePassword.equals(regeneratedPasswordToVerify);
    }

    private String getSecurePassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "FAILED TO HASH PASSWORD";
    }

    private String getSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return "FAILED TO GET SALT";
    }

}
