package com.aiykr.iquais.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {
    private String id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String type;
    String status;
    String grade;
    String dob;
    String institutionName;
    String location;
    String guardianFirstName;
    String guardianLastName;
    String guardianEmail;
    String guardianPhoneNumber;
}
