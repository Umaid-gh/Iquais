package com.aiykr.iquais.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@TypeAlias("user")
@Data
public class UserDAO {
    @Id
    private ObjectId id;
    String firstName;
    String lastName;
    @Indexed
    String email;
    String phoneNumber;
    String type;
    String status = "active";
    String grade;
    String dob;
    String institutionName;
    String location;
    String guardianFirstName;
    String guardianLastName;
    @Indexed
    String guardianEmail;
    String guardianPhoneNumber;
}
