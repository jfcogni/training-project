package com.cognizant.jfcogni.trainingproject.controllers;

import com.cognizant.jfcogni.trainingproject.services.GitHubService;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoToCreateDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubUserDTO;
import jakarta.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class GitHubController {


    @Autowired
    private GitHubService gitHubService;

    @GetMapping("/get-user-info")
    public ResponseEntity<GitHubUserDTO> getUserInfo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationToken
    ) throws Exception {

        GitHubUserDTO user;

        if(authorizationToken == null || authorizationToken.isEmpty()) // no puede ser nulo nunca si esta como required=true(por defecto) en la obtencion del parametro
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        user = gitHubService.getUserByAuthToken(authorizationToken);

        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/get-repos-user-info")
    public ResponseEntity<List<GitHubRepoDTO>> getReposUserInfo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationToken
    ) throws Exception {

        List<GitHubRepoDTO> repos;

        if(authorizationToken == null || authorizationToken.isEmpty()) // no puede ser nulo nunca si esta como required=true en la obtencion del parametro
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        repos = gitHubService.getReposByAuthToken(authorizationToken);

        return new ResponseEntity<>(repos,HttpStatus.OK);
    }

    @PostMapping("/create-repository")
    public ResponseEntity<GitHubRepoDTO> createRepository(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationToken,
            @Valid @RequestBody GitHubRepoToCreateDTO repoToCreate,
            BindingResult result
    ) throws Exception {

        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(authorizationToken)) // no puede ser nulo nunca si esta como required=true en la obtencion del parametro
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        if(repoToCreate == null || StringUtils.isBlank(repoToCreate.getName()) || StringUtils.isBlank(repoToCreate.getDescription()) || StringUtils.isBlank(repoToCreate.getHomepage())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


        GitHubRepoDTO repo = gitHubService.createRepoByAuthToken(authorizationToken,repoToCreate);

        return new ResponseEntity<>(repo,HttpStatus.CREATED);
    }
}
