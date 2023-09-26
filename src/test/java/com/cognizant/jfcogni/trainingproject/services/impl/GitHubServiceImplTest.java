package com.cognizant.jfcogni.trainingproject.services;

import com.cognizant.jfcogni.trainingproject.dto.GitHubUserDTO;
import com.cognizant.jfcogni.trainingproject.services.impl.GitHubServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    @Mock
    private GitHubServiceImpl gitHubService;

    @Test
    public void testGetUserByAuthToken_nullGitHubApiUrl() throws Throwable {
        //given
        Mockito.when(gitHubService.getUserByAuthToken(anyString())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubService.getUserByAuthToken("validGitHubAuthorizationToken");

        //then
        assertThrows(ResponseStatusException.class,executable);
    }


    @Test
    public void testGetUserByAuthToken_blankGitHubApiUrl() throws IOException, InterruptedException {
        //given
        Mockito.when(gitHubService.getUserByAuthToken(anyString())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubService.getUserByAuthToken("validGitHubAuthorizationToken");

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetUserByAuthToken_withNotValidAuthorizationToken_responseNot200() throws IOException, InterruptedException {
        //given
        Mockito.when(gitHubService.getUserByAuthToken(anyString())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubService.getUserByAuthToken("blankGitHubAuthorizationToken");

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetUserByAuthToken_withValidAuthorizationToken() throws IOException, InterruptedException {
        //given
        GitHubUserDTO expected = new GitHubUserDTO("JesusName","jesusLogin");
        Mockito.when(gitHubService.getUserByAuthToken(anyString())).thenReturn(expected);

        //when
        GitHubUserDTO result = gitHubService.getUserByAuthToken("validGitHubAuthorizationToken");

        //then
        assertAll(
                () -> assertEquals(expected.getName(),result.getName()),
                () -> assertEquals(expected.getLogin(),result.getLogin())
        );

    }

}