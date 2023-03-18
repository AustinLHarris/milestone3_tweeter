package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.observer.UserNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends Presenter{

    public LoginPresenter(loginView view) {
        super(view);
        this.view = view;
        loginService = new LoginService();
    }

    private loginView view;
    private LoginService loginService;

    public void loginTask(String alias, String password) {
        loginService.loginTask(alias,password,new GetLoginObserver());
    }

    public interface loginView extends PresenterView {
        void loginToast(boolean status);
    }

    public class GetLoginObserver extends GetGeneralObserver implements UserNotificationObserver {
        @Override
        protected String getMessagePrefix(boolean isException) {
            if(isException){
                return "Failed to login because of exception: ";
            }else {
                return "Failed to login: ";
            }
        }
        @Override
        public void handleSuccess(User user) {
            view.loginToast(false);
            view.runActivity(user);
            view.displayErrorMessage("Hello " + Cache.getInstance().getCurrUser().getName());
        }
    }
}
