package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() {
        LoginRequest request = new LoginRequest(this.username, this.password);
        ServerFacade facade = new ServerFacade();
        LoginResponse response;
        try {
             response = facade.login(request,"login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TweeterRemoteException e) {
            throw new RuntimeException(e);
        }
//        User loggedInUser = getFakeData().getFirstUser();
//        AuthToken authToken = getFakeData().getAuthToken();
        return new Pair<>(response.getUser(), response.getAuthToken());
    }
}
