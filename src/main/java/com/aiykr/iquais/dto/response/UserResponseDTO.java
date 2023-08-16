package com.aiykr.iquais.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Data Transfer Object (DTO) class representing user information in API responses.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {

    /**
     * The unique ID of the user.
     */
    private String id;

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
    private String status;

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
    private String guardianEmail;

    /**
     * The phone number of the guardian associated with the user.
     */
    private String guardianPhoneNumber;
}
