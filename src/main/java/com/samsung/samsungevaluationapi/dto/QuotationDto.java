package com.samsung.samsungevaluationapi.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class QuotationDto {

    private String fromCurrencyCode;
    private String toCurrencyCode;
    private BigDecimal cotacao;
    private LocalDate dataHoraCotacao;

}
