package com.cognizant.jfcogni.trainingproject.views;

public class GitHubRepoToCreateView {

    public GitHubRepoToCreateView(){}


    public GitHubRepoToCreateView(String name, String description, boolean privateRepo, String homepage) {
        this.name = name;
        this.description = description;
        this.privateRepo = privateRepo;
        this.homepage = homepage;
    }

    private String name;

    private String description;
    private boolean privateRepo;
    private String homepage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPrivateRepo() {
        return privateRepo;
    }

    public void setPrivateRepo(boolean privateRepo) {
        this.privateRepo = privateRepo;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
}
