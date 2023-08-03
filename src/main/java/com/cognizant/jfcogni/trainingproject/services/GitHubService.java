package com.cognizant.jfcogni.trainingproject.services;

import com.cognizant.jfcogni.trainingproject.views.GitHubRepoToCreateView;
import com.cognizant.jfcogni.trainingproject.views.GitHubRepoView;
import com.cognizant.jfcogni.trainingproject.views.GitHubUserView;

import java.io.IOException;
import java.util.List;

public interface GitHubService {



    GitHubUserView getUserByAuthToken(String authorizationToken) throws IOException, InterruptedException;
    List<GitHubRepoView> getReposByAuthToken(String authorizationToken) throws IOException, InterruptedException;

    GitHubRepoView createRepoByAuthToken(String authorizationToken, GitHubRepoToCreateView repository) throws IOException, InterruptedException;
}
