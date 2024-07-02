package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.transaction.withdraw.dto.GetWithdrawDto;
import dev.guarmo.whales.model.transaction.withdraw.dto.PostWithdrawDto;
import dev.guarmo.whales.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping("/withdraws")
public class WithdrawController {
    private final WithdrawService withdrawService;

    @PostMapping
    public GetWithdrawDto addWithdrawRequest(@RequestBody PostWithdrawDto postWithdrawDto, Authentication authentication) {
        return withdrawService.addWithdrawRequest(postWithdrawDto, authentication.getName());
    }

    @GetMapping
    public List<GetWithdrawDto> getWithdrawsOfUser(Authentication authentication) {
        return withdrawService.getWithdrawsOfUserByLogin(authentication.getName());
    }
}
