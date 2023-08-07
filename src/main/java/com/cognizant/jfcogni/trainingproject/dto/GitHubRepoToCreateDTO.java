package com.cognizant.jfcogni.trainingproject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.bind.DefaultValue;

public class GitHubRepoToCreateDTO {

    public GitHubRepoToCreateDTO(){}


    public GitHubRepoToCreateDTO(String name, String description, boolean privateRepo, String homepage) {
        this.name = name;
        this.description = description;
        this.privateRepo = privateRepo;
        this.homepage = homepage;
    }



    @NotBlank
    @Min(2)
    private String name;

    @NotBlank
    @Min(2)
    private String description;
    private boolean privateRepo;
    @NotBlank
    @Min(2)
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
