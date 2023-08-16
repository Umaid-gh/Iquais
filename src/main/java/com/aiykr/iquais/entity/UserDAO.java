package com.aiykr.iquais.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Data Access Object (DAO) class representing user information in the MongoDB collection.
 */
@Document(collection = "users")
@TypeAlias("user")
@Data
public class UserDAO {

    /**
     * The unique ID of the user.
     */
    @Id
    private ObjectId id;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The email address of the user.
     */
    @Indexed
    private String email;

    /**
     * The phone number of the user.
     */
    private String phoneNumber;

    /**
     * The type of the user (e.g., student, guardian).
     */
    private String type;

    /**
     * The status of the user.
     */
    private String status = "active";

    /**
     * The grade of the user.
     */
    private String grade;

    /**
     * The date of birth of the user.
     */
    private String dob;

    /**
     * The name of the institution the user belongs to.
     */
    private String institutionName;

    /**
     * The location of the user.
     */
    private String location;

    /**
     * The first name of the guardian associated with the user.
     */
    private String guardianFirstName;

    /**
     * The last name of the guardian associated with the user.
     */
    private String guardianLastName;

    /**
     * The email address of the guardian associated with the user.
     */
    @Indexed
    private String guardianEmail;

    /**
     * The phone number of the guardian associated with the user.
     */
    private String guardianPhoneNumber;
}
