package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.user.RoleStatus;
import dev.guarmo.whales.model.user.dto.GetContentWithoutHistoryUserDto;
import dev.guarmo.whales.model.user.dto.GetUserCredentialsDto;
import dev.guarmo.whales.model.user.dto.GetUserWithReferralsDto;
import dev.guarmo.whales.model.user.dto.PostUserDto;
import dev.guarmo.whales.service.DepositService;
import dev.guarmo.whales.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final DepositService depositService;

    @GetMapping("/reflink/{userTelegramId}")
    public String generateReferralLinkForUser(@PathVariable String userTelegramId) {
        return userService.generateRefLinkForUser(userTelegramId);
    }

    @PostMapping("/register")
    public GetUserCredentialsDto addUser(@RequestBody PostUserDto postUserDto) {
        return userService.addUser(postUserDto, RoleStatus.USER);
    }

    @GetMapping("/reftree/{tgid}")
    public GetUserWithReferralsDto getReferralsTree(@PathVariable String tgid) {
        return userService.getFourLevelsReferralTree(tgid);
    }

    @GetMapping("/{tgid}")
    public GetContentWithoutHistoryUserDto getContentUserDtoByTgId(@PathVariable String tgid) {
        return userService.findByLogin(tgid);
    }
}
