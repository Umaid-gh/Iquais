package com.aiykr.iquais.repository;

import com.aiykr.iquais.entity.UserDAO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing user data in the MongoDB database.
 */
@Repository
public interface UserRepository extends MongoRepository<UserDAO, ObjectId> {

    /**
     * Retrieves a list of users based on the provided email address.
     *
     * @param email The email address to search for.
     * @return A list of users matching the given email.
     */
    List<UserDAO> findAllByEmail(String email);

    Optional<UserDAO> findByEmail(String studentEmail);
}
