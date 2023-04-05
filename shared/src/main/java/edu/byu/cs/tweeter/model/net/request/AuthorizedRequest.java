package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class AuthorizedRequest {

    public AuthorizedRequest() {
    }

    public AuthorizedRequest(AuthToken authToken) {
        this.authToken = authToken;
    }

    private AuthToken authToken;


    /**
     * Returns the auth token of the user who is making the request.
     *
     * @return the auth token.
     */
    public AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Sets the auth token.
     *
     * @param authToken the auth token.
     */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
