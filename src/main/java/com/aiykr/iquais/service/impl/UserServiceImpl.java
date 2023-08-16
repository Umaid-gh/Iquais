package com.aiykr.iquais.service.impl;

import com.aiykr.iquais.dto.request.PostUserDTO;
import com.aiykr.iquais.dto.response.Meta;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import com.aiykr.iquais.entity.UserDAO;
import com.aiykr.iquais.entity.UserType;
import com.aiykr.iquais.exception.IquaisException;
import com.aiykr.iquais.repository.UserRepository;
import com.aiykr.iquais.service.EmailService;
import com.aiykr.iquais.service.UserService;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EmailService emailService; // Assuming you have autowired the EmailService

    public Response<UserResponseDTO> createUser(PostUserDTO postUserDTO) throws IquaisException {
        Response<UserResponseDTO> response = new Response<>();
        try {
            // Check if the student is already present by email address
            String studentEmail = postUserDTO.getEmail();
            log.info("Student Email: {}", studentEmail);

            List<UserDAO> studentUsers = userRepository.findAllByEmail(studentEmail);
            log.info("Student Email from DB: {}", studentUsers);

            boolean studentExists = studentUsers.stream()
                    .anyMatch(user -> Objects.nonNull(user.getEmail()) && user.getEmail().equals(studentEmail));
            if (studentExists) {
                String existingEmail = studentUsers.get(0).getEmail();
                log.info("Student Email {} already present in our DB", existingEmail);
                throw new IquaisException(HttpStatus.CONFLICT, "IQ002", "Student Email already present in our DB");
            } else {
                // Create Student
                UserDAO studentDAO = new ModelMapper().map(postUserDTO, UserDAO.class);
                studentDAO.setType(UserType.STUDENT.name());
                userRepository.save(studentDAO);
                log.info("Save New Student: {}", studentDAO);
            }

            // Check if the guardian is already present by email address
            String guardianEmail = postUserDTO.getGuardianEmail();
            log.info("Guardian Email: {}", guardianEmail);
            List<UserDAO> guardianUsers = userRepository.findAllByEmail(guardianEmail);
            log.info("Guardian Email from DB: {}", guardianUsers);
            boolean guardianExists = guardianUsers.stream()
                    .anyMatch(user -> Objects.nonNull(user.getEmail()) && user.getEmail().equals(guardianEmail));
            if (guardianExists) {
                String existingEmail = guardianUsers.get(0).getEmail();
                log.info("Guardian Email {} already present in our DB", existingEmail);
            } else {
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
            response.setData(userResponseDTO);
            response.setMeta(Meta.builder().status(HttpStatus.CREATED.value()).message("Student Created Successfully").build());
            return response;
        } catch (IquaisException iqEx) {
            throw iqEx;
        } catch (Exception ex) {
            log.error("An exception occurred: {}", ex.getMessage(), ex);
            ex.getStackTrace();
            throw new IquaisException(HttpStatus.INTERNAL_SERVER_ERROR, "IQ001", "Exception occurred while generating and storing in DB");
        }
    }

    private void sendEmailTo(String studentEmail, String guardianEmail) throws IquaisException {
        try {
            emailService.sendEmail(studentEmail, guardianEmail, "Student Account Created and Guardian Linked", "Welcome to our platform! \nYour password will be sent in separate email!!");
            log.info("Email sent successfully!!");
        } catch (MessagingException messagingException) {
            // Handle exception properly in production code
            log.error("Error sending email: {}", messagingException.getMessage(), messagingException);
            // You can rethrow the exception if necessary
            throw new IquaisException(HttpStatus.UNAUTHORIZED, "IQ003", "Error while sending Email");
        }
    }

    public Response<UserResponseDTO> getStudentById(String id) {
        Response<UserResponseDTO> response = new Response<>();
        Optional<UserDAO> optionalUserDAO = userRepository.findById(new ObjectId(id));
        if (optionalUserDAO.isPresent()) {
            UserDAO userDAO = optionalUserDAO.get();
            UserResponseDTO userResponseDTO = new ModelMapper().map(userDAO, UserResponseDTO.class);
            response.setMeta(Meta.builder().status(HttpStatus.OK.value()).message("Student Retrieved Successfully").build());
            response.setData(userResponseDTO);
        } else {
            response.setMeta(Meta.builder().status(HttpStatus.NOT_FOUND.value()).message("Student Retrieved Successfully").build());
        }
        return response;
    }

    public Response<List<UserResponseDTO>> getAllUsers() {
        Response<List<UserResponseDTO>> response = new Response<>();
        List<UserDAO> userDAOS = userRepository.findAll();
        List<UserResponseDTO> usersList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        userDAOS.stream().forEach(userDAO -> usersList.add(modelMapper.map(userDAO, UserResponseDTO.class)));
        if(usersList.isEmpty())
            response.setMeta(Meta.builder().message("DataBase is Empty").status(HttpStatus.NOT_FOUND.value()).build());
        else
            response.setMeta(Meta.builder().message("Retrieval Successful").status(HttpStatus.OK.value()).build());
        response.setData(usersList);
        return response;
    }
}
