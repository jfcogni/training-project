package com.cognizant.jfcogni.trainingproject.dto;


public class GitHubUserDTO {

    public GitHubUserDTO() {
    }
    public GitHubUserDTO(String name, String login) {
        this.name = name;
        this.login = login;
    }


    private String name;

    private String login;


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
