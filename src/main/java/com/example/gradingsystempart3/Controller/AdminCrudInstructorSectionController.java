package com.example.gradingsystempart3.Controller;

import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Service.UserService;
import com.example.gradingsystempart3.Service.EnrollmentService;
import com.example.gradingsystempart3.Service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/crud/instructor_section")
public class AdminCrudInstructorSectionController {
    EnrollmentService enrollmentService;

    private int currentUserId;
    @Autowired
    public AdminCrudInstructorSectionController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/add")
    public String addInstructorToSection(@RequestParam("current_user_id") int currentUserId,
                             @RequestParam(value = "instructor_id",required = false) Integer instructorId,
                             @RequestParam(value = "section_id",required = false) Integer sectionId){
        this.currentUserId = currentUserId;
        if(filled(instructorId) && filled(sectionId)){
            if(!enrollmentService.instructorSectionExists(instructorId,sectionId)) {
                if(!SectionService.sectionExists(sectionId)){
                    return redirectError("No Section exists with the given ID");
                }else if(!UserService.instructorExists(instructorId)){
                    return redirectError("No Instructor exists with the given ID");
                }else {
                    enrollmentService.addInstructorToSection(instructorId,sectionId);
                    return redirectSuccess("Instructor Added to Section successfully");
                }
            }else {
                return redirectError("A record already exists with the given IDs");
            }
        }else {
            return redirectError("Please enter all necessary info");
        }
    }
    @PostMapping("/delete")
    public String deleteInstructorFromSection(@RequestParam("current_user_id") int currentUserId,
                                              @RequestParam(value = "instructor_id",required = false) Integer instructorId,
                                              @RequestParam(value = "section_id",required = false) Integer sectionId){
        this.currentUserId = currentUserId;
        if(filled(sectionId) && filled(instructorId)){
            if(enrollmentService.instructorSectionExists(instructorId,sectionId)) {
                enrollmentService.removeInstructorFromSection(instructorId,sectionId);
                return redirectSuccess("Instructor removed from Section successfully");
            }else {
                return redirectError("No record exists with the given IDs");
            }
        }else {
            return redirectError("Please enter all necessary info");
        }
    }
    private String redirectSuccess(String msg){
        return "redirect:/admin/crud_view?table=instructor_section&current_user_id="+currentUserId+"&success="+msg;
    }
    private String redirectError(String msg){
        return "redirect:/admin/crud_view?table=instructor_section&current_user_id="+currentUserId+"&error="+msg;
    }

    private boolean filled(Object o){
        if (o instanceof String)
            return !((String)o).isEmpty();
        return o != null;
    }
}
