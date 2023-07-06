package com.cognizant.jfcogni.trainingproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE,reason = "H")
public class GitHubException extends Exception{
}
