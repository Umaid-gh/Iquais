package com.aiykr.iquais.service.impl;

import com.aiykr.iquais.dto.request.PostUserDTO;
import com.aiykr.iquais.dto.response.Meta;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import com.aiykr.iquais.entity.UserDAO;
import com.aiykr.iquais.entity.UserType;
import com.aiykr.iquais.exception.ResponseExceptionHandler;
import com.aiykr.iquais.repository.UserRepository;
import com.aiykr.iquais.service.UserService;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Response> createUser(PostUserDTO postUserDTO) throws ResponseExceptionHandler {
        try {
            //Check if the student is already present by email address


            //Create Student
            UserDAO studentDAO = new ModelMapper().map(postUserDTO, UserDAO.class);
            studentDAO.setType(UserType.STUDENT.name());

            userRepository.save(studentDAO);

            //Check if the guardian is already present by email address

            //Create Guardian
            UserDAO guardianDAO = new UserDAO();
            guardianDAO.setFirstName(postUserDTO.getGuardianFirstName());
            guardianDAO.setLastName(postUserDTO.getGuardianLastName());
            guardianDAO.setEmail(postUserDTO.getGuardianEmail());
            guardianDAO.setType(UserType.GUARDIAN.name());

            userRepository.save(guardianDAO);
        } catch (Exception ex) {
            throw new ResponseExceptionHandler("Exception occured while generating and storing in DB");
        }

        //Generate and send the response
        Response response = new Response();
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

    public Response<List<UserResponseDTO>> getAllUsersByPagination(int page, int size, String sortBy, String sortOrder) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<UserDAO> userPage = userRepository.findAll(pageable);

            if (!userPage.hasContent()) {
                // Database is present but empty
                Response<List<UserResponseDTO>> emptyResponse = new Response<>();
                Meta emptyMeta = Meta.builder().message("Database is empty").status(HttpStatus.NOT_FOUND.value()).build();
                emptyResponse.setMeta(emptyMeta);
                return emptyResponse;
            } else {
                // Database is present and has data
                List<UserResponseDTO> usersList = userPage.getContent().stream()
                        .map(userDAO -> new ModelMapper().map(userDAO, UserResponseDTO.class))
                        .collect(Collectors.toList());

                Response<List<UserResponseDTO>> response = new Response<>();
                Meta meta = Meta.builder().message("Retrieval Successful").status(HttpStatus.OK.value()).build();
                response.setMeta(meta);
                response.setData(usersList);

                return response;
            }
        } catch (DataAccessException ex) {
            // Log the exception details for troubleshooting
            ex.printStackTrace();

            Response<List<UserResponseDTO>> errorResponse = new Response<>();
            Meta meta = Meta.builder().message("Database not found").status(HttpStatus.NOT_FOUND.value()).build();
            errorResponse.setMeta(meta);
            return errorResponse;
        } catch (Exception ex) {
            // Log the exception details for troubleshooting
            ex.printStackTrace();

            Response<List<UserResponseDTO>> errorResponse = new Response<>();
            Meta meta = Meta.builder().message("An error occurred: " + ex.getMessage()).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
            errorResponse.setMeta(meta);
            return errorResponse;
        }
    }
}
