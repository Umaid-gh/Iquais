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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Service for managing user-related operations, including creating students and guardians.
 */
@Service
public class UserServiceImpl implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

//    private static final String ALLOWED_CHARACTERS = "[A-Za-z0-9!@#$%^&*()_+]";
    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";

    String randomPassword = null;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
    // define levels of log
    @Transactional(rollbackFor = {IquaisException.class, EmailSendingException.class, Exception.class})
    public Response<UserResponseDTO> createUser(PostUserDTO postUserDTO) throws IquaisException {
        Response<UserResponseDTO> response = new Response<>();
        try {
            UserDAO studentDAO = createStudent(postUserDTO);
            UserDAO guardianDAO = createGuardian(postUserDTO);
            log.info("studentDAO : {}", studentDAO);
            log.info("guardianDAO : {}", guardianDAO);

            sendEmails(postUserDTO.getEmail(), postUserDTO.getGuardianEmail(),randomPassword);

            UserResponseDTO userResponseDTO = modelMapper.map(postUserDTO, UserResponseDTO.class);
            response.setMeta(Meta.builder().statusCode(HttpStatus.CREATED.value()).message("Student Created Successfully").build());
            response.setData(userResponseDTO);
            return response;
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
                log.info("Student Email {} already present in our DB", studentUserOpt.get().getEmail());
                throw new IquaisException(HttpStatus.CONFLICT, ErrorCodes.IQ002, "Student Email already present in our DB");
            }

            // Generate a random strong password
            randomPassword = generateRandomPassword();

            // Encode the password
            String encodedPassword = passwordEncoder.encode(randomPassword);

            UserDAO studentDAO = modelMapper.map(postUserDTO, UserDAO.class);
            studentDAO.setType(UserType.STUDENT.name());
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
     * Creates a new guardian user if not already present based on the provided information.
     *
     * @param postUserDTO The data required to create the guardian.
     * @return The UserDAO representing the newly created guardian, or null if the guardian already exists.
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
            emailService.sendEmail(studentEmail, guardianEmail,  "Student Account Created", getContent(randomPassword));
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
        Response<UserResponseDTO> response = new Response<>();
        UserResponseDTO userResponseDTO = null;
        Optional<UserDAO> optionalUserDAO = userRepository.findById(new ObjectId(id));
        if (optionalUserDAO.isPresent()) {
            UserDAO userDAO = optionalUserDAO.get();
            userResponseDTO = modelMapper.map(userDAO, UserResponseDTO.class);
            response.setData(userResponseDTO);
            log.info("User {} present in our DB", userResponseDTO.getFirstName());
            response.setMeta(Meta.builder().statusCode(HttpStatus.FOUND.value()).message("User Retrieved Successfully").build());
        } else {
            log.info("User is not present in our DB");
            response.setMeta(Meta.builder().statusCode(HttpStatus.NOT_FOUND.value()).message("User Not Found in DB").build());
        }
        log.info("User Detail: {}", userResponseDTO);
        response.setData(userResponseDTO);
        return response;
    }

    /**
     * Retrieves a list of all users from the database.
     *
     * @return A response containing a list of user information.
     */
    public Response<List<UserResponseDTO>> getAllUsers() {
        Response<List<UserResponseDTO>> response = new Response<>();
        List<UserDAO> userDAOS = userRepository.findAll();
        List<UserResponseDTO> usersList = new ArrayList<>();
        userDAOS.forEach(userDAO -> usersList.add(this.modelMapper.map(userDAO, UserResponseDTO.class)));
        if (usersList.isEmpty())
            response.setMeta(Meta.builder().message("DataBase is Empty").statusCode(HttpStatus.NOT_FOUND.value()).build());
        else
            response.setMeta(Meta.builder().message("Retrieval Successful").statusCode(HttpStatus.OK.value()).build());
        log.info("Total Users: {}",usersList.size());
        log.info("Users Details: {}", usersList);
        response.setData(usersList);
        return response;
    }
}
