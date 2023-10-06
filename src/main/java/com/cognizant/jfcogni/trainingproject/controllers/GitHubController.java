package com.cognizant.jfcogni.trainingproject.controllers;

import com.cognizant.jfcogni.trainingproject.services.GitHubService;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoToCreateDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@SecurityScheme(name = HttpHeaders.AUTHORIZATION, scheme = "JWT", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER)
@RestController
public class GitHubController {


    @Autowired
    private GitHubService gitHubService;

    @GetMapping("/get-repos-user-info")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @Operation(summary = "Get GitHub user repositories by GitHub Personal Access Token",
                description = "This endpoint return the GitHub User Repositories sending a GitHub Personal Access Token as header Autorization parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the GitHub User Repositories",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GitHubRepoDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error",
                    content = @Content) })
    public ResponseEntity<List<GitHubRepoDTO>> getReposUserInfo(
            HttpServletRequest request
    ) throws IOException, InterruptedException {

        List<GitHubRepoDTO> repos;
        if(request == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        String authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.isBlank(authorizationToken))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        repos = gitHubService.getReposByAuthToken(authorizationToken);

        return new ResponseEntity<>(repos,HttpStatus.OK);
    }

    @GetMapping("/get-user-info")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @Operation(summary = "Get GitHub User Information by GitHub Personal Access Token",
            description = "This endpoint return the GitHub User Information sending a GitHub Personal Access Token as header Autorization parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the GitHub User",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GitHubUserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error",
                    content = @Content) })
    public ResponseEntity<GitHubUserDTO> getUserInfo(
            HttpServletRequest request
    ) throws IOException, InterruptedException {

        GitHubUserDTO user;
        if(request == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.getReasonPhrase());

        String authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.isBlank(authorizationToken))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,HttpStatus.UNAUTHORIZED.getReasonPhrase());

        user = gitHubService.getUserByAuthToken(authorizationToken);

        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("/create-repository")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @Operation(summary = "Create a Repository on GitHub",
            description = "This endpoint create a new Repository on GitHub for the user with the Personal Access Token sending as header Autorization parameter. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created the Repository",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GitHubRepoDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error",
                    content = @Content) })
    public ResponseEntity<GitHubRepoDTO> createRepository(
            HttpServletRequest request,
            @Valid @RequestBody GitHubRepoToCreateDTO repoToCreate
    ) throws IOException, InterruptedException {

        if(request == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        String authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.isBlank(authorizationToken))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);


        GitHubRepoDTO repo = gitHubService.createRepoByAuthToken(authorizationToken,repoToCreate);

        return new ResponseEntity<>(repo,HttpStatus.CREATED);
    }


}
