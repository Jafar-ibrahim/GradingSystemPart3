package com.example.gradingsystempart3.Controller;

import com.example.gradingsystempart3.DAO.GradeDAO;
import com.example.gradingsystempart3.DAO.StudentDAO;
import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Exceptions.DuplicateRecordException;
import com.example.gradingsystempart3.Exceptions.UserNotFoundException;
import com.example.gradingsystempart3.Model.Grade;
import com.example.gradingsystempart3.Model.Section;
import com.example.gradingsystempart3.Model.UserDTO;
import com.example.gradingsystempart3.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/instructor/crud/grades")
public class InstructorCrudGradeController {
    private final SectionService sectionService;
    private final UserService userService;
    private final EnrollmentService enrollmentService;
    private final GradeService gradeService;

    @Autowired
    public InstructorCrudGradeController(SectionService sectionService, UserService userService, EnrollmentService enrollmentService, GradeService gradeService) {
        this.sectionService = sectionService;
        this.userService = userService;
        this.enrollmentService = enrollmentService;
        this.gradeService = gradeService;
    }

    @GetMapping
    public String instructorGradesView(@RequestParam(name = "current_user_id") int currentUserId ,
                                 @RequestParam(name = "instructor_id",required = false) Integer instructorId,
                                 @RequestParam(name = "section_id",required = false) Integer sectionId , Model model){
        UserDTO userDTO = new UserDTO(userService.getById(currentUserId));
        String instructorName = userDTO.getFirstName() +" "+userDTO.getLastName();
        if(filled(sectionId)){
            Section section = sectionService.getById(sectionId);
            String courseName = section.getCourse().getName();
            List<Grade> sectionGrades = gradeService.getSectionGrades(sectionId);
            List<Integer> studentsIds = enrollmentService.getStudentIdsBySection(sectionId);
            model.addAttribute("section_id", sectionId);
            model.addAttribute("students_ids", studentsIds);
            model.addAttribute("section_grades", sectionGrades);
            model.addAttribute("course_name", courseName);

        }
        List<Section> sections = enrollmentService.getInstructorSections(instructorId);

        model.addAttribute("sections", sections);
        model.addAttribute("user_id", currentUserId);
        model.addAttribute("instructor_id", instructorId);
        model.addAttribute("instructor_name", instructorName);
        return "instructor_grades_view";
    }

    @PostMapping("/add")
    public String addGrade(@RequestParam("current_user_id") int currentUserId,
                           @RequestParam(name = "instructor_id",required = false) Integer instructorId,
                           @RequestParam(value = "section_id",required = false) Integer sectionId,
                           @RequestParam(value = "student_id",required = false) Integer studentId,
                           @RequestParam(value = "grade",required = false) Double grade, Model model){
        
        if(filled(sectionId) && filled(studentId) && filled(grade)){
            try {
                StudentDAO.checkStudentExists(studentId);
                GradeDAO.checkGradeExists(studentId,sectionId);
                if(!enrollmentService.studentSectionExists(studentId,sectionId)){
                    model.addAttribute("error","Student is not enrolled in this section");
                } else if (grade < 0 || grade > 100) {
                    model.addAttribute("error","Grade must be 0-100");
                } else {
                    gradeService.addGrade(studentId,sectionId,grade);
                    model.addAttribute("success","Grade added successfully");
                }
            } catch (UserNotFoundException e) {
                model.addAttribute("error","Invalid student ID , try again");
            } catch (DuplicateRecordException e) {
                model.addAttribute("error","Grade already exists");
            }
        }else
            model.addAttribute("error","Please enter all necessary info");

        return instructorGradesView(currentUserId,instructorId,sectionId,model);

    }
    @PostMapping("/delete")
    public String deleteGrade(@RequestParam("current_user_id") int currentUserId,
                              @RequestParam(name = "instructor_id",required = false) Integer instructorId,
                              @RequestParam(value = "section_id",required = false) Integer sectionId,
                              @RequestParam(value = "student_id",required = false) Integer studentId, Model model){
        
        if(filled(sectionId) && filled(studentId)){
            try {
                StudentDAO.checkStudentExists(studentId);
                if(!GradeDAO.gradeExists(studentId,sectionId)){
                    model.addAttribute("error","A grade does not exist for this student");
                }else {
                    gradeService.deleteGrade(studentId, sectionId);
                    model.addAttribute("success","Grade deleted successfully");
                }
            } catch (UserNotFoundException e) {
                model.addAttribute("error","Invalid student ID , try again");
            }
        }else
            model.addAttribute("error","Please enter all necessary info");

        return instructorGradesView(currentUserId,instructorId,sectionId,model);
    }
    @PostMapping("/update")
    public String updateGrade(@RequestParam("current_user_id") int currentUserId,
                              @RequestParam(name = "instructor_id",required = false) Integer instructorId,
                              @RequestParam(value = "section_id",required = false) Integer sectionId,
                              @RequestParam(value = "student_id",required = false) Integer studentId,
                              @RequestParam(value = "grade",required = false) Double grade, Model model){
        
        if(filled(sectionId)){
            try {
                StudentDAO.checkStudentExists(studentId);
                if(!GradeDAO.gradeExists(studentId,sectionId)){
                    model.addAttribute("error","A grade does not exist for this student , add one instead");
                }else if (grade < 0 || grade > 100) {
                    model.addAttribute("error","Grade must be 0-100");
                }else {
                    gradeService.updateGrade(studentId, sectionId, grade);
                    model.addAttribute("success","Grade modified successfully");
                }
            } catch (UserNotFoundException e) {
                model.addAttribute("error","Invalid student ID , try again");
            }
        }else {
            model.addAttribute("error","Please enter the Section ID");
        }

        return instructorGradesView(currentUserId,instructorId,sectionId,model);
    }

    private boolean filled(Object o){
        if (o instanceof String)
            return !((String)o).isEmpty();
        return o != null;
    }
}
