package com.aiykr.iquais.dto.response;

/**
 * This class contains constant error codes used throughout the application.
 * Error codes are used for identifying specific error scenarios in a standardized way.
 * It helps in error handling, logging, and providing meaningful error messages to users.
 */
public class ErrorCodes {
    /**
     * Error code occurred while creating student or guardian.
     */
    public static final String IQ001 = "IQ001";

    /**
     * Error code for Email already present in our DB.
     */
    public static final String IQ002 = "IQ002";

    /**
     * Error code while sending Email.
     */
    public static final String IQ003 = "IQ003";

    /**
     * Error code while storing data in DB and sending Email.
     */
    public static final String IQ005 = "IQ005";

    /**
     * Error code If User not Found in DB.
     */
    public static final String IQ006 = "IQ006";

    /**
     * Invalid Credential
     */
    public static final String IQ007 = "IQ007";

    // Can continue adding more error codes as needed for your application.

    /**
     * Private constructor to prevent the instantiation of this class.
     * This class should only be used to access error code constants.
     */
    private ErrorCodes() {
        //Utility classes, which are collections of static members, are not meant to be instantiated.
        //Even abstract utility classes, which can be extended, should not have public constructors.
        //Java adds an implicit public constructor to every class which does not define at least one explicitly.
        // Hence, at least one non-public constructor should be defined.
    }
}

