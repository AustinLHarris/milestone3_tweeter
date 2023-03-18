package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowersHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowingHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.presenter.GetFollowersPresenter;
import edu.byu.cs.tweeter.client.presenter.GetFollowingPresenter;
import edu.byu.cs.tweeter.client.presenter.GetMainPresenter;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends SingleThreadService{

    public void loadMoreFollowing(User user, int pageSize, User lastFollowee, PagedPresenter.GetPagedObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new GetFollowingHandler(observer));
        execute(getFollowingTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(getFollowingTask);
    }

    public void loadMoreFollowers(User user, int pageSize, User lastFollower, PagedPresenter.GetPagedObserver observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollower, new GetFollowersHandler(observer));
        execute(getFollowersTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(getFollowersTask);
    }

    public void isFollowerTask(User selectedUser, GetMainPresenter.GetIsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        execute(isFollowerTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(isFollowerTask);
    }

    public void unfollowTask(User selectedUser, SimpleNotificationObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        execute(unfollowTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(unfollowTask);
    }

    public void followTask(User selectedUser, SimpleNotificationObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        execute(followTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(followTask);
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser, GetMainPresenter.GetFollowerCountObserver followerObserver, GetMainPresenter.GetFollowingCountObserver followingObserver) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(followerObserver));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(followingObserver));
        executor.execute(followingCountTask);
    }

}
