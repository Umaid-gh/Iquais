package com.aiykr.iquais.dto.request;

import com.aiykr.iquais.entity.Guardian;
import com.aiykr.iquais.entity.Student;

public class UserDao {
    Student student;
    Guardian guardian;

    public UserDao(UserDto dto) {
        // Converting from DTO to Entity
        this.student.setId(dto.student.getId());
        this.student.setName(dto.student.getName());
        this.student.setEmail(dto.student.getEmail());
        this.student.setPhoneNumber(dto.student.getPhoneNumber());
        this.student.setType(dto.student.getType());
        this.student.setStatus(dto.student.getStatus());
        this.student.setDob(dto.student.getDob());
        this.student.setGrade(dto.student.getGrade());
        this.student.setLocation(dto.student.getLocation());
        this.student.setInstitutionName(dto.student.getInstitutionName());
        this.guardian.setName(dto.guardian.getName());
        this.guardian.setEmail(dto.guardian.getEmail());
        this.guardian.setPhoneNumber(dto.guardian.getPhoneNumber());
        this.guardian.setId(dto.guardian.getId());
        this.guardian.setType(dto.guardian.getType());
        this.guardian.setStatus(dto.guardian.getStatus());
    }
}
