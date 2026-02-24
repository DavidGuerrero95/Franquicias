package co.com.nequi.commons.exception.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessExceptionEnum {
    REQUEST_BODY("MSB001", "Request body error", "The request body is invalid or malformed."),

    FRANCHISE_ALREADY_EXISTS("MSB010", "Franchise already exists", "A franchise with the same name already exists."),
    FRANCHISE_NOT_FOUND("MSB011", "Franchise not found", "The franchise was not found."),

    BRANCH_ALREADY_EXISTS("MSB020", "Branch already exists", "A branch with the same name already exists in the franchise."),
    BRANCH_NOT_FOUND("MSB021", "Branch not found", "The branch was not found."),

    PRODUCT_ALREADY_EXISTS("MSB030", "Product already exists", "A product with the same name already exists in the branch."),
    PRODUCT_NOT_FOUND("MSB031", "Product not found", "The product was not found."),

    STOCK_NEGATIVE("MSB032", "Invalid stock", "Stock cannot be negative.");

    private final String code;
    private final String description;
    private final String message;
}