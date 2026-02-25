package co.com.nequi.model.franchise.report;

import java.util.List;

public record FranchiseTreePage(
        long totalRecords,
        int pageSize,
        int pageNumber,
        int totalPageNumber,
        boolean lastPageIndicator,
        boolean additionalRecords,
        List<FranchiseTreeItem> data
) {
}