package com.aiykr.iquais.repository;

import com.aiykr.iquais.entity.UserDAO;
import io.swagger.annotations.Api;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing user data in the MongoDB database.
 */
@Api(tags = "User Repository")
@Repository
public interface UserRepository extends MongoRepository<UserDAO, ObjectId> {

    /**
     * Retrieves a list of users based on the provided email address.
     *
     * @param email The email address to search for.
     * @return A list of users matching the given email.
     */
    List<UserDAO> findAllByEmail(String email);
}
