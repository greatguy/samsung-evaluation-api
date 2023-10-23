package com.samsung.samsungevaluationapi.dto;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDto {

    private Integer currencyId;
    private String currencyCode;
    private String currencyDesc;

}
