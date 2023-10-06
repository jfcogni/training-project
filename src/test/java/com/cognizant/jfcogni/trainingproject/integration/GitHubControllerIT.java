package com.cognizant.jfcogni.trainingproject.integration;

import com.cognizant.jfcogni.trainingproject.controllers.GitHubController;
import com.cognizant.jfcogni.trainingproject.dto.GitHubUserDTO;
import com.cognizant.jfcogni.trainingproject.services.GitHubServiceImpl;
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

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(GitHubController.class)
class GitHubControllerIT {

    private final String validAuthorizationToken = "validAuthorizationToken";
    private final String blankAuthorizationToken = "";
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

    public void testGetUserInfoWithBlankAuthorizationUserTokenResponseUnauthorized()throws Exception {
        //given
        RequestBuilder request = MockMvcRequestBuilders
                .get("/get-user-info")
                .header(HttpHeaders.AUTHORIZATION,blankAuthorizationToken)
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
                .header(HttpHeaders.AUTHORIZATION,validAuthorizationToken+"*")
                .accept(MediaType.APPLICATION_JSON);
        when(gitHubService.getUserByAuthToken(anyString())).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED,HttpStatus.UNAUTHORIZED.getReasonPhrase()));

        //then
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
                .andReturn();

        verify(gitHubService, times(1)).getUserByAuthToken(anyString());

    }

    @Test
    public void testGetUserInfoWithValidAuthorizationUserTokenResponseOk() throws Exception {
        //given
        GitHubUserDTO expected = new GitHubUserDTO("JesusName","jesusLogin");
        //given
        RequestBuilder request = MockMvcRequestBuilders
                .get("/get-user-info")
                .header(HttpHeaders.AUTHORIZATION,validAuthorizationToken)
                .accept(MediaType.APPLICATION_JSON);
        when(gitHubService.getUserByAuthToken(anyString())).thenReturn(expected);

        //then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.login").value(expected.getLogin()))
                .andReturn();

        verify(gitHubService, times(1)).getUserByAuthToken(anyString());

    }

}