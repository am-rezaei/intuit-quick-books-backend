package com.intuit.developer.helloworld.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCredential {
    String accessToken;

    String realmId;
}
