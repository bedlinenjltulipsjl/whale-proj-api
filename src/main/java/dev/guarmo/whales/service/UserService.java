package dev.guarmo.whales.service;

import dev.guarmo.whales.model.transaction.deposit.mapper.DepositMapper;
import dev.guarmo.whales.model.user.RoleStatus;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.model.user.dto.*;
import dev.guarmo.whales.model.user.mapper.UserMapper;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserCredentialsRepo repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserCredentialsRepo userCredentialsRepository;
    @Value("${bot.link}")
    private String botLink;

    public GetUserCredentialsDto addUser(PostUserDto user, RoleStatus role) {
        UserCredentials model = userMapper.toModel(user);
        model.setRole(role);
        model.setPassword(passwordEncoder.encode(user.getPassword()));
        UserCredentials save = repository.save(model);

        UserCredentials upperReferral = model.getUpperReferral();
        upperReferral.getBottomReferrals().add(save);

        UserCredentials save1 = repository.save(upperReferral);
        return userMapper.toGetCredentialsDto(save);
    }

    public GetUserCredentialsDto getByCredentialsByLogin(String login) {
        UserCredentials userCredentials = repository.findByLogin(login).orElseThrow();
        return userMapper.toGetCredentialsDto(userCredentials);
    }

    public String generateRefLinkForUser(String userTelegramId) {
        return botLink + Base64.getUrlEncoder().encodeToString(userTelegramId.getBytes(StandardCharsets.UTF_8));
    }

    public GetContentWithoutHistoryUserDto findByLogin(String tgid) {
        UserCredentials userCredentials = userCredentialsRepository.findByLogin(tgid).orElseThrow();
        return userMapper.toGetWithoutHistoryDto(userCredentials);
    }

    public UserCredentials findByLoginModel(String tgid) {
        return userCredentialsRepository.findByLogin(tgid).orElseThrow();
    }

    public GetFullDto getFourLevelsReferralTree(String tgid) {
        UserCredentials userCredentials = userCredentialsRepository.findByLogin(tgid).orElseThrow();
        return userMapper.toFullGetDto(userCredentials);
    }

    public GetFullDto findFullDtoByLogin(String name) {
        return userMapper.toFullGetDto(findByLoginModel(name));
    }

    public List<GetTopUserDto> getTopTen() {
        Pageable topTen = PageRequest.of(0, 12);
        Page<UserCredentials> resultPage = userCredentialsRepository.findTopByMaxSumOfIncomes(topTen);
        List<UserCredentials> content = resultPage.getContent();
        return content
                .stream()
                .map(userMapper::toGetTopUserDto)
                .toList();
    }
}
