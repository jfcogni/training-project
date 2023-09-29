package com.cognizant.jfcogni.trainingproject.controllers;

import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoToCreateDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubUserDTO;
import com.cognizant.jfcogni.trainingproject.services.GitHubServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class GitHubControllerTest {

    @Mock
    private GitHubServiceImpl gitHubService;

    @InjectMocks
    private GitHubController gitHubController;

    @Test
    public void testGetUserInfo_withNullRequest_responseStatusException() {
        //given
        HttpServletRequest request = null;

        //when
        Executable executable = () -> gitHubController.getUserInfo(request);

        //then
        assertThrows(ResponseStatusException.class,executable);

    }

    @Test
    public void testGetUserInfo_withNotValidAuthorizationUserToken() {
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
    public void testGetUserInfo_withValidAuthorizationUserToken_ResponseStatus200() throws IOException, InterruptedException {
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



    @Test
    public void testGetReposUserInfo_withNullRequest_responseStatusException() {
        //given
        HttpServletRequest request = null;

        //when
        Executable executable = () -> gitHubController.getReposUserInfo(request);

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
        Executable executable = () -> gitHubController.getReposUserInfo(request);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testGetReposUserInfo_withValidAuthorizationUserToken_ResponseStatus200() throws IOException, InterruptedException {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String authorizationToke = "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";
        List<GitHubRepoDTO> expected = Arrays.asList(new GitHubRepoDTO(1L,"NameRepo", new GitHubUserDTO("JesusName","jesusLogin")));
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationToke);
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
    }



    @Test
    public void testCreateRepository_withNullRequest_responseStatusException() {
        //given
        HttpServletRequest request = null;
        GitHubRepoToCreateDTO gitHubRepoToCreateDTO = null;

        //when
        Executable executable = () -> gitHubController.createRepository(request,gitHubRepoToCreateDTO);

        //then
        assertThrows(ResponseStatusException.class,executable);

    }

    @Test
    public void testCreateRepository_withNotValidAuthorizationUserToken() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String authorizationToke = "";
        GitHubRepoToCreateDTO gitHubRepoToCreateDTO = null;
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationToke);

        //when
        Executable executable = () -> gitHubController.createRepository(request,gitHubRepoToCreateDTO);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepository_withEmptyRepoToCreate_ResponseStatusException() throws IOException, InterruptedException {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String authorizationToke = "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";
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO();
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationToke);
        when(gitHubService.createRepoByAuthToken(anyString(),any())).thenThrow(ResponseStatusException.class);

        //when
        Executable executable = () -> gitHubController.createRepository(request,repoToCreate);

        //then
        assertThrows(ResponseStatusException.class,executable);
    }

    @Test
    public void testCreateRepository_withValidAuthorizationUserToken_ResponseStatus200() throws IOException, InterruptedException {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String authorizationToke = "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";
        GitHubRepoDTO expected = new GitHubRepoDTO(1L,"NameRepo", new GitHubUserDTO("JesusName","jesusLogin"));
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO("NameRepo", "DescriptionRepo", false, "RepoHomePage");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationToke);
        when(gitHubService.createRepoByAuthToken(anyString(),any())).thenReturn(expected);
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
    }

}