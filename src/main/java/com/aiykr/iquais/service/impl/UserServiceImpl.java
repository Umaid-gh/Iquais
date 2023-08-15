package com.aiykr.iquais.service.impl;

import com.aiykr.iquais.dto.request.PostUserDTO;
import com.aiykr.iquais.dto.response.Meta;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import com.aiykr.iquais.entity.UserDAO;
import com.aiykr.iquais.entity.UserType;
import com.aiykr.iquais.exception.IquaisException;
import com.aiykr.iquais.repository.UserRepository;
import com.aiykr.iquais.service.UserService;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Response> createUser(PostUserDTO postUserDTO) throws IquaisException {
        Response response = new Response();
        try {
            String studentEmail = postUserDTO.getEmail();
            log.info("Student Email: {}", studentEmail);
            List<UserDAO> studentUsers = userRepository.findAllByEmail(studentEmail);
            log.info("Student Email from DB: {}", studentUsers);
            boolean studentExists = studentUsers.stream().anyMatch(user -> user.getEmail() != null && user.getEmail().equals(studentEmail));
            if (studentExists) {
                log.info("Student Email {} already present in our DB", studentUsers.get(0).getEmail());
                throw new IquaisException(HttpStatus.CONFLICT,"IQ002","Student Email already present in our DB");
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
            boolean guardianExists = guardianUsers.stream().anyMatch(user ->
                    user.getEmail() != null && user.getEmail().equals(guardianEmail)
            );
            if (guardianExists) {
                log.info("Guardian Email {} already present in our DB", guardianUsers.get(0).getEmail());
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
        } catch (IquaisException iqEx) {
            throw iqEx;
        }
        catch (Exception ex) {
            log.error("An exception occurred: {}", ex.getMessage(), ex);
            ex.getStackTrace();
            throw new IquaisException(HttpStatus.INTERNAL_SERVER_ERROR,"IQ001","Exception occurred while generating and storing in DB");
        }

        // Generate and send the response

        response.setData(postUserDTO);
        response.setMeta(Meta.builder().status(HttpStatus.CREATED.value()).message("Student Created Successfully").build());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    public ResponseEntity<Response<UserResponseDTO>> getStudentById(String id) {
        Response response = new Response();
        Optional<UserDAO> optionalUserDAO = userRepository.findById(new ObjectId(id));
        if (optionalUserDAO.isPresent()) {
            UserDAO userDAO = optionalUserDAO.get();
            UserResponseDTO userResponseDTO = new ModelMapper().map(userDAO, UserResponseDTO.class);
            response.setMeta(Meta.builder().status(HttpStatus.OK.value()).message("Student Retrieved Successfully").build());
            response.setData(userResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.setMeta(Meta.builder().status(HttpStatus.NOT_FOUND.value()).message("Student Retrieved Successfully").build());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    public ResponseEntity<Response<List<UserResponseDTO>>> getAllUsers() {

        List<UserDAO> userDAOS = userRepository.findAll();
        List<UserResponseDTO> usersList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        userDAOS.stream().forEach(userDAO -> {
            usersList.add(modelMapper.map(userDAO, UserResponseDTO.class));
        });
        Response response = new Response();
        response.setMeta(Meta.builder().message("Retrieval Successful").status(HttpStatus.OK.value()).build());
        response.setData(usersList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
