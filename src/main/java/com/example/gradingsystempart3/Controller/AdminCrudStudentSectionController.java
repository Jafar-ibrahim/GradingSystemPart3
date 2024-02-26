package com.example.gradingsystempart3.Controller;

import com.example.gradingsystempart3.Service.UserService;
import com.example.gradingsystempart3.Service.EnrollmentService;
import com.example.gradingsystempart3.Service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/crud/student_section")
public class AdminCrudStudentSectionController {
    EnrollmentService enrollmentService;

    

    @Autowired
    public AdminCrudStudentSectionController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/add")
    public String addStudentToSection(@RequestParam("current_user_id") int currentUserId,
                                         @RequestParam(value = "student_id",required = false) Integer studentId,
                                         @RequestParam(value = "section_id",required = false) Integer sectionId){
        
        if(filled(studentId) && filled(sectionId)){
            if(!enrollmentService.studentSectionExists(studentId,sectionId)) {
                if(!SectionService.sectionExists(sectionId)){
                    return redirectError(currentUserId,"No Section exists with the given ID");
                }else if(!UserService.studentExists(studentId)){
                    return redirectError(currentUserId,"No Student exists with the given ID");
                }else {
                    enrollmentService.addStudentToSection(studentId,sectionId);
                    return redirectSuccess(currentUserId,"Student Added to Section successfully");
                }
            }else {
                return redirectError(currentUserId,"A record already exists with the given IDs");
            }
        }else {
            return redirectError(currentUserId,"Please enter all necessary info");
        }
    }
    @PostMapping("/delete")
    public String deleteStudentFromSection(@RequestParam("current_user_id") int currentUserId,
                                              @RequestParam(value = "student_id",required = false) Integer studentId,
                                              @RequestParam(value = "section_id",required = false) Integer sectionId){
        
        if(filled(sectionId) && filled(studentId)){
            if(enrollmentService.studentSectionExists(studentId,sectionId)) {
                enrollmentService.removeStudentFromSection(studentId,sectionId);
                return redirectSuccess(currentUserId,"Student removed from Section successfully");
            }else {
                return redirectError(currentUserId,"No record exists with the given IDs");
            }
        }else {
            return redirectError(currentUserId,"Please enter all necessary info");
        }
    }
    private String redirectSuccess(int currentUserId,String msg){
        return "redirect:/admin/crud_view?table=student_section&current_user_id="+currentUserId+"&success="+msg;
    }
    private String redirectError(int currentUserId,String msg){
        return "redirect:/admin/crud_view?table=student_section&current_user_id="+currentUserId+"&error="+msg;
    }

    private boolean filled(Object o){
        if (o instanceof String)
            return !((String)o).isEmpty();
        return o != null;
    }
}
