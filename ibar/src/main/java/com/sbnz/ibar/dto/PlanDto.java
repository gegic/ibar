package com.sbnz.ibar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDto {
    private UUID id;

    @NotEmpty(message = "Name cannot be empty.")
    private String name;

    @NotNull(message = "Price cannot be null.")
    @Min(value = 1, message = "Price must be higher or equal to 1.")
    private double price;

    private Set<String> categoryNames;

    @NotEmpty(message = "Categories cannot be empty.")
    private Set<UUID> categoryIds;

    @NotNull(message = "Rank cannot be null.")
    private UUID rankId;

    private String rankName;

    private String description;

    @NotNull(message = "Duration in days cannot be null.")
    @Min(value = 1, message = "Duration in days must be higher or equal to 1.")
    private Long dayDuration;
}
