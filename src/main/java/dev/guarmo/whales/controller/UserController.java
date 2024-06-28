package dev.guarmo.whales.controller;

import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.investmodel.dto.GetInvestModel;
import dev.guarmo.whales.model.investmodel.mapper.InvestModelMapper;
import dev.guarmo.whales.model.user.RoleStatus;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.model.user.dto.*;
import dev.guarmo.whales.model.user.mapper.UserMapper;
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
    private final UserHelper userHelper;
    private final UserMapper userMapper;
    private final InvestModelMapper investModelMapper;

    @GetMapping("/reflink")
    public String generateReferralLinkForUser(Authentication authentication) {
        return userHelper.generateRefLinkForUser(authentication.getName());
    }

    @PostMapping("/register")
    public GetUserCredentialsDto addUser(@RequestBody PostUserDto postUserDto) {
        return userService.addUser(postUserDto, RoleStatus.USER);
    }

    @GetMapping("/reftree")
    public GetFullDto getReferralsTree(Authentication authentication) {
        return userService.getFourLevelsReferralTree(authentication.getName());
    }

    // MODIFY HERE A LOT
    @GetMapping("/by-token")
    public GetFullDto getContentUserDtoByTgId(Authentication authentication) {
        UserCredentials model = userService.findByLoginModel(authentication.getName());
        List<GetInvestModel> list = model.getInvestModels().stream().map(investModelMapper::toGetDto).toList();

        GetFullDto fullDtoByLogin = userService.findFullDtoByLogin(authentication.getName());
        fullDtoByLogin.setTransactions(allTransactionService.getAllTypesOfTransactions(authentication.getName()));
        fullDtoByLogin.setInvestModels(list);
        return fullDtoByLogin;
    }

    @GetMapping("/top")
    public List<GetTopUserDto> getContentUserDtoByTgId() {
        return userService.getTopTen();
    }
}
