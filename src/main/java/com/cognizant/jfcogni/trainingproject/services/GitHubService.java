package com.cognizant.jfcogni.trainingproject.services;

import com.cognizant.jfcogni.trainingproject.views.GitHubRepoView;
import com.cognizant.jfcogni.trainingproject.views.GitHubUserView;

import java.io.IOException;

public interface GitHubService {



    public GitHubUserView getUserByAuthToken(String authorizationToken) throws IOException, InterruptedException;
    // TODO ->method public GitHubRepoView getRepoByxxx();
}
