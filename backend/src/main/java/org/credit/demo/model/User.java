package org.credit.demo.model;

import lombok.Data;

@Data
public class User {
    private String id;
    private String email;
    private String passwordHash;
    private boolean verified;
}
