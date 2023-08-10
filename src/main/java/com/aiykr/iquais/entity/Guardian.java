package com.aiykr.iquais.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
@TypeAlias("guardian")
@EqualsAndHashCode(callSuper=false)
public class Guardian extends User {

}
