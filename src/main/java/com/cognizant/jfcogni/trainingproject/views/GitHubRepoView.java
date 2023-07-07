package com.cognizant.jfcogni.trainingproject.views;

public class GitHubRepoView {

    private Long id;
    private String name;
    private GitHubUserView owner;

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

    public GitHubUserView getOwner() {
        return owner;
    }

    public void setOwner(GitHubUserView owner) {
        this.owner = owner;
    }
}
