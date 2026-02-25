package co.com.nequi.model.franchise.report;

import java.util.List;

public record FranchiseTreeItem(
        Long id,
        String name,
        List<BranchTreeItem> branches
) {
}