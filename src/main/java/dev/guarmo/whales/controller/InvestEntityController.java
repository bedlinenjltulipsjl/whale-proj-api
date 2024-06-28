package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.investmodel.dto.GetInvestModel;
import dev.guarmo.whales.model.investmodel.dto.PostInvestModel;
import dev.guarmo.whales.service.InvestModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tables")
public class InvestEntityController {
    private final InvestModelService investModelService;
//    @GetMapping
//    public List<GetInvestModel> buyTable(Authentication authentication) {
//        return investModelService.g(authentication.getName());
//    }


    @PatchMapping
    public GetInvestModel buyTable(@RequestBody PostInvestModel investModel, Authentication authentication) {
        return investModelService.addInvestModel(investModel, authentication.getName());
    }
}
