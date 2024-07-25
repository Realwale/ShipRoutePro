package com.shiproutepro.backend.config;


import com.shiproutepro.backend.entities.AppUser;
import com.shiproutepro.backend.exception.BadCredentialsException;
import com.shiproutepro.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${security.max-failed-attempts}")
    private int MAX_FAILED_ATTEMPTS;

    @Value("${security.lock-time-duration}") // In minutes
    private int LOCK_TIME_DURATION;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();


        log.error("Authenticating user: {}", username);

        AppUser user = userRepository.findByEmail(username).orElseThrow(
                () -> new BadCredentialsException("Invalid email or password")
        );

        log.error("User found: {}", user.getEmail());
        log.error("Stored password hash: {}", user.getPassword());
        log.error("Provided raw password: {}", password);
        if (user.isAccountLocked()) {
            throw new LockedException("Your account is locked. Please try again later.");
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            resetFailedAttempts(user);
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        } else {
            handleFailedLogin(user);
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    private void handleFailedLogin(AppUser user) {
        user.increaseFailedAttempts();

        if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
            user.lockAccount(LOCK_TIME_DURATION);
        }

        userRepository.save(user);
    }

    private void resetFailedAttempts(AppUser user) {
        user.resetFailedAttempts();
        user.unlockAccount();
        userRepository.save(user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
