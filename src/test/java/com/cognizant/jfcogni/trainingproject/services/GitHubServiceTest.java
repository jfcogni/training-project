package com.cognizant.jfcogni.trainingproject.services;

import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoToCreateDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubUserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    private final String fieldGitHubApiUrlInGitHubService = "gitHubApiUrl";
    private final String gitHubApiUrl = "http://www.MockedURL.com";
    private final String blankGitHubApiUrl = "";
    private final String validAuthorizationToken = "validAuthorizationToken";
    private final String notValidAuthorizationToken = "";

    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse response;

    @InjectMocks
    private GitHubServiceImpl gitHubService;

    // TEST FOR METHOD getUserByAuthToken
    @Test
    public void testGetUserByAuthToken_nullGitHubApiUrl(){
        //given
        ReflectionTestUtils.setField(gitHubService,fieldGitHubApiUrlInGitHubService,null);

        //when
        Executable executable = () -> gitHubService.getUserByAuthToken(validAuthorizationToken);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetUserByAuthToken_blankGitHubApiUrl() {
        //given
        ReflectionTestUtils.setField(gitHubService,fieldGitHubApiUrlInGitHubService,blankGitHubApiUrl);

        //when
        Executable executable = () -> gitHubService.getUserByAuthToken(validAuthorizationToken);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetUserByAuthToken_withNotValidAuthorizationToken_responseNot200() throws Exception {
        //given
        ReflectionTestUtils.setField(gitHubService,fieldGitHubApiUrlInGitHubService, gitHubApiUrl);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.BAD_REQUEST.value());

        //when
        Executable executable = () -> gitHubService.getUserByAuthToken(notValidAuthorizationToken);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(response).statusCode();
    }

    @Test
    public void testGetUserByAuthToken_withValidAuthorizationToken() throws IOException, InterruptedException {
        //given
        ReflectionTestUtils.setField(gitHubService,fieldGitHubApiUrlInGitHubService, gitHubApiUrl);
        GitHubUserDTO expected = new GitHubUserDTO("JesusName","jesusLogin");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.OK.value());

        ObjectMapper objectMapper = new ObjectMapper();
        when(response.body()).thenReturn(objectMapper.writeValueAsString(expected));

        //when
        GitHubUserDTO result = gitHubService.getUserByAuthToken(validAuthorizationToken);

        //then
        assertAll(
                () -> assertEquals(expected.getName(),result.getName()),
                () -> assertEquals(expected.getLogin(),result.getLogin())
        );

        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(response).statusCode();
        verify(response).body();

    }


    // TEST FOR METHOD getReposByAuthToken
    @Test
    public void testGetReposByAuthToken_nullGitHubApiUrl(){
        //given
        ReflectionTestUtils.setField(gitHubService,fieldGitHubApiUrlInGitHubService,null);

        //when
        Executable executable = () -> gitHubService.getReposByAuthToken(validAuthorizationToken);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetReposByAuthToken_blankGitHubApiUrl(){
        //given
        ReflectionTestUtils.setField(gitHubService,fieldGitHubApiUrlInGitHubService,blankGitHubApiUrl);

        //when
        Executable executable = () -> gitHubService.getReposByAuthToken(validAuthorizationToken);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetReposByAuthToken_withNotValidAuthorizationToken_responseNot200() throws IOException, InterruptedException {
        //given
        ReflectionTestUtils.setField(gitHubService,fieldGitHubApiUrlInGitHubService, gitHubApiUrl);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.BAD_REQUEST.value());

        //when
        Executable executable = () -> gitHubService.getReposByAuthToken(notValidAuthorizationToken);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(response).statusCode();
    }

    @Test
    public void testGetReposByAuthToken_withValidAuthorizationToken() throws IOException, InterruptedException {
         //given
        ReflectionTestUtils.setField(gitHubService,fieldGitHubApiUrlInGitHubService, gitHubApiUrl);
        List<GitHubRepoDTO> expected = Arrays.asList(new GitHubRepoDTO (1L,"RepoName", new GitHubUserDTO("JesusName","jesusLogin")));

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.OK.value());

        ObjectMapper objectMapper = new ObjectMapper();
        when(response.body()).thenReturn(objectMapper.writeValueAsString(expected));

        //when
        List<GitHubRepoDTO>  result = gitHubService.getReposByAuthToken("validGitHubAuthorizationToken");

        //then
        assertAll(
                () -> assertEquals(expected.get(0).getId(),result.get(0).getId()),
                () -> assertEquals(expected.get(0).getName(),result.get(0).getName()),
                () -> assertEquals(expected.size(),result.size())
        );
        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(response).statusCode();
        verify(response).body();
    }


    // TEST FOR METHOD createRepoByAuthToken
    @Test
    public void testCreateRepoByAuthToken_nullGitHubApiUrl() {
        //given
        ReflectionTestUtils.setField(gitHubService,fieldGitHubApiUrlInGitHubService,null);

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(validAuthorizationToken, new GitHubRepoToCreateDTO());

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepoByAuthToken_blankGitHubApiUrl(){
        //given
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl",blankGitHubApiUrl);

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(validAuthorizationToken, new GitHubRepoToCreateDTO());

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepoByAuthToken_withNullRepository() {
        //given
        GitHubRepoToCreateDTO repoToCreate = null;
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl",gitHubApiUrl);

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(validAuthorizationToken, repoToCreate);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepoByAuthToken_withRepositoryValuesBlank() {
        //given
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO();
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl",gitHubApiUrl);

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(validAuthorizationToken,repoToCreate);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepoByAuthToken_withRepositoryValuesBlank2(){
        //given
        GitHubRepoToCreateDTO repoToCreate = mock(GitHubRepoToCreateDTO.class);
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl",gitHubApiUrl);
        when(repoToCreate.getName()).thenReturn("nombre");

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(validAuthorizationToken,repoToCreate);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(repoToCreate).getName();
        verify(repoToCreate).getDescription();
        verify(repoToCreate, never()).getHomepage();
    }
    @Test
    public void testCreateRepoByAuthToken_failToCreateRepository_ResponseNot201() throws IOException, InterruptedException {
        //given
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO("RepoName", "RepoDescription", false, "HomePage" );
        ReflectionTestUtils.setField(gitHubService,fieldGitHubApiUrlInGitHubService,gitHubApiUrl);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.BAD_REQUEST.value());

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(validAuthorizationToken,repoToCreate);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(response, times(1)).statusCode();
    }

    @Test
    public void testCreateRepoByAuthToken_createRepositoryOk_Response201() throws IOException, InterruptedException {


        //given
        GitHubRepoDTO expected = new GitHubRepoDTO (1L,"RepoName", new GitHubUserDTO("JesusName","jesusLogin"));
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO("RepoName", "RepoDescription", false, "HomePage" );
        ReflectionTestUtils.setField(gitHubService,fieldGitHubApiUrlInGitHubService,gitHubApiUrl);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.CREATED.value());

        ObjectMapper objectMapper = new ObjectMapper();
        when(response.body()).thenReturn(objectMapper.writeValueAsString(expected));

        //when
        GitHubRepoDTO  result = gitHubService.createRepoByAuthToken(validAuthorizationToken,repoToCreate);

        //then
        assertAll(
                () -> assertEquals(expected.getId(),result.getId()),
                () -> assertEquals(expected.getName(),result.getName()),
                () -> assertEquals(expected.getOwner().getName(),result.getOwner().getName()),
                () -> assertEquals(expected.getOwner().getLogin(),result.getOwner().getLogin())
        );
        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(response, times(1)).statusCode();
        verify(response).body();
    }
}