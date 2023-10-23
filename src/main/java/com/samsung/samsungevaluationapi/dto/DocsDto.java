package com.samsung.samsungevaluationapi.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocsDto {

    private Integer documentId;
    private String documentNumber;
    private String notaFiscal;
    private LocalDate documentDate;
    private BigDecimal documentValue;
    private String currencyCode;

}
