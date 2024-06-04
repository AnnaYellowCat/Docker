package ru.gudkova.mongoproject.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sales")
public class Sale {
    @Id
    private String id;

    private String clientName;

    @Indexed(unique = true)
    private String date;

    private Map<String, Integer> productTitlesByQuantity = new HashMap<>();

    @Override
    public String toString() {
        return clientName + " " + date+ " " + productTitlesByQuantity;
    }
}
