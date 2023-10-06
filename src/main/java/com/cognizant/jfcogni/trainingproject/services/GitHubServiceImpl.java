package com.cognizant.jfcogni.trainingproject.services;

import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoToCreateDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubUserDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class GitHubServiceImpl implements GitHubService {

    @Value("${url.github.api}")
    private String gitHubApiUrl;

    @Autowired
    private HttpClient httpClient;

    private static final String ACCEPT_APPLICATION_GITHUB = "application/vnd.github+json";

    @Override
    public GitHubUserDTO getUserByAuthToken(String authorizationToken) throws IOException, InterruptedException {


        if(StringUtils.isBlank(gitHubApiUrl))
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        HttpResponse<String> response = httpGetCall(gitHubApiUrl+"/user", authorizationToken);

        if(response.statusCode()!=200){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        GitHubUserDTO user = mapper.readValue(response.body(), GitHubUserDTO.class);
        if (user != null){
            if(StringUtils.isBlank(user.getName()))
                user.setName("Sin nombre definido en GitHub");
            return user;
        }else
            return null;
    }

    @Override
    public List<GitHubRepoDTO> getReposByAuthToken(String authorizationToken) throws IOException, InterruptedException {
        if(StringUtils.isBlank(gitHubApiUrl))
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        HttpResponse<String> response = httpGetCall(gitHubApiUrl+"/user/repos", authorizationToken);

        if(response.statusCode()!=200){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(response.body(), new TypeReference<>(){});
    }

    @Override
    public GitHubRepoDTO createRepoByAuthToken(String authorizationToken, GitHubRepoToCreateDTO repository) throws IOException, InterruptedException {
        if(StringUtils.isBlank(gitHubApiUrl))
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        if(repository == null || StringUtils.isBlank(repository.getName()) || StringUtils.isBlank(repository.getDescription()) || StringUtils.isBlank(repository.getHomepage())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonRepo = mapper.writeValueAsString( repository );

        HttpResponse<String> response = httpPostCall(gitHubApiUrl+"/user/repos", authorizationToken,jsonRepo);

        if(response.statusCode()!=201){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,response.body());
        }

        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(response.body(), new TypeReference<>(){});
    }


// --------- internal service methods --------------

    private HttpResponse<String> httpGetCall(String URL, String token) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header(HttpHeaders.ACCEPT,ACCEPT_APPLICATION_GITHUB)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .GET()
                .build();


        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    }

    private HttpResponse<String> httpPostCall(String URL, String token, String jsonBody) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header(HttpHeaders.ACCEPT,ACCEPT_APPLICATION_GITHUB)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();


        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    }
}
