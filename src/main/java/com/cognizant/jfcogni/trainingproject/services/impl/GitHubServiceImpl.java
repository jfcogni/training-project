package com.cognizant.jfcogni.trainingproject.services.impl;

import com.cognizant.jfcogni.trainingproject.services.GitHubService;
import com.cognizant.jfcogni.trainingproject.views.GitHubRepoView;
import com.cognizant.jfcogni.trainingproject.views.GitHubUserView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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

    @Override
    public GitHubUserView getUserByAuthToken(String authorizationToken) throws IOException, InterruptedException {


        if(StringUtils.isBlank(gitHubApiUrl))
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        HttpResponse<String> response = httpCall(gitHubApiUrl+"user", authorizationToken);

        if(response.statusCode()!=200){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        GitHubUserView user = mapper.readValue(response.body(), GitHubUserView.class);
        if (user != null){
            if(StringUtils.isBlank(user.getName()))
                user.setName("Sin nombre definido en GitHub");


            return user;
        }else
            return null;
    }

    @Override
    public List<GitHubRepoView> getReposByAuthToken(String authorizationToken) throws IOException, InterruptedException {
        if(StringUtils.isBlank(gitHubApiUrl))
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        HttpResponse<String> response = httpCall(gitHubApiUrl+"user/repos", authorizationToken);

        if(response.statusCode()!=200){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(response.body(), new TypeReference<>(){});
    }


// --------- internal service methods --------------

    private HttpResponse<String> httpCall(String URL, String token) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Accept","application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();


        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    }


}
