package com.mcewen.ptm.dto;

import java.math.BigDecimal;

public record InventoryMaxResponse(
        Long id,
        String name,
        BigDecimal inventoryValue
) {}
