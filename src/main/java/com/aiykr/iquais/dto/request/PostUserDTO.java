package com.aiykr.iquais.dto.request;

import lombok.Data;

@Data
public class PostUserDTO {
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String grade;
    String dob;
    String institutionName;
    String location;
    String guardianFirstName;
    String guardianLastName;
    String guardianEmail;
    String guardianPhoneNumber;
}
