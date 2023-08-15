package com.aiykr.iquais.repository;

import com.aiykr.iquais.entity.UserDAO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserDAO, ObjectId> {

    List<UserDAO> findAllByEmail(String email);
}
