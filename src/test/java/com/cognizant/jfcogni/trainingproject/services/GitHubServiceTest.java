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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GitHubServiceTest {

    private static final String FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE = "gitHubApiUrl";
    private static final String GITHUB_API_URL = "http://www.MockedURL.com";
    private static final String BLANK_GITHUB_API_URL = "";
    private static final String VALID_AUTHORIZATION_TOKEN = "validAuthorizationToken";
    private static final String NOT_VALID_AUTHORIZATION_TOKEN = "";

    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse response;

    @InjectMocks
    private GitHubServiceImpl gitHubService;

    // TEST FOR METHOD getUserByAuthToken
    @Test
    public void testGetUserByAuthTokenNullGitHubApiUrl(){
        //given
        ReflectionTestUtils.setField(gitHubService, FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE,null);

        //when
        Executable executable = () -> gitHubService.getUserByAuthToken(VALID_AUTHORIZATION_TOKEN);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetUserByAuthTokenBlankGitHubApiUrl() {
        //given
        ReflectionTestUtils.setField(gitHubService, FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE, BLANK_GITHUB_API_URL);

        //when
        Executable executable = () -> gitHubService.getUserByAuthToken(VALID_AUTHORIZATION_TOKEN);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetUserByAuthTokenWithNotValidAuthorizationTokenResponseNot200() throws Exception {
        //given
        ReflectionTestUtils.setField(gitHubService, FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE, GITHUB_API_URL);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.BAD_REQUEST.value());

        //when
        Executable executable = () -> gitHubService.getUserByAuthToken(NOT_VALID_AUTHORIZATION_TOKEN);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(response).statusCode();
    }

    @Test
    public void testGetUserByAuthTokenWithValidAuthorizationTokenAndUserReturnNullUser() throws IOException, InterruptedException {
        //given
        ReflectionTestUtils.setField(gitHubService, FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE, GITHUB_API_URL);

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.OK.value());

        ObjectMapper objectMapper = new ObjectMapper();
        when(response.body()).thenReturn(objectMapper.writeValueAsString(null));

        //when
        GitHubUserDTO result = gitHubService.getUserByAuthToken(VALID_AUTHORIZATION_TOKEN);

        assertEquals(null,result);



        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(response).statusCode();
        verify(response).body();

    }

    @Test
    public void testGetUserByAuthTokenWithValidAuthorizationTokenAndUserWithoutName() throws IOException, InterruptedException {
        //given
        ReflectionTestUtils.setField(gitHubService, FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE, GITHUB_API_URL);
        GitHubUserDTO expected = new GitHubUserDTO("","jesusLogin");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.OK.value());

        ObjectMapper objectMapper = new ObjectMapper();
        when(response.body()).thenReturn(objectMapper.writeValueAsString(expected));

        //when
        GitHubUserDTO result = gitHubService.getUserByAuthToken(VALID_AUTHORIZATION_TOKEN);

        //then
        assertAll(
                () -> assertEquals("Sin nombre definido en GitHub",result.getName()),
                () -> assertEquals(expected.getLogin(),result.getLogin())
        );

        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(response).statusCode();
        verify(response).body();

    }

    @Test
    public void testGetUserByAuthTokenWithValidAuthorizationToken() throws IOException, InterruptedException {
        //given
        ReflectionTestUtils.setField(gitHubService, FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE, GITHUB_API_URL);
        GitHubUserDTO expected = new GitHubUserDTO("JesusName","jesusLogin");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.OK.value());

        ObjectMapper objectMapper = new ObjectMapper();
        when(response.body()).thenReturn(objectMapper.writeValueAsString(expected));

        //when
        GitHubUserDTO result = gitHubService.getUserByAuthToken(VALID_AUTHORIZATION_TOKEN);

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
    public void testGetReposByAuthTokenNullGitHubApiUrl(){
        //given
        ReflectionTestUtils.setField(gitHubService, FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE,null);

        //when
        Executable executable = () -> gitHubService.getReposByAuthToken(VALID_AUTHORIZATION_TOKEN);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetReposByAuthTokenBlankGitHubApiUrl(){
        //given
        ReflectionTestUtils.setField(gitHubService, FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE, BLANK_GITHUB_API_URL);

        //when
        Executable executable = () -> gitHubService.getReposByAuthToken(VALID_AUTHORIZATION_TOKEN);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetReposByAuthTokenWithNotValidAuthorizationTokenResponseNot200() throws IOException, InterruptedException {
        //given
        ReflectionTestUtils.setField(gitHubService, FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE, GITHUB_API_URL);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.BAD_REQUEST.value());

        //when
        Executable executable = () -> gitHubService.getReposByAuthToken(NOT_VALID_AUTHORIZATION_TOKEN);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(response).statusCode();
    }

    @Test
    public void testGetReposByAuthTokenWithValidAuthorizationToken() throws IOException, InterruptedException {
         //given
        ReflectionTestUtils.setField(gitHubService, FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE, GITHUB_API_URL);
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
    public void testCreateRepoByAuthTokenNullGitHubApiUrl() {
        //given
        ReflectionTestUtils.setField(gitHubService, FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE,null);

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(VALID_AUTHORIZATION_TOKEN, new GitHubRepoToCreateDTO());

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepoByAuthTokenBlankGitHubApiUrl(){
        //given
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl", BLANK_GITHUB_API_URL);

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(VALID_AUTHORIZATION_TOKEN, new GitHubRepoToCreateDTO());

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepoByAuthTokenWithNullRepository() {
        //given
        GitHubRepoToCreateDTO repoToCreate = null;
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl", GITHUB_API_URL);

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(VALID_AUTHORIZATION_TOKEN, repoToCreate);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepoByAuthTokenWithRepositoryValuesBlank() {
        //given
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO();
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl", GITHUB_API_URL);

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(VALID_AUTHORIZATION_TOKEN,repoToCreate);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepoByAuthTokenWithRepositoryValuesBlank2(){
        //given
        GitHubRepoToCreateDTO repoToCreate = mock(GitHubRepoToCreateDTO.class);
        ReflectionTestUtils.setField(gitHubService,"gitHubApiUrl", GITHUB_API_URL);
        when(repoToCreate.getName()).thenReturn("nombre");

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(VALID_AUTHORIZATION_TOKEN,repoToCreate);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(repoToCreate).getName();
        verify(repoToCreate).getDescription();
        verify(repoToCreate, never()).getHomepage();
    }
    @Test
    public void testCreateRepoByAuthTokenFailToCreateRepositoryResponseNot201() throws IOException, InterruptedException {
        //given
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO("RepoName", "RepoDescription", false, "HomePage" );
        ReflectionTestUtils.setField(gitHubService, FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE, GITHUB_API_URL);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.BAD_REQUEST.value());

        //when
        Executable executable = () -> gitHubService.createRepoByAuthToken(VALID_AUTHORIZATION_TOKEN,repoToCreate);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(response, times(1)).statusCode();
    }

    @Test
    public void testCreateRepoByAuthTokenCreateRepositoryOkResponse201() throws IOException, InterruptedException {


        //given
        GitHubRepoDTO expected = new GitHubRepoDTO (1L,"RepoName", new GitHubUserDTO("JesusName","jesusLogin"));
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO("RepoName", "RepoDescription", false, "HomePage" );
        ReflectionTestUtils.setField(gitHubService, FIELD_GITHUBAPIURL_IN_GIT_HUB_SERVICE, GITHUB_API_URL);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.CREATED.value());

        ObjectMapper objectMapper = new ObjectMapper();
        when(response.body()).thenReturn(objectMapper.writeValueAsString(expected));

        //when
        GitHubRepoDTO  result = gitHubService.createRepoByAuthToken(VALID_AUTHORIZATION_TOKEN,repoToCreate);

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