package dev.guarmo.whales.service;

import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.model.transaction.withdraw.MoneyWithdraw;
import dev.guarmo.whales.model.transaction.withdraw.WithdrawStatus;
import dev.guarmo.whales.model.transaction.withdraw.dto.GetWithdrawDto;
import dev.guarmo.whales.model.transaction.withdraw.dto.PostWithdrawDto;
import dev.guarmo.whales.model.transaction.withdraw.mapper.WithdrawMapper;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import dev.guarmo.whales.repository.WithdrawRepo;
import dev.guarmo.whales.teleg.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final WithdrawRepo withdrawRepo;
    private final UserCredentialsRepo userCredentialsRepo;
    private final WithdrawMapper withdrawMapper;
    private final TelegramService telegramService;

    public GetWithdrawDto addWithdrawRequest(PostWithdrawDto postWithdrawDto, String login) {
        MoneyWithdraw model = withdrawMapper.toModel(postWithdrawDto);
        model.setWithdrawStatus(WithdrawStatus.PENDING);
        MoneyWithdraw saved = withdrawRepo.save(model);

        UserCredentials userCredentials = userCredentialsRepo.findByLogin(login).orElseThrow();
        userCredentials.getWithdraws().add(model);
        userCredentialsRepo.save(userCredentials);

        GetWithdrawDto withdrawGetDto = withdrawMapper.toGetDto(saved);

        telegramService.sendNotificationAboutWithdraw(userCredentials, withdrawGetDto);
        return withdrawGetDto;
    }

    public List<GetWithdrawDto> getWithdrawsOfUserByLogin(String tgid) {
        UserCredentials userCredentials = userCredentialsRepo.findByLogin(tgid).orElseThrow();
        return userCredentials.getWithdraws().stream().map(withdrawMapper::toGetDto).toList();
    }
}
