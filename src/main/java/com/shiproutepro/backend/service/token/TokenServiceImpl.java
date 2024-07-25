package com.shiproutepro.backend.service.token;


import com.shiproutepro.backend.data.response.api.APIResponse;
import com.shiproutepro.backend.entities.AppUser;
import com.shiproutepro.backend.entities.JwtToken;
import com.shiproutepro.backend.exception.InvalidArgumentException;
import com.shiproutepro.backend.exception.ResourceAlreadyExistsException;
import com.shiproutepro.backend.exception.ResourceNotFoundException;
import com.shiproutepro.backend.mail.events.common.EventType;
import com.shiproutepro.backend.mail.events.common.onApplicationEvent;
import com.shiproutepro.backend.repository.token.TokenRepository;
import com.shiproutepro.backend.repository.user.UserRepository;
import com.shiproutepro.backend.utils.DateUtils;
import com.shiproutepro.backend.utils.EmailUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{

    private final TokenRepository tokenRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;


    @Override
    public void saveEmailConfirmToken(AppUser user) {
        String token = UUID.randomUUID().toString();
        JwtToken verificationToken = new JwtToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(DateUtils.calculateExpiryDate());
        tokenRepository.save(verificationToken);
        eventPublisher.publishEvent(new onApplicationEvent(user, token, EventType.REGISTRATION));
    }

    @Override
    public APIResponse confirmEmail(String token, HttpServletRequest request) {
        JwtToken confirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(()-> new ResourceNotFoundException("Null confirmation link"));

        AppUser user = confirmationToken.getUser();

        if (Objects.isNull(user)){
            throw new ResourceNotFoundException("User not found with email");
        }

        if(user.isEmailVerified()){
            throw new ResourceAlreadyExistsException("Your account is already verified. Proceed to login.");
        }

        if (confirmationToken.getExpiryDate().before(new Date())){
            tokenRepository.delete(confirmationToken);
            throw new InvalidArgumentException("Token is expired. Please request a new verification link at: "
                    + EmailUtils.applicationUrl(request)+"/api/v1/account/new-verification-link?email="+user.getEmail());
        }
        user.setEmailVerified(true);
        userRepository.save(user);

        return APIResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .hasError(false)
                .message("Email successfully verified")
                .data(null)
                .build();
    }

    @Override
    public APIResponse sendNewConfirmationLink(String email, HttpServletRequest request) {

        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Account not found"));

        if (appUser.isEmailVerified()) {
            throw new ResourceAlreadyExistsException("Account already verified, Proceed to login");
        }

        if(tokenRepository.existsByUser(appUser)){
            tokenRepository.delete(tokenRepository.findByUser(appUser));
        }
       eventPublisher.publishEvent(new onApplicationEvent(appUser, EmailUtils.applicationUrl(request), EventType.RESEND));

        return APIResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .hasError(false)
                .message("Please check your mail for a new verification link")
                .data(null)
                .build();
    }
}
