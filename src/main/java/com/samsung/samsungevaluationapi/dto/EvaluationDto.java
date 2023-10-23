package com.samsung.samsungevaluationapi.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDto {

    private String documentNumber;
    private LocalDate documentDate;
    private String currencyCode;
    private String currencyDesc;
    private BigDecimal documentValue;
    private BigDecimal valueUSD;
    private BigDecimal valuePEN;
    private BigDecimal valueBRL;

}
