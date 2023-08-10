package com.aiykr.iquais.dto.request;

import com.aiykr.iquais.entity.Guardian;
import com.aiykr.iquais.entity.Student;
import lombok.Data;

@Data
public class UserDto {
    Student student;
    Guardian guardian;

    public UserDto(UserDao dao) {
        // Converting from DTO to Entity
        this.student.setId(dao.student.getId());
        this.student.setName(dao.student.getName());
        this.student.setEmail(dao.student.getEmail());
        this.student.setPhoneNumber(dao.student.getPhoneNumber());
        this.student.setType(dao.student.getType());
        this.student.setStatus(dao.student.getStatus());
        this.student.setDob(dao.student.getDob());
        this.student.setGrade(dao.student.getGrade());
        this.student.setLocation(dao.student.getLocation());
        this.student.setInstitutionName(dao.student.getInstitutionName());
        this.guardian.setName(dao.guardian.getName());
        this.guardian.setEmail(dao.guardian.getEmail());
        this.guardian.setPhoneNumber(dao.guardian.getPhoneNumber());
        this.guardian.setId(dao.guardian.getId());
        this.guardian.setType(dao.guardian.getType());
        this.guardian.setStatus(dao.guardian.getStatus());
    }
}
