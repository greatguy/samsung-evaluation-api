package com.samsung.samsungevaluationapi.service;

import com.samsung.samsungevaluationapi.dto.CurrencyDto;
import com.samsung.samsungevaluationapi.dto.DocsDto;
import com.samsung.samsungevaluationapi.dto.EvaluationDto;
import com.samsung.samsungevaluationapi.dto.QuotationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.constant.ConstantDescs.NULL;
import static javax.swing.text.SimpleAttributeSet.EMPTY;

@Service
public class CurrencyService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${spring.url}")
    String url;


    public List<CurrencyDto> getCurrencyList() {

        String urlCurrency = this.url + "currency";

        ResponseEntity<List<CurrencyDto>> responseEntity = restTemplate.exchange(
                urlCurrency,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CurrencyDto>>() {
                }
        );

        return responseEntity.getBody();
    }

    public List<DocsDto> getDocsList() {

        String urlDocs = this.url + "docs";

        ResponseEntity<List<DocsDto>> responseEntity = restTemplate.exchange(
                urlDocs,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DocsDto>>() {
                }
        );

        return responseEntity.getBody();
    }

    public List<QuotationDto> getQuotationList() {

        String urlQuotation = this.url + "quotation";

        ResponseEntity<List<QuotationDto>> responseEntity = restTemplate.exchange(
                urlQuotation,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<QuotationDto>>() {
                }
        );

        return responseEntity.getBody();
    }

    public List<EvaluationDto> getEvaluationList(String documentNumber, String currencyCode, LocalDate startDate, LocalDate endDate) {

        List<CurrencyDto> listCurrency = this.getCurrencyList();
        List<DocsDto> listDocs = this.getDocsList();
        List<QuotationDto> listQuotation = this.getQuotationList();
        List<EvaluationDto> listEvaluation = new ArrayList<>();

        // Filtrando documentos

        if (!(documentNumber==null) && !listDocs.isEmpty()) listDocs = listDocs.stream().filter(doc -> doc.getDocumentNumber().equals(documentNumber)).collect(Collectors.toList());
        if (!(currencyCode==null) && !listDocs.isEmpty()) listDocs = listDocs.stream().filter(doc -> doc.getCurrencyCode().equals(currencyCode)).collect(Collectors.toList());
        if (!(startDate==null) && !(endDate==null) && !listDocs.isEmpty()) listDocs = listDocs.stream().filter(doc -> ((doc.getDocumentDate().isAfter(startDate.minusDays(1)) &&
                        doc.getDocumentDate().isBefore(endDate.plusDays(1))))).collect(Collectors.toList());

        if (listDocs.isEmpty()) return null;

        // Aplicando Cotações

        LocalDate minQuotationDate = listQuotation.stream().map(QuotationDto::getDataHoraCotacao).min(LocalDate::compareTo).get();
        LocalDate maxQuotationDate = listQuotation.stream().map(QuotationDto::getDataHoraCotacao).max(LocalDate::compareTo).get();

        for (DocsDto doc : listDocs) {

            CurrencyDto currency = listCurrency.stream().filter((cur) -> cur.getCurrencyCode().equals(doc.getCurrencyCode())).findAny().get();
            LocalDate dateQuotation = LocalDate.now();

            if (doc.getDocumentDate().isBefore(minQuotationDate)) return null;
            if (doc.getDocumentDate().isAfter(maxQuotationDate))  dateQuotation = maxQuotationDate; else dateQuotation = doc.getDocumentDate();

            BigDecimal valueQuotationUSD = doc.getCurrencyCode().equals("USD") ? new BigDecimal("1") : findQuotation(listQuotation, dateQuotation, doc.getCurrencyCode(), "USD").getCotacao();
            BigDecimal valueQuotationPEN = doc.getCurrencyCode().equals("PEN") ? new BigDecimal("1") : findQuotation(listQuotation, dateQuotation, doc.getCurrencyCode(), "PEN").getCotacao();
            BigDecimal valueQuotationBRL = doc.getCurrencyCode().equals("BRL") ? new BigDecimal("1") : findQuotation(listQuotation, dateQuotation, doc.getCurrencyCode(), "BRL").getCotacao();

            BigDecimal valueUSD = doc.getDocumentValue().multiply(valueQuotationUSD).setScale(2, RoundingMode.HALF_DOWN) ;
            BigDecimal valuePEN = doc.getDocumentValue().multiply(valueQuotationPEN).setScale(2,RoundingMode.HALF_DOWN) ;
            BigDecimal valueBRL = doc.getDocumentValue().multiply(valueQuotationBRL).setScale(2,RoundingMode.HALF_DOWN) ;

            EvaluationDto evaluation = EvaluationDto.builder()
                    .documentNumber(doc.getDocumentNumber())
                    .documentDate(doc.getDocumentDate())
                    .currencyCode(doc.getCurrencyCode())
                    .currencyDesc(currency.getCurrencyDesc())
                    .documentValue(doc.getDocumentValue())
                    .valueUSD(valueUSD)
                    .valuePEN(valuePEN)
                    .valueBRL(valueBRL)
                    .build();

            listEvaluation.add(evaluation);
        }

        return listEvaluation;

    }

    private QuotationDto findQuotation(List<QuotationDto> listQuotation, LocalDate dataQuotation, String fromCurrency, String toCurrency) {
        return listQuotation.stream().filter((quotation) -> quotation.getDataHoraCotacao().equals(dataQuotation)
                && quotation.getFromCurrencyCode().equals(fromCurrency)
                && quotation.getToCurrencyCode().equals(toCurrency)).findAny().get();
    }




}
