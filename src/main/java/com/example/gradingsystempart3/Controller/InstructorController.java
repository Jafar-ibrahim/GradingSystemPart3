package com.example.gradingsystempart3.Controller;

import com.example.gradingsystempart3.Model.Section;
import com.example.gradingsystempart3.Model.UserDTO;
import com.example.gradingsystempart3.Service.EnrollmentService;
import com.example.gradingsystempart3.Service.GradeService;
import com.example.gradingsystempart3.Service.SectionService;
import com.example.gradingsystempart3.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    private final SectionService sectionService;
    private final UserService userService;
    private final EnrollmentService enrollmentService;
    private final GradeService gradeService;
    

    @Autowired
    public InstructorController(SectionService sectionService, UserService userService, EnrollmentService enrollmentService, GradeService gradeService) {
        this.sectionService = sectionService;
        this.userService = userService;
        this.enrollmentService = enrollmentService;
        this.gradeService = gradeService;
    }

    @GetMapping("/instructor_view")
    public String instructorView(@RequestParam(name = "current_user_id") int currentUserId , Model model){
        model.addAttribute("user_fullName",userService.getUserFullName(currentUserId));
        return "instructor_view";
    }

    @GetMapping("/assigned_sections")
    public String assignedSections(@RequestParam(name = "current_user_id") int currentUserId,
                                   @RequestParam(name = "instructor_id",required = false) Integer instructorId, Model model){
        
        UserDTO userDTO = new UserDTO(userService.getById(currentUserId));
        String instructorName = userDTO.getFirstName()+" "+userDTO.getLastName();
        List<Section> sections = enrollmentService.getInstructorSections(instructorId);
        model.addAttribute("instructor_sections", sections);
        model.addAttribute("instructor_name", instructorName);

        return "instructor_sections_view";
    }

    @GetMapping("/statistical_info")
    public String statisticalInfo(@RequestParam(name = "current_user_id") int currentUserId,
                                  @RequestParam(name = "instructor_id",required = false) Integer instructorId,
                                  @RequestParam(name = "section_id",required = false) Integer sectionId,Model model){

        
        UserDTO userDTO = new UserDTO(userService.getById(currentUserId));
        String instructorName = userDTO.getFirstName() +" "+userDTO.getLastName();
        if(filled(sectionId)){
            List<Double> statistics = new ArrayList<>();
            statistics.add(gradeService.getMinGradeForSection(sectionId));
            statistics.add(gradeService.getMaxGradeForSection(sectionId));
            statistics.add(gradeService.getMedianGradeForSection(sectionId));
            statistics.add(gradeService.getAverageGradeForSection(sectionId));
            Section section = sectionService.getById(sectionId);
            String courseName = section.getCourse().getName();
            model.addAttribute("section_id", sectionId);
            model.addAttribute("course_name", courseName);
            model.addAttribute("statistics", statistics);

        }
        List<Section> sections = enrollmentService.getInstructorSections(instructorId);

        model.addAttribute("sections", sections);
        model.addAttribute("user_id", currentUserId);
        model.addAttribute("instructor_id", instructorId);
        model.addAttribute("instructor_name", instructorName);

        return "instructor_statistical_view";
    }

    private boolean filled(Object o){
        if (o instanceof String)
            return !((String)o).isEmpty();
        return o != null;
    }
}
