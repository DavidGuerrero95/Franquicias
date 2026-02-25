package co.com.nequi.model.product.report;

public record TopStockItem(
        Long branchId,
        String branchName,
        Long productId,
        String productName,
        Integer stock
) {
}