package com.cognizant.jfcogni.trainingproject.controllers;

import com.cognizant.jfcogni.trainingproject.dto.GitHubUserDTO;
import com.cognizant.jfcogni.trainingproject.services.GitHubServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class GitHubControllerTest {

    @Mock
    private GitHubServiceImpl gitHubService;

    @InjectMocks
    private GitHubController gitHubController;

    @Test
    public void testGetReposUserInfo_withNullRequest_responseStatusException() {
        //given
        HttpServletRequest request = null;

        //when
        Executable executable = () -> gitHubController.getUserInfo(request);

        //then
        assertThrows(ResponseStatusException.class,executable);

    }

    @Test
    public void testGetReposUserInfo_withNotValidAuthorizationUserToken() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String authorizationToke = "";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationToke);

        //when
        Executable executable = () -> gitHubController.getUserInfo(request);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetReposUserInfo_withValidAuthorizationUserToken_ResponseStatus200() throws IOException, InterruptedException {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String authorizationToke = "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";
        GitHubUserDTO expected = new GitHubUserDTO("JesusName","jesusLogin");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationToke);
        when(gitHubService.getUserByAuthToken(authorizationToke)).thenReturn(expected);

        //when
        ResponseEntity<GitHubUserDTO> result = gitHubController.getUserInfo(request);


        //then
        assertAll(
                () -> assertEquals(expected.getName(), result.getBody().getName()),
                () -> assertEquals(expected.getLogin(), result.getBody().getLogin()),
                () -> assertEquals(HttpStatus.OK, result.getStatusCode())
        );
    }

}