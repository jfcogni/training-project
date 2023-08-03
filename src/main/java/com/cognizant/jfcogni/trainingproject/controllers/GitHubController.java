package com.cognizant.jfcogni.trainingproject.controllers;

import com.cognizant.jfcogni.trainingproject.services.GitHubService;
import com.cognizant.jfcogni.trainingproject.views.GitHubRepoToCreateView;
import com.cognizant.jfcogni.trainingproject.views.GitHubRepoView;
import com.cognizant.jfcogni.trainingproject.views.GitHubUserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class GitHubController {


    @Autowired
    private GitHubService gitHubService;

    @GetMapping("/get-user-info")
    public ResponseEntity<GitHubUserView> getUserInfo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationToken
    ) throws Exception {

        GitHubUserView user;

        if(authorizationToken == null || authorizationToken.isEmpty()) // no puede ser nulo nunca si esta como required=true(por defecto) en la obtencion del parametro
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        user = gitHubService.getUserByAuthToken(authorizationToken);

        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/get-repos-user-info")
    public ResponseEntity<List<GitHubRepoView>> getReposUserInfo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationToken
    ) throws Exception {

        List<GitHubRepoView> repos;

        if(authorizationToken == null || authorizationToken.isEmpty()) // no puede ser nulo nunca si esta como required=true en la obtencion del parametro
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        repos = gitHubService.getReposByAuthToken(authorizationToken);

        return new ResponseEntity<>(repos,HttpStatus.OK);
    }

    @PostMapping("/create-repository")
    public ResponseEntity<GitHubRepoView> createRepository(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationToken,
            @RequestParam String repositoryName,
            @RequestParam String repositoryDescription,
            @RequestParam boolean repositoryPrivate,
            @RequestParam String repositoryHomePage
    ) throws Exception {

        List<GitHubRepoToCreateView> repos;

        if(authorizationToken == null || authorizationToken.isEmpty()) // no puede ser nulo nunca si esta como required=true en la obtencion del parametro
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        //TODO: COMPROBAR VALORES DE PARAMETROS RECIBIDOS

        GitHubRepoToCreateView repoToCreate = new GitHubRepoToCreateView(repositoryName,repositoryDescription,repositoryPrivate,repositoryHomePage);

        GitHubRepoView repo = gitHubService.createRepoByAuthToken(authorizationToken,repoToCreate);

        return new ResponseEntity<>(repo,HttpStatus.CREATED);
    }
}
