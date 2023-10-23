package com.samsung.samsungevaluationapi.controller;


import com.samsung.samsungevaluationapi.dto.CurrencyDto;
import com.samsung.samsungevaluationapi.dto.DocsDto;
import com.samsung.samsungevaluationapi.dto.EvaluationDto;
import com.samsung.samsungevaluationapi.dto.QuotationDto;
import com.samsung.samsungevaluationapi.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cotacao")
public class CurrencyController {


    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/currency")
    public List<CurrencyDto> getCurrency() {
        return currencyService.getCurrencyList();
    }

    @GetMapping("/docs")
    public List<DocsDto> getDocs() {
        return currencyService.getDocsList();
    }

    @GetMapping("/quotation")
    public List<QuotationDto> getQuotation() {
        return currencyService.getQuotationList();
    }

    @GetMapping("/evaluation")
    public List<EvaluationDto> getEvaluation(@RequestParam(name="documentNumber", required=false) String documentNumber, @RequestParam(name="currencyCode", required=false) String currencyCode,
                                             @RequestParam(name="startDate", required=false) LocalDate startDate, @RequestParam(name="endDate", required=false) LocalDate endDate ) {

        return currencyService.getEvaluationList(documentNumber, currencyCode, startDate, endDate);
    }

}
