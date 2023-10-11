package com.cognizant.jfcogni.trainingproject.integration;

import com.cognizant.jfcogni.trainingproject.controllers.GitHubController;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubRepoToCreateDTO;
import com.cognizant.jfcogni.trainingproject.dto.GitHubUserDTO;
import com.cognizant.jfcogni.trainingproject.services.GitHubServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(GitHubController.class)
public class GitHubControllerTestIT {

    private static final String VALID_AUTHORIZATION_TOKEN = "validAuthorizationToken";
    private static final String NOT_VALID_AUTHORIZATION_TOKEN = "*";
    private static final String BLANK_AUTHORIZATION_TOKEN = "";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GitHubServiceImpl gitHubService;

    @Test
    public void testGetUserInfoWithoutAuthorizationUserTokenResponseUnauthorized()throws Exception {
        //given
        RequestBuilder request = MockMvcRequestBuilders
                .get("/get-user-info")
                .accept(MediaType.APPLICATION_JSON);

        //then
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void testGetUserInfoWithBlankAuthorizationUserTokenResponseUnauthorized()throws Exception {
        //given
        RequestBuilder request = MockMvcRequestBuilders
                .get("/get-user-info")
                .header(HttpHeaders.AUTHORIZATION, BLANK_AUTHORIZATION_TOKEN)
                .accept(MediaType.APPLICATION_JSON);

        //then
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
                .andReturn();
    }

    @Test
    public void testGetUserInfoWithNotValidAuthorizationUserTokenResponseUnauthorized() throws Exception {
        //given
        RequestBuilder request = MockMvcRequestBuilders
                .get("/get-user-info")
                .header(HttpHeaders.AUTHORIZATION, NOT_VALID_AUTHORIZATION_TOKEN)
                .accept(MediaType.APPLICATION_JSON);
        when(gitHubService.getUserByAuthToken(NOT_VALID_AUTHORIZATION_TOKEN)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED,HttpStatus.UNAUTHORIZED.getReasonPhrase()));

        //then
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
                .andReturn();

        verify(gitHubService, times(1)).getUserByAuthToken(NOT_VALID_AUTHORIZATION_TOKEN);

    }

    @Test
    public void testGetUserInfoWithValidAuthorizationUserTokenResponseOk() throws Exception {
        //given
        GitHubUserDTO expected = new GitHubUserDTO("JesusName","jesusLogin");
        //given
        RequestBuilder request = MockMvcRequestBuilders
                .get("/get-user-info")
                .header(HttpHeaders.AUTHORIZATION, VALID_AUTHORIZATION_TOKEN)
                .accept(MediaType.APPLICATION_JSON);
        when(gitHubService.getUserByAuthToken(VALID_AUTHORIZATION_TOKEN)).thenReturn(expected);

        //then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.login").value(expected.getLogin()))
                .andReturn();

        verify(gitHubService, times(1)).getUserByAuthToken(VALID_AUTHORIZATION_TOKEN);

    }



    @Test
    public void testGetReposUserInfoWithoutAuthorizationUserTokenResponseUnauthorized()throws Exception {
        //given
        RequestBuilder request = MockMvcRequestBuilders
                .get("/get-repos-user-info")
                .accept(MediaType.APPLICATION_JSON);

        //then
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void testGetReposUserInfoWithBlankAuthorizationUserTokenResponseUnauthorized()throws Exception {
        //given
        RequestBuilder request = MockMvcRequestBuilders
                .get("/get-repos-user-info")
                .header(HttpHeaders.AUTHORIZATION, BLANK_AUTHORIZATION_TOKEN)
                .accept(MediaType.APPLICATION_JSON);

        //then
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
                .andReturn();
    }

    @Test
    public void testGetReposUserInfoWithNotValidAuthorizationUserTokenResponseUnauthorized() throws Exception {
        //given
        RequestBuilder request = MockMvcRequestBuilders
                .get("/get-repos-user-info")
                .header(HttpHeaders.AUTHORIZATION, NOT_VALID_AUTHORIZATION_TOKEN)
                .accept(MediaType.APPLICATION_JSON);
        when(gitHubService.getReposByAuthToken(NOT_VALID_AUTHORIZATION_TOKEN)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED,HttpStatus.UNAUTHORIZED.getReasonPhrase()));

        //then
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
                .andReturn();

        verify(gitHubService, times(1)).getReposByAuthToken(NOT_VALID_AUTHORIZATION_TOKEN);

    }

    @Test
    public void testGetReposUserInfoWithValidAuthorizationUserTokenResponseOk() throws Exception {
        //given
        List<GitHubRepoDTO> expected = Arrays.asList(new GitHubRepoDTO(1L,"NameRepo", new GitHubUserDTO("JesusName","jesusLogin")));
        //given
        RequestBuilder request = MockMvcRequestBuilders
                .get("/get-repos-user-info")
                .header(HttpHeaders.AUTHORIZATION, VALID_AUTHORIZATION_TOKEN)
                .accept(MediaType.APPLICATION_JSON);
        when(gitHubService.getReposByAuthToken(VALID_AUTHORIZATION_TOKEN)).thenReturn(expected);

        //then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(expected.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(expected.get(0).getName()))
                .andExpect(jsonPath("$[0].owner.name").value(expected.get(0).getOwner().getName()))
                .andExpect(jsonPath("$[0].owner.login").value(expected.get(0).getOwner().getLogin()))
                .andReturn();

        verify(gitHubService, times(1)).getReposByAuthToken(VALID_AUTHORIZATION_TOKEN);

    }



    @Test
    public void testGetCreateRepositoryResponseMethodNotAllowed()throws Exception {
        //given
        RequestBuilder request = MockMvcRequestBuilders
                .get("/create-repository")
                .accept(MediaType.APPLICATION_JSON);

        //then
        mockMvc.perform(request)
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    @Test
    public void testPostCreateRepositoryWithoutObjectRepoToCreateDTOResponseBadRequest()throws Exception {
        //given
        RequestBuilder request = MockMvcRequestBuilders
                .post("/create-repository")
                .accept(MediaType.APPLICATION_JSON);

        //then
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testPostCreateRepositoryWithoutAuthorizationUserTokenResponseUnauthorized()throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO("NameRepo", "DescriptionRepo", false, "RepoHomePage");
        RequestBuilder request = MockMvcRequestBuilders
                .post("/create-repository")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(repoToCreate));


        //then
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void testCreateRepositoryWithBlankAuthorizationUserTokenResponseUnauthorized()throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO("NameRepo", "DescriptionRepo", false, "RepoHomePage");
        RequestBuilder request = MockMvcRequestBuilders
                .post("/create-repository")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BLANK_AUTHORIZATION_TOKEN)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(repoToCreate));

        //then
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
                .andReturn();
    }

    @Test
    public void testCreateRepositoryWithNotValidAuthorizationUserTokenResponseUnauthorized() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO("NameRepo", "DescriptionRepo", false, "RepoHomePage");
        RequestBuilder request = MockMvcRequestBuilders
                .post("/create-repository")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, NOT_VALID_AUTHORIZATION_TOKEN)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(repoToCreate));
        when(gitHubService.createRepoByAuthToken(NOT_VALID_AUTHORIZATION_TOKEN,repoToCreate)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED,HttpStatus.UNAUTHORIZED.getReasonPhrase()));

        //then
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
                .andReturn();

        verify(gitHubService, times(1)).createRepoByAuthToken(NOT_VALID_AUTHORIZATION_TOKEN,repoToCreate);

    }

    @Test
    public void testCreateRepositoryWithValidAuthorizationUserTokenResponseCreated() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        GitHubRepoToCreateDTO repoToCreate = new GitHubRepoToCreateDTO("NameRepo", "DescriptionRepo", false, "RepoHomePage");
        GitHubRepoDTO expected = new GitHubRepoDTO(1L,"NameRepo", new GitHubUserDTO("JesusName","jesusLogin"));
        RequestBuilder request = MockMvcRequestBuilders
                .post("/create-repository")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, VALID_AUTHORIZATION_TOKEN)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(repoToCreate));

        when(gitHubService.createRepoByAuthToken(VALID_AUTHORIZATION_TOKEN,repoToCreate)).thenReturn(expected);

        //then
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.owner.name").value(expected.getOwner().getName()))
                .andExpect(jsonPath("$.owner.login").value(expected.getOwner().getLogin()))
                .andReturn();

        verify(gitHubService, times(1)).createRepoByAuthToken(VALID_AUTHORIZATION_TOKEN,repoToCreate);

    }

}