package com.example.gradingsystempart3.Controller;

import com.example.gradingsystempart3.Model.Grade;
import com.example.gradingsystempart3.Model.Section;
import com.example.gradingsystempart3.Model.UserDTO;
import com.example.gradingsystempart3.Service.EnrollmentService;
import com.example.gradingsystempart3.Service.GradeService;
import com.example.gradingsystempart3.Service.SectionService;
import com.example.gradingsystempart3.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final UserService userService;
    private final EnrollmentService enrollmentService;
    private final GradeService gradeService;


    @Autowired
    public StudentController(UserService userService, EnrollmentService enrollmentService, GradeService gradeService) {
        this.userService = userService;
        this.enrollmentService = enrollmentService;
        this.gradeService = gradeService;
    }

    @GetMapping("/student_view")
    public String instructorView(@RequestParam(name = "current_user_id") int currentUserId , Model model){
        model.addAttribute("user_fullName",userService.getUserFullName(currentUserId));
        return "student_view";
    }

    @GetMapping("/enrolled_sections")
    public String enrolledSections(@RequestParam(name = "current_user_id") int currentUserId,
                                   @RequestParam(name = "student_id",required = false) Integer studentId, Model model){

        UserDTO userDTO = new UserDTO(userService.getById(currentUserId));
        String studentName = userDTO.getFirstName()+" "+userDTO.getLastName();
        List<Section> sections = enrollmentService.getInstructorSections(studentId);
        model.addAttribute("student_sections", sections);
        model.addAttribute("student_name", studentName);

        return "student_courses_view";
    }

    @GetMapping("/grade_report")
    public String gradeReport(@RequestParam(name = "current_user_id") int currentUserId,
                              @RequestParam(name = "student_id",required = false) Integer studentId, Model model){
        UserDTO userDTO = new UserDTO(userService.getById(currentUserId));
        String studentName = userDTO.getFirstName()+" "+userDTO.getLastName();

        List<Pair<Grade, Double>> gradeReport = gradeService.getGradeReport(studentId);
        double studentAverage = gradeService.getStudentAverage(studentId);

        model.addAttribute("gradeReport", gradeReport);
        model.addAttribute("student_name", studentName);
        model.addAttribute("student_average", studentAverage);

        return "student_grade_report_view";
    }


}
