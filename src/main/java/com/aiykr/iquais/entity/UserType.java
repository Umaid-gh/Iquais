package com.aiykr.iquais.entity;

import io.swagger.annotations.ApiModel;

/**
 * Enumeration representing the types of users.
 */
@ApiModel(description = "Enumeration for User Types")
public enum UserType {

    /**
     * Represents a student user.
     */
    STUDENT,

    /**
     * Represents a guardian user.
     */
    GUARDIAN,

    /**
     * Represents an admin user.
     */
    ADMIN
}

