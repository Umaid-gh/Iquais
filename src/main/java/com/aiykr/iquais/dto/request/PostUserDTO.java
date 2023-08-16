package com.aiykr.iquais.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Data Transfer Object (DTO) class for creating a new user.
 */
@ApiModel(description = "DTO for creating a new user")
@Data
public class PostUserDTO {

    /**
     * The first name of the user.
     */
    String firstName;

    /**
     * The last name of the user.
     */
    String lastName;

    /**
     * The email address of the user.
     */
    String email;

    /**
     * The phone number of the user.
     */
    String phoneNumber;

    /**
     * The grade of the user.
     */
    String grade;

    /**
     * The date of birth of the user.
     */
    String dob;

    /**
     * The name of the institution the user belongs to.
     */
    String institutionName;

    /**
     * The location of the user.
     */
    String location;

    /**
     * The first name of the guardian associated with the user.
     */
    String guardianFirstName;

    /**
     * The last name of the guardian associated with the user.
     */
    String guardianLastName;

    /**
     * The email address of the guardian associated with the user.
     */
    String guardianEmail;

    /**
     * The phone number of the guardian associated with the user.
     */
    String guardianPhoneNumber;
}

