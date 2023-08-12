package com.aiykr.iquais.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "users")
@TypeAlias("student")
@EqualsAndHashCode(callSuper=false)
public class Student extends User {
    @Field
    String grade;
    @Field
    Date dob;
    @Field
    String institutionName;
    @Field
    String location;
}
