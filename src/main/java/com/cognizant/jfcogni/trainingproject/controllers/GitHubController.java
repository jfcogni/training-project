package com.cognizant.jfcogni.trainingproject.controllers;

import com.cognizant.jfcogni.trainingproject.services.impl.GitHubServiceImpl;
import com.cognizant.jfcogni.trainingproject.views.GitHubUserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class GitHubController {


    @Autowired
    private GitHubServiceImpl gitHubService;

    @GetMapping("/get-user-info")
    public ResponseEntity<GitHubUserView> getUserInfo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION,required = true) String authorizationToken
    ) throws Exception {

        GitHubUserView user;

        if(authorizationToken == null || authorizationToken.isEmpty()) // no puede ser nulo nunca si esta como required=true en la obtencion del parametro
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        user = gitHubService.getUserByAuthToken(authorizationToken);

        return new ResponseEntity<GitHubUserView>(user,HttpStatus.OK);
    }

}
