package co.com.nequi.r2dbc.product;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("product")
public record ProductData(
        @Id Long id,
        @Column("branch_id") Long branchId,
        String name,
        Integer stock,
        Instant createdAt,
        Instant updatedAt
) { }