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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.swing.text.StyledEditorKit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation class for managing user-related operations.
 */
@Service
public class UserServiceImpl implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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
        try {
            UserDAO studentDAO = createStudent(postUserDTO);
            UserDAO guardianDAO = createGuardian(postUserDTO);
            log.info("studentDAO : {}", studentDAO);
            log.info("guardianDAO : {}", guardianDAO);
            sendEmails(postUserDTO.getEmail(), postUserDTO.getGuardianEmail());

            UserResponseDTO userResponseDTO = modelMapper.map(postUserDTO, UserResponseDTO.class);
            Response<UserResponseDTO> response = Response.<UserResponseDTO>builder()
                            .data(userResponseDTO)
                                    .meta(Meta.builder().statusCode(HttpStatus.CREATED.value()).message("Student Created Successfully").build())
                                            .build();
            return response;
        } catch (IquaisException iqEx) {
            throw iqEx;
        } catch (Exception ex) {
            log.error("An exception occurred: {}", ex.getMessage(), ex);
            throw new IquaisException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.IQ005, "Exception while storing in DB and sending Email");
        }
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
            UserDAO studentDAO = modelMapper.map(postUserDTO, UserDAO.class);
            studentDAO.setType(UserType.STUDENT.name());
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
     * @param studentEmail  The email address of the student.
     * @param guardianEmail The email address of the guardian.
     * @throws IquaisException If an error occurs while sending the email.
     */
    private void sendEmails(String studentEmail, String guardianEmail) throws IquaisException {
        try {
            emailService.sendEmail(studentEmail, guardianEmail, "Student Account Created", "Welcome to our platform! \nGuardian Linked and your password will be sent in separate email!!");
            log.info("Email sent successfully!!");
        } catch (MessagingException msgEx) {
            // Handle exception properly in production code
            log.error("Error sending email: {}", msgEx.getMessage(), msgEx);
            // You can rethrow the exception if necessary
            throw new EmailSendingException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.IQ003, "Error while sending Email");
        }
    }

    /**
     * Retrieves a student's information based on the provided ID.
     *
     * @param id The unique ID of the student to retrieve.
     * @return A response containing the retrieved student's information.
     */
    public Response<UserResponseDTO> getStudentById(String id) {
        UserResponseDTO userResponseDTO = null;
        Optional<UserDAO> optionalUserDAO = userRepository.findById(new ObjectId(id));
        if (optionalUserDAO.isPresent()) {
            UserDAO userDAO = optionalUserDAO.get();
            userResponseDTO = modelMapper.map(userDAO, UserResponseDTO.class);
            Response<UserResponseDTO> response = Response.<UserResponseDTO>builder()
                            .data(userResponseDTO)
                                    .meta(Meta.builder().statusCode(HttpStatus.FOUND.value()).message("User Retrieved Successfully").build())
                                            .build();
            log.info("User {} present in our DB", userResponseDTO.getFirstName());
            return response;
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
     * @throws IquaisException If an error occurs during searching a data.
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

            Response<List<UserResponseDTO>> response = Response.<List<UserResponseDTO>>builder()
                            .data(usersList)
                                    .meta(Meta.builder().message("Retrieval Successful").statusCode(HttpStatus.OK.value()).build())
                                            .build();
            log.info("Total Users: {}", usersList.size());
            log.info("Users Details: {}", usersList);
            return response;
        } catch (DataAccessException ex){
            throw new IquaisException(HttpStatus.NOT_FOUND, ErrorCodes.IQ006, "No data found for the search string");
        } catch (Exception ex) {
            throw new IquaisException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.IQ007, "Internal Error");
        }
    }
}
