package com.cognizant.jfcogni.trainingproject.controllers;

import com.cognizant.jfcogni.trainingproject.views.UserView;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
public class GitHubController {

    @Value("${url.github.api}")
    public String gitHubApiUrl;

        @GetMapping("/get-user-info")
    public ResponseEntity<UserView> getUserInfo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION,required = true) String authorizationToken
    ) throws Exception {

        UserView user;

        if(StringUtils.isBlank(gitHubApiUrl))
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        if(authorizationToken == null || authorizationToken.isEmpty()) // no puede ser nulo nunca si esta como required=true en la obtencion del parametro
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        HttpResponse<String> response = httpCall(gitHubApiUrl+"user", authorizationToken);

        if(response.statusCode()!=200){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        user = mapper.readValue(response.body(), UserView.class);
        if (user != null && StringUtils.isBlank(user.getName())){
            user.setName("Sin nombre definido en GitHub");
        }

        return new ResponseEntity<UserView>(user,HttpStatus.OK);
    }

    private HttpResponse<String> httpCall(String URL, String token) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Accept","application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }
}
