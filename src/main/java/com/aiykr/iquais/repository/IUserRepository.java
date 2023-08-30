package com.aiykr.iquais.repository;

import com.aiykr.iquais.entity.UserDAO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing user data in the MongoDB database.
 */
@Repository
public interface IUserRepository extends MongoRepository<UserDAO, ObjectId> {

    /**
     * Find a user by their email address.
     *
     * This method searches for a user in the database using the provided email address.
     *
     * @param studentEmail The email address of the user to be found.
     * @return An Optional containing a UserDAO if a matching user is found, or an empty Optional if no match is found.
     */
    Optional<UserDAO> findByEmail(String studentEmail);

    /**
     * Find a user by their encoded password.
     *
     * This method searches for a user in the database using the provided encoded password.
     *
     * @param password The encoded password of the user to be found.
     * @return An Optional containing a UserDAO if a matching user with the specified encoded password is found,
     *         or an empty Optional if no match is found.
     */
    Optional<UserDAO> findByPassword(String password);
}
