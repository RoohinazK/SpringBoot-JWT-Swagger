package com.example.springrolejwt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.springrolejwt.config.TokenProvider;
import com.example.springrolejwt.model.AuthToken;
import com.example.springrolejwt.model.LoginUser;
import com.example.springrolejwt.model.User;
import com.example.springrolejwt.model.UserDto;
import com.example.springrolejwt.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value="User Portal", description="CRUD operations on Users", protocols="http")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    
    @Autowired
    private TokenProvider jwtTokenUtil;
    //private TokenProvider jwtTokenUtil;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Aunthenticate a User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Authenticated"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            
    })
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody LoginUser loginUser) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new AuthToken(token));
    }
    
    @ApiOperation(value = "Add a User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Added a User"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            
    })
    @RequestMapping(value="/register", method = RequestMethod.POST)
    public User saveUser(@RequestBody UserDto user){
        return userService.save(user);
    }


    @ApiOperation(value = "Admin Endpoint")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Pinged Admin"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            
    })
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/adminping", method = RequestMethod.GET)
    public String adminPing(){
        return "Only Admins Can Read This";
    }
    
    @ApiOperation(value = "User Endpoint")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Pinged User"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            
    })
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/userping", method = RequestMethod.GET)
    public String userPing(){
        return "Any User Can Read This";
    }

}
