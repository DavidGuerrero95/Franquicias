package co.com.nequi.model.franchise.query;

public record FranchiseTreeQuery(
        int pageSize,
        int pageNumber,
        String branchName
) {
}