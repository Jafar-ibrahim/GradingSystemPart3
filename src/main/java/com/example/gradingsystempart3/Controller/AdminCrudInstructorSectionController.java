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

    
    @Autowired
    public AdminCrudInstructorSectionController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/add")
    public String addInstructorToSection(@RequestParam("current_user_id") int currentUserId,
                             @RequestParam(value = "instructor_id",required = false) Integer instructorId,
                             @RequestParam(value = "section_id",required = false) Integer sectionId){
        
        if(filled(instructorId) && filled(sectionId)){
            if(!enrollmentService.instructorSectionExists(instructorId,sectionId)) {
                if(!SectionService.sectionExists(sectionId)){
                    return redirectError(currentUserId,"No Section exists with the given ID");
                }else if(!UserService.instructorExists(instructorId)){
                    return redirectError(currentUserId,"No Instructor exists with the given ID");
                }else {
                    enrollmentService.addInstructorToSection(instructorId,sectionId);
                    return redirectSuccess(currentUserId,"Instructor Added to Section successfully");
                }
            }else {
                return redirectError(currentUserId,"A record already exists with the given IDs");
            }
        }else {
            return redirectError(currentUserId,"Please enter all necessary info");
        }
    }
    @PostMapping("/delete")
    public String deleteInstructorFromSection(@RequestParam("current_user_id") int currentUserId,
                                              @RequestParam(value = "instructor_id",required = false) Integer instructorId,
                                              @RequestParam(value = "section_id",required = false) Integer sectionId){
        
        if(filled(sectionId) && filled(instructorId)){
            if(enrollmentService.instructorSectionExists(instructorId,sectionId)) {
                enrollmentService.removeInstructorFromSection(instructorId,sectionId);
                return redirectSuccess(currentUserId,"Instructor removed from Section successfully");
            }else {
                return redirectError(currentUserId,"No record exists with the given IDs");
            }
        }else {
            return redirectError(currentUserId,"Please enter all necessary info");
        }
    }
    private String redirectSuccess(int currentUserId,String msg){
        return "redirect:/admin/crud_view?table=instructor_section&current_user_id="+currentUserId+"&success="+msg;
    }
    private String redirectError(int currentUserId,String msg){
        return "redirect:/admin/crud_view?table=instructor_section&current_user_id="+currentUserId+"&error="+msg;
    }

    private boolean filled(Object o){
        if (o instanceof String)
            return !((String)o).isEmpty();
        return o != null;
    }
}
