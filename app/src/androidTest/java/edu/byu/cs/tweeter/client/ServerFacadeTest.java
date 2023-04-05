package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowCountRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowCountResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

public class ServerFacadeTest {

    private AuthToken authToken;

    private ServerFacade serverFacadeSpy;
    private RegisterRequest registerRequest;
    private LoginResponse registerResponse;
    private FollowersRequest followersRequest;
    private FollowingResponse followingResponse;
    private GetFollowCountRequest followCountRequest;
    private GetFollowCountResponse followCountResponse;

    @BeforeEach
    public void setup() {
        authToken = new AuthToken();
        serverFacadeSpy = Mockito.spy(new ServerFacade());

    }

    @Test
    public void testRegister_callOnServerFacade() throws InterruptedException {
        registerRequest = new RegisterRequest("Tom","Tom","Tom","Tom","Tom");

        try {
            registerResponse = serverFacadeSpy.register(registerRequest, "register");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TweeterRemoteException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNotNull(registerResponse.getAuthToken());
        Assertions.assertNotNull(registerResponse.getUser());
        Assertions.assertTrue(registerResponse.getUser().getAlias() == "@allen");

    }

    @Test
    public void testGetFollowers_callOnServerFacade() throws InterruptedException {
        followersRequest = new FollowersRequest(authToken,"Tom",3,null);

        try {
            followingResponse = serverFacadeSpy.getFollowers(followersRequest, "getfollowers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TweeterRemoteException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNotNull(followingResponse.getFollowees());
        Assertions.assertTrue(followingResponse.getFollowees().size() == 3);

    }

    @Test
    public void testGetFollowersCount_callOnServerFacade() throws InterruptedException {
        followCountRequest = new GetFollowCountRequest(authToken,"Tom");

        try {
            followCountResponse = serverFacadeSpy.getFollowerCount(followCountRequest, "followercount");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TweeterRemoteException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(followCountResponse.getCount() == 20);

    }
}
