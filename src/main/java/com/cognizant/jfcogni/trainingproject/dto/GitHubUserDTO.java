package com.cognizant.jfcogni.trainingproject.dto;


public class GitHubUserDTO {

    private String name;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private String login;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
