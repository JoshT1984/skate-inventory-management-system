package com.skillstorm.skate_inventory_mgmt_system.services;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import com.skillstorm.skate_inventory_mgmt_system.models.User;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final AuthProvisioningService authProvisioningService;

    public CustomOidcUserService(AuthProvisioningService authProvisioningService) {
        this.authProvisioningService = authProvisioningService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = new LinkedHashMap<>(oidcUser.getClaims());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerUserId = oidcUser.getSubject();
        String email = oidcUser.getEmail();
        String firstName = oidcUser.getGivenName();
        String lastName = oidcUser.getFamilyName();

        User user = authProvisioningService.provisionUser(provider, providerUserId, email, firstName, lastName);
        authProvisioningService.enrichAttributes(attributes, user);

        return new DefaultOidcUser(authProvisioningService.toAuthorities(user, oidcUser.getAuthorities()),
                oidcUser.getIdToken(), new OidcUserInfo(attributes), "sub");
    }
}
