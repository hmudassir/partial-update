package dev.portfolio.patch.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ProfileUpdateException.class)
	ProblemDetail rejected(ProfileUpdateException exception) {
		HttpStatus status = HttpStatus.valueOf(exception.status());
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
		problem.setTitle(status.getReasonPhrase());
		problem.setProperty("code", status == HttpStatus.NOT_FOUND ? "NOT_FOUND" : "BAD_REQUEST");
		return problem;
	}
}
