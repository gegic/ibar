package com.sbnz.ibar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ContentFileDto {
    private UUID path;
    private long quantity;
}
