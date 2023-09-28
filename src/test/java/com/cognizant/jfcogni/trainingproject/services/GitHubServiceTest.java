package com.cognizant.jfcogni.trainingproject.services;

import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoToCreateDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubUserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    @Mock
    private GitHubServiceImpl gitHubService;


    // TEST FOR METHOD getUserByAuthToken
    @Test
    public void testGetUserByAuthToken_nullGitHubApiUrl() throws Throwable {
        //given
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl",null);
        Mockito.when(gitHubService.getUserByAuthToken(anyString())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubService.getUserByAuthToken("validGitHubAuthorizationToken");

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetUserByAuthToken_blankGitHubApiUrl() throws IOException, InterruptedException {
        //given
        String blankGitHubApiUrl = "";
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl",blankGitHubApiUrl);
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


    // TEST FOR METHOD getReposByAuthToken
    @Test
    public void testGetReposByAuthToken_nullGitHubApiUrl() throws Throwable {
        //given
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl",null);
        Mockito.when(gitHubService.getReposByAuthToken(anyString())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubService.getReposByAuthToken("validGitHubAuthorizationToken");

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetReposByAuthToken_blankGitHubApiUrl() throws IOException, InterruptedException {
        //given
        String blankGitHubApiUrl = "";
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl",blankGitHubApiUrl);
        Mockito.when(gitHubService.getReposByAuthToken(anyString())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubService.getReposByAuthToken("validGitHubAuthorizationToken");

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetReposByAuthToken_withNotValidAuthorizationToken_responseNot200() throws IOException, InterruptedException {
        //given
        Mockito.when(gitHubService.getUserByAuthToken(anyString())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubService.getUserByAuthToken("blankGitHubAuthorizationToken");

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetReposByAuthToken_withValidAuthorizationToken() throws IOException, InterruptedException {
        //given
        List<GitHubRepoDTO> expected = Arrays.asList(new GitHubRepoDTO (1L,"RepoName", new GitHubUserDTO("JesusName","jesusLogin")));
        Mockito.when(gitHubService.getReposByAuthToken(anyString())).thenReturn(expected);

        //when
        List<GitHubRepoDTO>  result = gitHubService.getReposByAuthToken("validGitHubAuthorizationToken");

        //then
        assertAll(
                () -> assertEquals(expected.get(0).getId(),result.get(0).getId()),
                () -> assertEquals(expected.get(0).getName(),result.get(0).getName())
        );

    }


    // TEST FOR METHOD createRepoByAuthToken
    @Test
    public void testCreateRepoByAuthToken_nullGitHubApiUrl() throws Throwable {
        //given
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl",null);
        Mockito.when(gitHubService.createRepoByAuthToken(anyString(),any())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(anyString(),any());

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepoByAuthToken_blankGitHubApiUrl() throws IOException, InterruptedException {
        //given
        String blankGitHubApiUrl = "";
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl",blankGitHubApiUrl);
        Mockito.when(gitHubService.createRepoByAuthToken(anyString(),any())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(anyString(),any());

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepoByAuthToken_withNullRepository() throws IOException, InterruptedException {
        //given
        GitHubRepoToCreateDTO repoToCreate = null;
        Mockito.when(gitHubService.createRepoByAuthToken(anyString(),any())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken("",repoToCreate);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepoByAuthToken_withRepositoryValuesBlank() throws IOException, InterruptedException {
        //given
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO();
        Mockito.when(gitHubService.createRepoByAuthToken(anyString(),any())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken("",repoToCreate);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }
    @Test
    public void testCreateRepoByAuthToken_failToCreateRepository_ResponseNot201() throws IOException, InterruptedException {
        //given
        GitHubRepoDTO expected = new GitHubRepoDTO (1L,"RepoName", new GitHubUserDTO("JesusName","jesusLogin"));
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO("RepoName", "RepoDescription", false, "HomePage" );
        Mockito.when(gitHubService.createRepoByAuthToken(anyString(),any())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken("",repoToCreate);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepoByAuthToken_createRepositoryOk_Response201() throws IOException, InterruptedException {
        //given
        GitHubRepoDTO expected = new GitHubRepoDTO (1L,"RepoName", new GitHubUserDTO("JesusName","jesusLogin"));
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO("RepoName", "RepoDescription", false, "HomePage" );
        Mockito.when(gitHubService.createRepoByAuthToken(anyString(),any())).thenReturn(expected);

        //when
        GitHubRepoDTO  result = gitHubService.createRepoByAuthToken("validGitHubAuthorizationToken",repoToCreate);

        //then
        assertAll(
                () -> assertEquals(expected.getId(),result.getId()),
                () -> assertEquals(expected.getName(),result.getName()),
                () -> assertEquals(expected.getOwner().getName(),result.getOwner().getName()),
                () -> assertEquals(expected.getOwner().getLogin(),result.getOwner().getLogin())
        );

    }
}