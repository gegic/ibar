package com.sbnz.ibar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDto {
    private UUID id;
    private String name;
    private double price;
    private Set<String> categoryNames;
    private Set<UUID> categoryIds;
    private UUID rankId;
    private String rankName;
    private String description;
    private Long dayDuration;
}
