package co.com.nequi.model.franchise.report;

import java.util.List;

public record BranchTreeItem(
        Long id,
        String name,
        List<ProductTreeItem> products
) {
}