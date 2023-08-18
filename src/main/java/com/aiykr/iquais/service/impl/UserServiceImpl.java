package com.aiykr.iquais.service.impl;

import com.aiykr.iquais.dto.request.PostUserDTO;
import com.aiykr.iquais.dto.response.Meta;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import com.aiykr.iquais.entity.UserDAO;
import com.aiykr.iquais.entity.UserType;
import com.aiykr.iquais.exception.EmailSendingException;
import com.aiykr.iquais.exception.IquaisException;
import com.aiykr.iquais.repository.UserRepository;
import com.aiykr.iquais.service.EmailService;
import com.aiykr.iquais.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation class for managing user-related operations.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Creates a new user based on the provided information.
     *
     * @param postUserDTO The data required to create the user.
     * @return A response containing user details.
     * @throws IquaisException If an error occurs during user creation.
     */
    // define levels of log
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Response<UserResponseDTO> createUser(PostUserDTO postUserDTO) throws IquaisException {
        Response<UserResponseDTO> response = new Response<>();
        try {
            // Check if the student is already present by email address
            String studentEmail = postUserDTO.getEmail();
            log.info("Student Email: {}", studentEmail);

            Optional<UserDAO> studentUserOpt = userRepository.findByEmail(studentEmail);

            UserDAO studentDAO = null;
            if (studentUserOpt.isPresent()) {
                String email = studentUserOpt.get().getEmail();

                if (!StringUtils.isEmpty(email) && email.equalsIgnoreCase(studentEmail)) {
                    log.info("Student Email {} already present in our DB", studentEmail);
                    throw new IquaisException(HttpStatus.CONFLICT, "IQ002", "Student Email already present in our DB");
                }
            }
            // Create Student
            studentDAO = new ModelMapper().map(postUserDTO, UserDAO.class);
            studentDAO.setType(UserType.STUDENT.name());
            userRepository.save(studentDAO);
            log.info("Save New Student: {}", studentDAO);

            // Check if the guardian is already present by email address
            String guardianEmail = postUserDTO.getGuardianEmail();
            log.info("Guardian Email: {}", guardianEmail);
            Optional<UserDAO> guardianUserOpt = userRepository.findByEmail(guardianEmail);
            UserDAO guardianDao = null;
            if (guardianUserOpt.isPresent()) {
                String email = guardianUserOpt.get().getEmail();
                log.info("Guardian Email from DB: {}", email);
                if (StringUtils.isEmpty(email) && email.equalsIgnoreCase(guardianEmail))
                    log.info("Guardian Email {} already present in our DB", guardianEmail);
            }
            else {
                // Create Guardian
                UserDAO guardianDAO = new UserDAO();
                guardianDAO.setFirstName(postUserDTO.getGuardianFirstName());
                guardianDAO.setLastName(postUserDTO.getGuardianLastName());
                guardianDAO.setEmail(postUserDTO.getGuardianEmail());
                guardianDAO.setType(UserType.GUARDIAN.name());
                userRepository.save(guardianDAO);
                log.info("Save New Guardian: {}", guardianDAO);
            }
            // Send email to student and guardian
            sendEmailTo(postUserDTO.getEmail(), postUserDTO.getGuardianEmail());

            // Generate and send the response
            UserResponseDTO userResponseDTO = new ModelMapper().map(postUserDTO, UserResponseDTO.class);
            response.setMeta(Meta.builder().statusCode(HttpStatus.CREATED.value()).message("Student Created Successfully").build());
            response.setData(userResponseDTO);
            return response;
        } catch (IquaisException iqEx) {
            throw iqEx;
        } catch (Exception ex) {
            log.error("An exception occurred: {}", ex.getMessage(), ex);
            ex.getStackTrace();
            throw new IquaisException(HttpStatus.INTERNAL_SERVER_ERROR, "IQ001", "Exception occurred while generating and storing in DB");
        }
    }

    /**
     * Sends an email to the student and guardian with account creation and linking information.
     *
     * @param studentEmail  The email address of the student.
     * @param guardianEmail The email address of the guardian.
     * @throws IquaisException If an error occurs while sending the email.
     */
    private void sendEmailTo(String studentEmail, String guardianEmail) throws IquaisException {
        try {
            emailService.sendEmail(studentEmail, guardianEmail, "Student Account Created and Guardian Linked", "Welcome to our platform! \nYour password will be sent in separate email!!");
            log.info("Email sent successfully!!");
        } catch (MessagingException msgEx) {
            // Handle exception properly in production code
            log.error("Error sending email: {}", msgEx.getMessage(), msgEx);
            // You can rethrow the exception if necessary
            throw new EmailSendingException(HttpStatus.INTERNAL_SERVER_ERROR, "IQ003", "Error while sending Email");
        }
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
            userResponseDTO = new ModelMapper().map(userDAO, UserResponseDTO.class);
            response.setData(userResponseDTO);
            response.setMeta(Meta.builder().statusCode(HttpStatus.FOUND.value()).message("Student Retrieved Successfully").build());
        } else {
            response.setMeta(Meta.builder().statusCode(HttpStatus.NOT_FOUND.value()).message("Student Not Found in DB").build());
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
        ModelMapper modelMapper = new ModelMapper();
        userDAOS.forEach(userDAO -> usersList.add(modelMapper.map(userDAO, UserResponseDTO.class)));
        if (usersList.isEmpty())
            response.setMeta(Meta.builder().message("DataBase is Empty").statusCode(HttpStatus.NOT_FOUND.value()).build());
        else
            response.setMeta(Meta.builder().message("Retrieval Successful").statusCode(HttpStatus.OK.value()).build());
        log.info("Users Details: {}", usersList);
        response.setData(usersList);
        return response;
    }
}
