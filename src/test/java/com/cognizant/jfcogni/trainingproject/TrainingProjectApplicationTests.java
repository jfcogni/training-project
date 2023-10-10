package com.cognizant.jfcogni.trainingproject;


import com.cognizant.jfcogni.trainingproject.controllers.GitHubControllerTest;
import com.cognizant.jfcogni.trainingproject.integration.GitHubControllerTestIT;
import com.cognizant.jfcogni.trainingproject.services.GitHubServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
		GitHubServiceTest.class,
		GitHubControllerTest.class,
        GitHubControllerTestIT.class
		})
public class TrainingProjectApplicationTests {
}
