package example.cashcard;

import org.springframework.data.annotation.Id;

public record CashCard(@Id Long id, Double amount, String owner) {
    // This record class represents a cash card with an ID and an amount.
    // It is used for serialization and deserialization in JSON format.

}
