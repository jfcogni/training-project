package com.cognizant.jfcogni.trainingproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GitHubRepoToCreateDTO {


    @NotBlank
    //@Min(2)
    @Size(min = 2)
    private String name;

    @NotBlank
    @Size(min = 2)
    private String description;

    // TODO: PONER OBJETO BOOLEAN Y AGREGAR VALIDACION CON NOTNULL
    private boolean privateRepo;
    @NotBlank
    @Size(min = 2)
    private String homepage;

    public GitHubRepoToCreateDTO() {}

    public GitHubRepoToCreateDTO(String name, String description, boolean privateRepo, String homepage) {
        this.name = name;
        this.description = description;
        this.privateRepo = privateRepo;
        this.homepage = homepage;
    }

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
