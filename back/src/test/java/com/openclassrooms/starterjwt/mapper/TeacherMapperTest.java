package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeacherMapperTest {

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherMapper teacherMapper =  Mappers.getMapper(TeacherMapper.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToEntity() {
        // Créer un TeacherDto à mapper
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");

        // Mapper le TeacherDto en Teacher
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Vérifier les mappages
        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Doe", teacher.getLastName());
    }

    @Test
    public void testToDto() {
        // Créer un Teacher à mapper
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");

        // Mapper le Teacher en TeacherDto
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Vérifier les mappages
        assertEquals(1L, teacherDto.getId());
        assertEquals("Jane", teacherDto.getFirstName());
        assertEquals("Smith", teacherDto.getLastName());
    }
}