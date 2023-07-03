package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {
    private TeacherRepository teacherRepository;
    private TeacherController teacherController;
    private  TeacherMapper teacherMapper;
    private TeacherService teacherService;

    @BeforeEach
    void setUp(){
        teacherRepository = Mockito.mock(TeacherRepository.class);
        teacherMapper = Mockito.mock(TeacherMapper.class);
        teacherService = new TeacherService(teacherRepository);
        teacherController = new TeacherController( teacherService, teacherMapper);
        }

    @DisplayName("Id is valid")
    @Test
    void testFindByIdSuccess(){
        String id = "00000001";
        Teacher teacher = new Teacher();

        teacher.setId(Long.valueOf(id));
        teacher.setFirstName("Alan");
        teacher.setLastName("Turing");
        LocalDateTime localDate = LocalDateTime.now();
        teacher.setCreatedAt(localDate);
        teacher.setUpdatedAt(localDate);
        Optional<Teacher> opt = Optional.ofNullable(teacher);

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(teacher.getId());
        teacherDto.setFirstName(teacher.getFirstName());
        teacherDto.setLastName(teacher.getLastName());
        teacherDto.setCreatedAt(teacher.getCreatedAt());
        teacherDto.setUpdatedAt(teacher.getUpdatedAt());
        when(teacherRepository.findById(Long.valueOf(id))).thenReturn(opt);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);
        var response = teacherController.findById(id);
        //Retour de données
        assertEquals( teacherDto,response.getBody());
        assertEquals(HttpHeaders.EMPTY,response.getHeaders());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("Id is not valid")
    @Test
    void testFindByIdFail(){
        String id = "00000001";
        when(teacherRepository.findById(Long.valueOf(id))).thenReturn(Optional.empty());
        var response = teacherController.findById(id);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @DisplayName("test return all teachers")
    @Test
    void testFindAll() {
        List<Teacher> teachers = new ArrayList<>();
        String id = "00000001";
        Teacher teacher = new Teacher();

        teacher.setId(Long.valueOf(id));
        teacher.setFirstName("Alan");
        teacher.setLastName("Turing");
        LocalDateTime localDate = LocalDateTime.now();
        teacher.setCreatedAt(localDate);
        teacher.setUpdatedAt(localDate);
        teachers.add(teacher);

        List<TeacherDto> teachersDto = new ArrayList<>();
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(teacher.getId());
        teacherDto.setFirstName(teacher.getFirstName());
        teacherDto.setLastName(teacher.getLastName());
        teacherDto.setCreatedAt(teacher.getCreatedAt());
        teacherDto.setUpdatedAt(teacher.getUpdatedAt());
        teachersDto.add(teacherDto);

        when(teacherRepository.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teachersDto);
        var response = teacherController.findAll();

        assertEquals(teachersDto,response.getBody());
        assertEquals(HttpHeaders.EMPTY,response.getHeaders());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}