package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.dto.GetInvestModel;
import dev.guarmo.whales.model.investmodel.dto.PostInvestModel;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.service.InvestModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/tables")
@CrossOrigin(allowedHeaders = "*")
public class InvestEntityController {
    private final InvestModelService investModelService;

    @PatchMapping
    public InvestModel buyTable(
            @RequestBody PostInvestModel investModel,
            Authentication authentication) {
        return investModelService.buyInvestModel(investModel, authentication.getName());
    }
}
