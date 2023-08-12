package com.aiykr.iquais.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
@TypeAlias("user")
@Data
public class User {
    @Id
    private Long id;
    @Field
    String name;
    @Field
    String email;
    @Field
    String phoneNumber;
    String type;

    //Nikhil
    String password;
    String status = "active";
}
