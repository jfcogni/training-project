package com.cognizant.jfcogni.trainingproject.services;

import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoToCreateDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubUserDTO;

import java.io.IOException;
import java.util.List;

public interface GitHubService {



    GitHubUserDTO getUserByAuthToken(String authorizationToken) throws IOException, InterruptedException;
    List<GitHubRepoDTO> getReposByAuthToken(String authorizationToken) throws IOException, InterruptedException;

    GitHubRepoDTO createRepoByAuthToken(String authorizationToken, GitHubRepoToCreateDTO repository) throws IOException, InterruptedException;
}
