package com.cognizant.jfcogni.trainingproject;


import com.cognizant.jfcogni.trainingproject.controllers.GitHubControllerTest;
import com.cognizant.jfcogni.trainingproject.integration.GitHubControllerIT;
import com.cognizant.jfcogni.trainingproject.services.GitHubServiceTest;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;


@RunWith(JUnitPlatform.class)
@SelectClasses({
		GitHubServiceTest.class,
		GitHubControllerTest.class,
		GitHubControllerIT.class
		})
class TrainingProjectApplicationTests {


}
