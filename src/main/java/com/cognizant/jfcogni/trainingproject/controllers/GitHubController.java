package com.cognizant.jfcogni.trainingproject.controllers;

import com.cognizant.jfcogni.trainingproject.services.GitHubService;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoToCreateDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubUserDTO;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@SecurityScheme(name = HttpHeaders.AUTHORIZATION, scheme = "JWT", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER)
@RestController
public class GitHubController {


    @Autowired
    private GitHubService gitHubService;

    @GetMapping("/get-repos-user-info")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public ResponseEntity<List<GitHubRepoDTO>> getReposUserInfo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationToken
    ) throws Exception {

        List<GitHubRepoDTO> repos;

        if(authorizationToken == null || authorizationToken.isEmpty()) // no puede ser nulo nunca si esta como required=true en la obtencion del parametro
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        repos = gitHubService.getReposByAuthToken(authorizationToken);

        return new ResponseEntity<>(repos,HttpStatus.OK);
    }

    @GetMapping("/get-user-info")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public ResponseEntity<GitHubUserDTO> getUserInfo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationToken
    ) throws Exception {

        GitHubUserDTO user;

        if(authorizationToken == null || authorizationToken.isEmpty()) // no puede ser nulo nunca si esta como required=true(por defecto) en la obtencion del parametro
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        user = gitHubService.getUserByAuthToken(authorizationToken);

        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("/create-repository")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public ResponseEntity<GitHubRepoDTO> createRepository(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationToken,
            @Valid @RequestBody GitHubRepoToCreateDTO repoToCreate
    ) throws Exception {





        if(StringUtils.isBlank(authorizationToken)) // no puede ser nulo nunca si esta como required=true en la obtencion del parametro
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);




        GitHubRepoDTO repo = gitHubService.createRepoByAuthToken(authorizationToken,repoToCreate);

        return new ResponseEntity<>(repo,HttpStatus.CREATED);
    }


}
