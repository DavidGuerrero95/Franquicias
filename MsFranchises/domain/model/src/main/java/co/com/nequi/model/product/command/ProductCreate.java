package co.com.nequi.model.product.command;

public record ProductCreate(Long branchId, String name, Integer stock) {
}