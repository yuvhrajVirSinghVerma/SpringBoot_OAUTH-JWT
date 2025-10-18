package com.example.SpringOauth;

import com.example.SpringOauth.Entities.User;
import com.example.SpringOauth.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
// in yml file if we have scope openid then OidcUserService loaduser is called , if not contains openid scope then  DefaultOAuth2UserService loaduser is called
@Service
public class CustomOAuthService extends OidcUserService {

    @Autowired
    UserRepo userRepo;
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        String providerId = oidcUser.getSubject();


        try {
            User user = userRepo.findByEmail(email).orElse(new User());
            user.setEmail(email);
            user.setName(name);
            userRepo.save(user);
            return oidcUser;
        } catch (Exception ex) {
            System.out.println("Exception "+ex);
            throw new RuntimeException("Error processing OAuth2 user"+ ex);
        }
    }
}
