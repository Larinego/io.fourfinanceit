/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.fourfinanceit.rest;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import io.fourfinanceit.model.User;
import io.fourfinanceit.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("authapi/")
//@PreAuthorize( "hasRole(@roles.ADMIN)" )
public class AuthRestController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> addUser(@RequestBody @Valid User user, BindingResult bindingResult) throws Exception {

        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (user == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<String>("", headers, HttpStatus.BAD_REQUEST);
        }
        Gson gson = new Gson();

        String token = this.userService.saveUser(user);
        return new ResponseEntity<String>(gson.toJson(token), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getToken(@RequestParam("username") final String username, @RequestParam("password") final String password) {
        String token = userService.login(username, password);
        Gson gson = new Gson();
        if (StringUtils.isEmpty(token)) {
            return new ResponseEntity<String>("", HttpStatus.OK);
        }

        return new ResponseEntity<String>(gson.toJson(token), HttpStatus.OK);
    }
}
