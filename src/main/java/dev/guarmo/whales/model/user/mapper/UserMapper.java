package dev.guarmo.whales.model.user.mapper;

import dev.guarmo.whales.config.MapperConfig;
import dev.guarmo.whales.model.transaction.deposit.Deposit;
import dev.guarmo.whales.model.transaction.income.Income;
import dev.guarmo.whales.model.transaction.purchase.Purchase;
import dev.guarmo.whales.model.transaction.withdraw.MoneyWithdraw;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.model.user.dto.*;
import org.mapstruct.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class, uses = {UserMapperHelper.class})
public interface UserMapper {
    @Mapping(target = "upperReferralLogin", source = "upperReferral", qualifiedByName = "mapUpperReferralToLogin")
    GetUserCredentialsDto toGetCredentialsDto(UserCredentials user);

    @Mapping(target = "upperReferral", source = "referralLogin", qualifiedByName = "mapUpperReferralByLogin")
    UserCredentials toModel(PostUserDto postUserDto);

    GetContentWithoutHistoryUserDto toGetWithoutHistoryDto(UserCredentials user);

    GetContentUserDto toGetDto(UserCredentials user);
    @AfterMapping
    default void initArraysWithIdsAfterMapping(@MappingTarget GetContentUserDto dto,
                                      UserCredentials model) {
        List<Long> depositIds = model.getDeposits()
                .stream()
                .map(Deposit::getId)
                .toList();
        dto.setDepositIds(depositIds);

        List<Long> withdrawIds = model.getWithdraws()
                .stream()
                .map(MoneyWithdraw::getId)
                .toList();
        dto.setWithdrawIds(withdrawIds);

        List<Long> purchaseIds = model.getPurchases()
                .stream()
                .map(Purchase::getId)
                .toList();
        dto.setPurchaseIds(purchaseIds);

        List<Long> incomeIds = model.getIncomes()
                .stream()
                .map(Income::getId)
                .toList();
        dto.setIncomeIds(incomeIds);
    }

    @Mapping(target = "bottomReferrals", ignore = true)
    @Mapping(target = "treeLevel", ignore = true)
//    @Mapping(target = "reflink", source = "login", qualifiedByName = "getReflink")
//    @Mapping(target = "transactions", source = "login", qualifiedByName = "mapTransactionsToUser")
    GetFullDto toFullGetDto(UserCredentials user);

//    @AfterMapping
//    default void mapReferralsAfterMapping(@MappingTarget GetFullDto dto, UserCredentials model) {
//        mapReferrals(dto, model, 0);
//    }
//
//    default void mapReferrals(GetFullDto dto, UserCredentials model, int level) {
//        dto.setTreeLevel(level);
//
//        // Sets recursion basis (how deep fetch referrals)
//        if (level >= 3) {
//            dto.setBottomReferrals(Collections.emptyList());
//            return;
//        }
//
//        List<GetFullDto> referrals = model.getBottomReferrals()
//                .stream()
//                .map(referral -> {
//                    GetFullDto referralDto = toFullGetDto(referral);
//                    mapReferrals(referralDto, referral, level + 1);
//                    return referralDto;
//                })
//                .collect(Collectors.toList());
//        dto.setBottomReferrals(referrals);
//    }

    @Mapping(target = "investedAmount", source = "login", qualifiedByName = "getBottomReferralsAmount")
    @Mapping(target = "earnedAmount", source = "login", qualifiedByName = "getEarnedAmount")
    GetTopUserDto toGetTopUserDto(UserCredentials user);
}
