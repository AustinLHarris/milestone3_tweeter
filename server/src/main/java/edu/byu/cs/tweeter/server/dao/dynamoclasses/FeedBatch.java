package edu.byu.cs.tweeter.server.dao.dynamoclasses;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class FeedBatch {
    Status status;
    List<String> targetAliases;

    public FeedBatch(Status status, List<String> targetAliases) {
        this.status = status;
        this.targetAliases = targetAliases;
    }

    public FeedBatch() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getTargetAliases() {
        return targetAliases;
    }

    public void setTargetAliases(List<String> targetAliases) {
        this.targetAliases = targetAliases;
    }
}
