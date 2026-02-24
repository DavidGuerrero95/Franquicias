package co.com.nequi.r2dbc.branch;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("branch")
public record BranchData(
        @Id Long id,
        @Column("franchise_id") Long franchiseId,
        String name,
        Instant createdAt,
        Instant updatedAt
) { }