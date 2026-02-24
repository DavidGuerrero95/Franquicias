package co.com.nequi.r2dbc.franchise;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("franchise")
public record FranchiseData(
        @Id Long id,
        String name,
        Instant createdAt,
        Instant updatedAt
) { }