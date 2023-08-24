package com.aiykr.iquais.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

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
    @Email(message = "Please provide a valid email address")
    @Indexed(unique = true)
    private String email;

    /**
     * The password of the user generated randomly.
     */
    @JsonIgnore
    private String password;

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
    // Use LocalDate for date of birth
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-[0-9]{4}$", message = "Date of birth should be in dd-mm-yyyy format")
    private LocalDate dob;

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
    @Email(message = "Please provide a valid guardian email address")
    private String guardianEmail;

    /**
     * The phone number of the guardian associated with the user.
     */
    private String guardianPhoneNumber;
}
