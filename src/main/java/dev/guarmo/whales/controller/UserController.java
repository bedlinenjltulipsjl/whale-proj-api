package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.user.RoleStatus;
import dev.guarmo.whales.model.user.dto.*;
import dev.guarmo.whales.service.AllTransactionService;
import dev.guarmo.whales.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(allowedHeaders = "*")
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AllTransactionService allTransactionService;

    @GetMapping("/reflink")
    public String generateReferralLinkForUser(Authentication authentication) {
        return userService.generateRefLinkForUser(authentication.getName());
    }

    @PostMapping("/register")
    public GetUserCredentialsDto addUser(@RequestBody PostUserDto postUserDto) {
        return userService.addUser(postUserDto, RoleStatus.USER);
    }

    @GetMapping("/reftree")
    public GetFullDto getReferralsTree(Authentication authentication) {
        return userService.getFourLevelsReferralTree(authentication.getName());
    }

    @GetMapping("/by-token")
    public GetFullDto getContentUserDtoByTgId(Authentication authentication) {
        GetFullDto fullDtoByLogin = userService.findFullDtoByLogin(authentication.getName());
        fullDtoByLogin.setTransactions(allTransactionService.getAllTypesOfTransactions(authentication.getName()));
        return fullDtoByLogin;
    }

    @GetMapping("/top")
    public GetTopUserDto getContentUserDtoByTgId() {
        List<GetTopUserDto> topTen = userService.getTopTen();
        return null;
    }
}
