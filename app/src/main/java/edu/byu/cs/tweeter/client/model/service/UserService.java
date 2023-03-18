package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.observer.UserNotificationObserver;
import edu.byu.cs.tweeter.client.presenter.GetFollowersPresenter;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends SingleThreadService{

    public void getUser(String userAlias, UserNotificationObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandler(observer));
        execute(getUserTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(getUserTask);
    }


}
