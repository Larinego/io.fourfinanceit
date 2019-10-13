package io.fourfinanceit.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class RootRestController {

	@RequestMapping(value = "/")
	public void redirectToSwagger(HttpServletResponse response) throws IOException {
		response.sendRedirect("/application/swagger-ui.html");
	}

}

