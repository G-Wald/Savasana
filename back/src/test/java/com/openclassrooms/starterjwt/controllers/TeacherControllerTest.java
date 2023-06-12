package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    private TeacherController teacherController;
    private TeacherService teacherService;
    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp(){
        teacherService = Mockito.mock(TeacherService.class);
        teacherMapper = Mockito.mock(TeacherMapper.class);
        teacherController = new TeacherController(teacherService, teacherMapper);

    }
    @DisplayName("Id is not valid")
    @Test
    void testFindByIdFail(){
        String id = "00000001";
        when(teacherService.findById(Long.valueOf(id))).thenReturn(null);
        var response = teacherController.findById(id);

        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
        when(teacherService.findById(Long.valueOf(id))).thenReturn(teacher);
        var response = teacherController.findById(id);

        //Retour de données??
        assertEquals( null,response.getBody());
        assertEquals(HttpHeaders.EMPTY,response.getHeaders());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    public ResponseEntity<?> findAllTest() {
        List<Teacher> teachers = this.teacherService.findAll();

        return ResponseEntity.ok().body(this.teacherMapper.toDto(teachers));
    }

}