package com.trademax.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Indexed(unique = true)
    private String email;

    @Pattern(
        regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}",
        message = "Invalid PAN format. Example: ABCDE1234F"
    )
    @NotBlank(message = "PAN is required")
    @Indexed(unique = true)
    private String pan;

    private Wallet wallet;
    private List<PortfolioItem> portfolio;
}
