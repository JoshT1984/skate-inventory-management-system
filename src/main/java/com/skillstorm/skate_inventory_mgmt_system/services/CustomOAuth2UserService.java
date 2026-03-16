package com.skillstorm.skate_inventory_mgmt_system.services;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.skillstorm.skate_inventory_mgmt_system.models.User;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final AuthProvisioningService authProvisioningService;

    public CustomOAuth2UserService(AuthProvisioningService authProvisioningService) {
        this.authProvisioningService = authProvisioningService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = delegate.loadUser(userRequest);
        Map<String, Object> attributes = new LinkedHashMap<>(oauthUser.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerUserId = stringValue(attributes.get("sub"));
        String email = stringValue(attributes.get("email"));
        String firstName = stringValue(attributes.getOrDefault("given_name", attributes.get("name")));
        String lastName = stringValue(attributes.getOrDefault("family_name", "User"));

        User user = authProvisioningService.provisionUser(provider, providerUserId, email, firstName, lastName);
        authProvisioningService.enrichAttributes(attributes, user);

        String nameAttributeKey = attributes.containsKey("sub") ? "sub" : "email";
        return new DefaultOAuth2User(authProvisioningService.toAuthorities(user, oauthUser.getAuthorities()),
                attributes, nameAttributeKey);
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
