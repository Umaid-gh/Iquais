package com.aiykr.iquais.service.impl;

import com.aiykr.iquais.dto.request.UserDto;
import com.aiykr.iquais.dto.response.UserResponse;
import com.aiykr.iquais.repository.UserRepository;
import com.aiykr.iquais.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    public UserResponse<List<UserDto>> getAllUsers() {
        List<UserDto> userDtoList = new ArrayList<>();
        UserResponse<List<UserDto>> response = new UserResponse<>();

        // Fetch companyData from DB
//        List<User> dataDb = (List<User>)userRepository.findAll();
//
//        if (dataDb.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database is empty");
//        } else {
//            for (int i = 0; i < dataDb.size(); i++) {
//                UserDao user = dataDb.get(i);
//                int id = user.getId();
//                UserDto dto = new UserDto(user);

        // saving in List
//                userDtoList.add(dto);
//            }
//            response.setData(userDtoList);
//            response.setMeta(getAllUsers().getMeta());
//        }
        return response;
    }
}
