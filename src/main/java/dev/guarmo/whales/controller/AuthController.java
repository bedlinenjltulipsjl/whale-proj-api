package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.user.RoleStatus;
import dev.guarmo.whales.model.user.dto.GetUserCredentialsDto;
import dev.guarmo.whales.model.user.dto.PostUserDto;
import dev.guarmo.whales.security.TokenService;
import dev.guarmo.whales.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/token")
    public String token(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }

    @PostMapping("/register")
    public GetUserCredentialsDto addUser(@RequestBody PostUserDto postUserDto) {

        // CHECK HERE AND ADD REFERRAL ABOVE SOME NOTIFICATION ABOUT USER REGISTRATION

        return userService.addUser(postUserDto, RoleStatus.ADMIN);
    }
}
