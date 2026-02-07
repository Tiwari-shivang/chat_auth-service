package com.chat.auth.controllers;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.chat.auth.DTOs.LoginResponse;
import com.chat.auth.enums.gender;
import com.chat.auth.enums.roles;
import com.chat.auth.models.Users;
import com.chat.auth.security.JWTUtils;
import com.chat.auth.services.AuthService;
import com.chat.auth.services.SecurityService;


@RestController
@RequestMapping("/oauth")
public class SSOController {
    @Autowired
    private RestTemplate template;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JWTUtils utils;
    @Value("${oauth.client-id}")
    private String client_id;

    @Value("${oauth.client-secret}")
    private String client_secret;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestParam String code){
        String oAuthURL = "https://oauth2.googleapis.com/token";
        MultiValueMap<String, String> oAuthParams = new LinkedMultiValueMap<>();
        oAuthParams.add("client_id", client_id);
        oAuthParams.add("client_secret", client_secret);
        oAuthParams.add("code", code);
        oAuthParams.add("redirect_uri", "http://localhost:8080/oauth/google");
        oAuthParams.add("grant_type", "authorization_code");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(oAuthParams, headers);
        ResponseEntity<Map> response = template.postForEntity(oAuthURL, request, Map.class);
        String idToken = (String) response.getBody().get("id_token");
        String userInfoURL = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        ResponseEntity<Map> userInfoResponse = template.getForEntity(userInfoURL, Map.class);
        String token = "";
        if(userInfoResponse.getStatusCode() == HttpStatus.OK){
            Users user = null;
            String email = (String) userInfoResponse.getBody().get("email");
            Map<String, Object> userInfo = userInfoResponse.getBody();
            try{
                user = (Users) securityService.loadUserByUsername(email);
                token = utils.buildToken(user);
            }
            catch(Exception ex){
                Users newUser = new Users();
                newUser.setFirstName((String) userInfo.get("given_name"));
                newUser.setLastName((String) userInfo.get("family_name"));
                newUser.setEmail(email);
                newUser.setPassword(encoder.encode(UUID.randomUUID().toString()));
                newUser.setRole(roles.user);
                newUser.setGender(gender.others);
                newUser.setIsActive(true);
                newUser.setLastLogin(new Timestamp(System.currentTimeMillis()));
                newUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                newUser.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                user = newUser;
                token = utils.buildToken(newUser);
            }
        }
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
