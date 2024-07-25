package com.shiproutepro.backend.service.token;


import com.shiproutepro.backend.data.response.api.APIResponse;
import com.shiproutepro.backend.entities.AppUser;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {

    void saveEmailConfirmToken(AppUser user);

    APIResponse confirmEmail(String token, HttpServletRequest request);

    APIResponse sendNewConfirmationLink(String email, HttpServletRequest request);
}
