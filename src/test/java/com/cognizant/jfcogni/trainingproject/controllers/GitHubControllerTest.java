package com.cognizant.jfcogni.trainingproject.controllers;

import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoToCreateDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubUserDTO;
import com.cognizant.jfcogni.trainingproject.services.GitHubServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GitHubControllerTest {

    private final String validAuthorizationToken = "validAuthorizationToken";
    private final String blankAuthorizationToken = "";
    @Mock
    private GitHubServiceImpl gitHubService;

    @InjectMocks
    private GitHubController gitHubController;

    @Test
    public void testGetUserInfoWithNullRequestResponseStatusException() throws IOException, InterruptedException {
        //given

        //when
        Executable executable = () -> gitHubController.getUserInfo(null);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(gitHubService, never()).getUserByAuthToken(anyString());

    }

    @Test
    public void testGetUserInfoWithBlankAuthorizationUserToken() throws IOException, InterruptedException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION,blankAuthorizationToken);

        //when
        Executable executable = () -> gitHubController.getUserInfo(request);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(gitHubService, never()).getUserByAuthToken(anyString());
    }

    @Test
    public void testGetUserInfoWithNotValidAuthorizationUserTokenResponseStatus401() throws IOException, InterruptedException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION,validAuthorizationToken);
        when(gitHubService.getUserByAuthToken(anyString())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubController.getUserInfo(request);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(gitHubService).getUserByAuthToken(validAuthorizationToken);
    }

    @Test
    public void testGetUserInfoWithValidAuthorizationUserTokenResponseStatus200() throws IOException, InterruptedException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION,validAuthorizationToken);
        GitHubUserDTO expected = new GitHubUserDTO("JesusName","jesusLogin");
        when(gitHubService.getUserByAuthToken(validAuthorizationToken)).thenReturn(expected);

        //when
        ResponseEntity<GitHubUserDTO> result = gitHubController.getUserInfo(request);


        //then
        assertAll(
                () -> assertEquals(expected.getName(), result.getBody().getName()),
                () -> assertEquals(expected.getLogin(), result.getBody().getLogin()),
                () -> assertEquals(HttpStatus.OK, result.getStatusCode())
        );
        verify(gitHubService).getUserByAuthToken(validAuthorizationToken);
    }



    @Test
    public void testGetReposUserInfoWithNullRequestResponseStatusException() throws IOException, InterruptedException {
        //given

        //when
        Executable executable = () -> gitHubController.getReposUserInfo(null);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(gitHubService, never()).getReposByAuthToken(anyString());

    }

    @Test
    public void testGetReposUserInfoWithBlankAuthorizationUserToken() throws IOException, InterruptedException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION,blankAuthorizationToken);

        //when
        Executable executable = () -> gitHubController.getReposUserInfo(request);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(gitHubService, never()).getReposByAuthToken(anyString());
    }

    @Test
    public void testGetReposUserInfoWithNotValidAuthorizationUserTokenResponseStatus401() throws IOException, InterruptedException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION,validAuthorizationToken);
        when(gitHubService.getReposByAuthToken(anyString())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubController.getReposUserInfo(request);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(gitHubService).getReposByAuthToken(anyString());
    }

    @Test
    public void testGetReposUserInfoWithValidAuthorizationUserTokenResponseStatus200() throws IOException, InterruptedException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION,validAuthorizationToken);
        List<GitHubRepoDTO> expected = Arrays.asList(new GitHubRepoDTO(1L,"NameRepo", new GitHubUserDTO("JesusName","jesusLogin")));
        when(gitHubService.getReposByAuthToken(anyString())).thenReturn(expected);

        //when
        ResponseEntity<List<GitHubRepoDTO>> result = gitHubController.getReposUserInfo(request);

        //then
        assertAll(
                () -> assertEquals(expected.get(0).getId(), result.getBody().get(0).getId()),
                () -> assertEquals(expected.get(0).getName(), result.getBody().get(0).getName()),
                () -> assertEquals(expected.get(0).getOwner().getName(), result.getBody().get(0).getOwner().getName()),
                () -> assertEquals(expected.get(0).getOwner().getLogin(), result.getBody().get(0).getOwner().getLogin()),
                () -> assertEquals(HttpStatus.OK, result.getStatusCode())
        );
        verify(gitHubService).getReposByAuthToken(anyString());
    }



    @Test
    public void testCreateRepositoryWithNullRequestResponseStatusException() throws IOException, InterruptedException {
        //given

        //when
        Executable executable = () -> gitHubController.createRepository(null,null);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(gitHubService, never()).createRepoByAuthToken(any(),any());

    }

    @Test
    public void testCreateRepositoryWithNotValidAuthorizationUserToken() throws IOException, InterruptedException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION,blankAuthorizationToken);

        //when
        Executable executable = () -> gitHubController.createRepository(request,null);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(gitHubService, never()).createRepoByAuthToken(any(),any());
    }

    @Test
    public void testCreateRepositoryWithEmptyRepoToCreateResponseStatusException() throws IOException, InterruptedException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION,validAuthorizationToken);
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO();
        when(gitHubService.createRepoByAuthToken(anyString(),any(GitHubRepoToCreateDTO.class))).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubController.createRepository(request,repoToCreate);

        //then
        assertThrows(ResponseStatusException.class,executable);
        verify(gitHubService).createRepoByAuthToken(any(),any());
    }

    @Test
    public void testCreateRepositoryWithValidAuthorizationUserTokenResponseStatus201() throws IOException, InterruptedException {
        //given
        GitHubRepoDTO expected = new GitHubRepoDTO(1L,"NameRepo", new GitHubUserDTO("JesusName","jesusLogin"));
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO("NameRepo", "DescriptionRepo", false, "RepoHomePage");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION,validAuthorizationToken);
        when(gitHubService.createRepoByAuthToken(anyString(),any(GitHubRepoToCreateDTO.class))).thenReturn(expected);
        //when
        ResponseEntity<GitHubRepoDTO> result = gitHubController.createRepository(request,repoToCreate);

        //then
        assertAll(
                () -> assertEquals(expected.getId(), result.getBody().getId()),
                () -> assertEquals(expected.getName(), result.getBody().getName()),
                () -> assertEquals(expected.getOwner().getName(), result.getBody().getOwner().getName()),
                () -> assertEquals(expected.getOwner().getLogin(), result.getBody().getOwner().getLogin()),
                () -> assertEquals(HttpStatus.CREATED, result.getStatusCode())
        );
        verify(gitHubService, times(1)).createRepoByAuthToken(anyString(),any(GitHubRepoToCreateDTO.class));
    }

}