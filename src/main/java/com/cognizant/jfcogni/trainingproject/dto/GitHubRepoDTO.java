package com.cognizant.jfcogni.trainingproject.dto;

public class GitHubRepoDTO {

    public GitHubRepoDTO(){}

    public GitHubRepoDTO(Long id, String name, GitHubUserDTO owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    private Long id;
    private String name;
    private GitHubUserDTO owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GitHubUserDTO getOwner() {
        return owner;
    }

    public void setOwner(GitHubUserDTO owner) {
        this.owner = owner;
    }



}
