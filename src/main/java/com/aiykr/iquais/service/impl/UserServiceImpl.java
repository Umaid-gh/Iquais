package com.aiykr.iquais.service.impl;

import com.aiykr.iquais.dto.request.PostUserDTO;
import com.aiykr.iquais.dto.response.ErrorCodes;
import com.aiykr.iquais.dto.response.Meta;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import com.aiykr.iquais.entity.UserDAO;
import com.aiykr.iquais.entity.UserType;
import com.aiykr.iquais.exception.EmailSendingException;
import com.aiykr.iquais.exception.IquaisException;
import com.aiykr.iquais.repository.IUserRepository;
import com.aiykr.iquais.service.IEmailService;
import com.aiykr.iquais.service.IUserService;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Service for managing user-related operations, including creating students and guardians.
 */
@Service
public class UserServiceImpl implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String ACTIVE_STATUS = "active";

    // Generate a random strong password
    private String generatedPassword = generateRandomPassword();

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Creates a new user based on the provided information.
     *
     * @param postUserDTO The data required to create the user.
     * @return A response containing user details.
     * @throws IquaisException If an error occurs during user creation.
     */
    @Transactional(rollbackFor = {IquaisException.class, EmailSendingException.class, Exception.class})
    public Response<UserResponseDTO> createUser(PostUserDTO postUserDTO) throws IquaisException {
        try {
            UserDAO studentDAO = createStudent(postUserDTO);
            UserDAO guardianDAO = createGuardian(postUserDTO);
            log.info("studentDAO : {}", studentDAO);
            log.info("guardianDAO : {}", guardianDAO);

            sendEmails(postUserDTO.getEmail(), postUserDTO.getGuardianEmail(), generatedPassword);

            UserResponseDTO userResponseDTO = modelMapper.map(postUserDTO, UserResponseDTO.class);
            return Response.<UserResponseDTO>builder()
                    .data(userResponseDTO)
                    .meta(Meta.builder().statusCode(HttpStatus.CREATED.value()).message("Student Created Successfully").build())
                    .build();
        } catch (IquaisException iqEx) {
            throw iqEx;
        } catch (Exception ex) {
            log.error("An exception occurred: {}", ex.getMessage(), ex);
            throw new IquaisException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.IQ005, "Exception while storing in DB and sending Email");
        }
    }

    /**
     * Generates a random strong password.
     *
     * @return A randomly generated password.
     */
    public static String generateRandomPassword() {
        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            password.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return password.toString();
    }

    /**
     * Creates a new student user based on the provided information.
     *
     * @param postUserDTO The data required to create the student.
     * @return The UserDAO representing the newly created student.
     * @throws IquaisException If an error occurs during student creation.
     */
    private UserDAO createStudent(PostUserDTO postUserDTO) throws IquaisException {
        // Check if student already exists
        try {
            Optional<UserDAO> studentUserOpt = userRepository.findByEmail(postUserDTO.getEmail());
            if (studentUserOpt.isPresent()) {
                UserDAO studentUser = studentUserOpt.get();
                if (ACTIVE_STATUS.equals(studentUser.getStatus())) {
                    log.info("Student Email {} is already present and active in our DB", studentUser.getEmail());
                    throw new IquaisException(HttpStatus.CONFLICT, ErrorCodes.IQ002, "Student Email already present and active in our DB");
                } else {
                    // Set the status to active again if it was disabled
                    studentUser.setStatus(ACTIVE_STATUS);
                    userRepository.save(studentUser);
                    log.info("Student Email {} was previously disabled, set to active again", studentUser.getEmail());
                }
            }

            // Encode the password
            String encodedPassword = passwordEncoder.encode(generatedPassword);

            UserDAO studentDAO = modelMapper.map(postUserDTO, UserDAO.class);
            studentDAO.setType(UserType.STUDENT.name());

            // Check if the generated password is already in the database
            while (isPasswordInDatabase(encodedPassword)) {
                generatedPassword = generateRandomPassword();
                encodedPassword = passwordEncoder.encode(generatedPassword);
            }

            // Save the encoded password
            studentDAO.setPassword(encodedPassword);

            userRepository.save(studentDAO);
            log.info("StudentDAO saved in DB");
            return studentDAO;
        } catch (IquaisException iqEx) {
            throw iqEx;
        } catch (Exception ex) {
            log.error("An exception occurred while creating student: {}", ex.getMessage(), ex);
            throw new IquaisException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.IQ001, "Exception occurred while creating student");
        }
    }

    /**
     * Checks if a password is already present in the database.
     * This method queries the database to determine if a user with the specified encoded password exists.
     *
     * @param password The encoded password to check for in the database.
     * @return `true` if a user with the provided encoded password is found in the database, `false` otherwise.
     */
    private boolean isPasswordInDatabase(String password) {
        Optional<UserDAO> userWithSamePassword = userRepository.findByPassword(password);
        return userWithSamePassword.isPresent();
    }

    /**
     * Creates a new guardian user if not already present based on the provided information.
     *
     * @param postUserDTO The data required to create the guardian.
     * @return The UserDAO representing the newly created guardian, or null if the guardian already exists.
     * @throws IquaisException If an error occurs during guardian creation.
     */
    private UserDAO createGuardian(PostUserDTO postUserDTO) throws IquaisException {
        // Check if guardian already exists
        try {
            Optional<UserDAO> guardianUserOpt = userRepository.findByEmail(postUserDTO.getGuardianEmail());
            if (guardianUserOpt.isEmpty()) {
                UserDAO guardianDAO = new UserDAO();
                guardianDAO.setFirstName(postUserDTO.getGuardianFirstName());
                guardianDAO.setLastName(postUserDTO.getGuardianLastName());
                guardianDAO.setEmail(postUserDTO.getGuardianEmail());
                guardianDAO.setType(UserType.GUARDIAN.name());
                userRepository.save(guardianDAO);
                log.info("GuardianDAO saved in DB");
                return guardianDAO;
            }
            log.info("Guardian Email {} already present in our DB", postUserDTO.getGuardianEmail());
            return null;
        } catch (Exception ex) {
            log.error("An exception occurred while creating guardian: {}", ex.getMessage(), ex);
            throw new IquaisException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.IQ001, "Exception occurred while creating guardian");
        }
    }

    /**
     * Sends welcome emails to the student and guardian after account creation.
     *
     * @param studentEmail   The email address of the student.
     * @param guardianEmail  The email address of the guardian.
     * @param randomPassword The randomly generated password.
     * @throws IquaisException If an error occurs while sending the email.
     */
    private void sendEmails(String studentEmail, String guardianEmail, String randomPassword) throws IquaisException {
        try {
            emailService.sendEmail(studentEmail, guardianEmail, "Student Account Created", getContent(randomPassword));
            log.info("Email sent successfully!!");
        } catch (MessagingException msgEx) {
            // Handle exception properly in production code
            log.error("Error sending email: {}", msgEx.getMessage(), msgEx);
            // You can rethrow the exception if necessary
            throw new EmailSendingException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.IQ003, "Error while sending Email");
        }
    }

    /**
     * Generates the content for a welcome email sent to a newly created student, including their randomly generated password.
     *
     * @param randomPassword The randomly generated password for the student.
     * @return The content of the welcome email.
     */
    private String getContent(String randomPassword) {
        return "Welcome to our platform! \nStudent Account has been created and Guardian Account has been Linked.\nPassword for student: " + randomPassword;
    }

    /**
     * Retrieves a student's information based on the provided ID.
     *
     * @param id The unique ID of the student to retrieve.
     * @return A response containing the retrieved student's information.
     */
    public Response<UserResponseDTO> getStudentById(String id) {
        UserResponseDTO userResponseDTO;
        Optional<UserDAO> optionalUserDAO = userRepository.findById(new ObjectId(id));
        if (optionalUserDAO.isPresent()) {
            UserDAO userDAO = optionalUserDAO.get();
            userResponseDTO = modelMapper.map(userDAO, UserResponseDTO.class);
            log.info("User {} present in our DB", userResponseDTO.getFirstName());
            return Response.<UserResponseDTO>builder()
                    .data(userResponseDTO)
                    .meta(Meta.builder().statusCode(HttpStatus.FOUND.value()).message("User Retrieved Successfully").build())
                    .build();
        } else {
            return Response.<UserResponseDTO>builder()
                    .data(null)
                    .meta(Meta.builder().statusCode(HttpStatus.NOT_FOUND.value()).message("User Not found in DB").build())
                    .build();
        }
    }

    /**
     * Retrieves a list of users based on pagination and sorting criteria.
     *
     * @param page      The page number for pagination (0-based index).
     * @param size      The number of items per page.
     * @param sortBy    The field by which to sort the results.
     * @param sortOrder The sorting order, either "asc" (ascending) or "desc" (descending).
     * @return A ResponseEntity containing the response with user data and appropriate metadata.
     * @throws IquaisException If an error occurs during searching data.
     */
    public Response<List<UserResponseDTO>> getAllUsers(int page, int size, String sortBy, String sortOrder) throws IquaisException {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<UserDAO> userPage = userRepository.findAll(pageable);

            if (!userPage.hasContent()) {
                throw new IquaisException(HttpStatus.NOT_FOUND, ErrorCodes.IQ006, "No data found for the search string");
            }
            List<UserResponseDTO> usersList = userPage.getContent().stream()
                    .map(userDAO -> modelMapper.map(userDAO, UserResponseDTO.class))
                    .collect(Collectors.toList());

            log.info("Total Users: {}", usersList.size());
            log.info("Users Details: {}", usersList);
            return Response.<List<UserResponseDTO>>builder()
                    .data(usersList)
                    .meta(Meta.builder().message("Retrieval Successful").statusCode(HttpStatus.OK.value()).build())
                    .build();
        } catch (DataAccessException ex) {
            throw new IquaisException(HttpStatus.NOT_FOUND, ErrorCodes.IQ006, "No data found for the search string");
        } catch (Exception ex) {
            throw new IquaisException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.IQ007, "Internal Error");
        }
    }

    /**
     * Soft Deletes a user by email address.
     *
     * @param email The email address of the user to delete.
     * @return A response containing user information.
     * @throws IquaisException If an error occurs during the deletion process.
     */
    public Response<UserResponseDTO> deleteStudentByEmail(String email) throws IquaisException {
        UserResponseDTO userResponseDTO;
        Optional<UserDAO> optionalUserDAO = userRepository.findByEmail(email);
        if (optionalUserDAO.isPresent()) {
            UserDAO userDAO = optionalUserDAO.get();
            if (ACTIVE_STATUS.equals(userDAO.getStatus())) {
                userDAO.setStatus("disabled"); // Set the status to disabled
                userRepository.save(userDAO); // Persist the status change
            } else {
                throw new IquaisException(HttpStatus.FOUND, ErrorCodes.IQ001, "User is already in a disabled status");
            }

            userResponseDTO = modelMapper.map(userDAO, UserResponseDTO.class);
            log.info("User {} Soft Deleted in our DB", userResponseDTO.getEmail());
            return Response.<UserResponseDTO>builder()
                    .data(userResponseDTO)
                    .meta(Meta.builder().statusCode(HttpStatus.OK.value()).message("User Soft Deleted Successfully").build())
                    .build();
        } else {
            return Response.<UserResponseDTO>builder()
                    .data(null)
                    .meta(Meta.builder().statusCode(HttpStatus.NOT_FOUND.value()).message("User Not found in DB to Delete").build())
                    .build();
        }
    }
}
