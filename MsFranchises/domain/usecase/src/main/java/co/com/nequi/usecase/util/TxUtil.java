package co.com.nequi.usecase.util;

import co.com.nequi.dto.transaction.Transaction;

public final class TxUtil {
    private TxUtil() { }

    public static Transaction next(Transaction base, Object request) {
        return Transaction.builder()
                .headers(base.getHeaders())
                .route(base.getRoute())
                .transactionCode(base.getTransactionCode())
                .request(request)
                .build();
    }
}