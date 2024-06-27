package dev.guarmo.whales.service;

import dev.guarmo.whales.model.transaction.withdraw.MoneyWithdraw;
import dev.guarmo.whales.model.transaction.withdraw.WithdrawStatus;
import dev.guarmo.whales.model.transaction.withdraw.dto.GetWithdrawDto;
import dev.guarmo.whales.model.transaction.withdraw.mapper.WithdrawMapper;
import dev.guarmo.whales.repository.WithdrawRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WithdrawServiceHelper {

    private final WithdrawRepo withdrawRepo;
    private final WithdrawMapper withdrawMapper;

    public GetWithdrawDto updateWithdrawStatus(String moneyWithdrawIdAsString, WithdrawStatus status) {
        Long moneyWithdrawId = Long.parseLong(moneyWithdrawIdAsString);
        MoneyWithdraw moneyWithdraw = withdrawRepo.findById(moneyWithdrawId).orElseThrow();
        moneyWithdraw.setWithdrawStatus(status);
        MoneyWithdraw saved = withdrawRepo.save(moneyWithdraw);
        return withdrawMapper.toGetDto(saved);
    }
}
