package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.transaction.withdraw.dto.GetWithdrawDto;
import dev.guarmo.whales.model.transaction.withdraw.dto.PostWithdrawDto;
import dev.guarmo.whales.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/withdraws")
public class WithdrawController {
    private final WithdrawService withdrawService;

    @PostMapping("/{tgid}")
    public GetWithdrawDto addWithdrawRequest(@RequestBody PostWithdrawDto postWithdrawDto, @PathVariable String tgid) {
        return withdrawService.addWithdrawRequest(postWithdrawDto, tgid);
    }

    @GetMapping("/{tgid}")
    public List<GetWithdrawDto> getWithdrawsOfUser(@PathVariable String tgid) {
        return withdrawService.getWithdrawsOfUserByLogin(tgid);
    }
}
