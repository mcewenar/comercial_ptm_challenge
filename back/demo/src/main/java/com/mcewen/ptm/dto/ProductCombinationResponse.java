package com.mcewen.ptm.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductCombinationResponse(
        List<String> productNames,
        BigDecimal total) {

}
