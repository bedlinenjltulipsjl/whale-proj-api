package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.transaction.income.dto.GetIncomeDto;
import dev.guarmo.whales.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @GetMapping("/{tgid}")
    public List<GetIncomeDto> getAllIncomesFromUser(@PathVariable String tgid) {
        return incomeService.findAllIncomesByLogin(tgid);
    }
}
