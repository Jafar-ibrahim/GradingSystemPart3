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

    private int currentUserId;

    @Autowired
    public AdminCrudStudentSectionController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/add")
    public String addStudentToSection(@RequestParam("current_user_id") int currentUserId,
                                         @RequestParam(value = "student_id",required = false) Integer studentId,
                                         @RequestParam(value = "section_id",required = false) Integer sectionId){
        this.currentUserId = currentUserId;
        if(filled(studentId) && filled(sectionId)){
            if(!enrollmentService.studentSectionExists(studentId,sectionId)) {
                if(!SectionService.sectionExists(sectionId)){
                    return redirectError("No Section exists with the given ID");
                }else if(!UserService.studentExists(studentId)){
                    return redirectError("No Student exists with the given ID");
                }else {
                    enrollmentService.addStudentToSection(studentId,sectionId);
                    return redirectSuccess("Student Added to Section successfully");
                }
            }else {
                return redirectError("A record already exists with the given IDs");
            }
        }else {
            return redirectError("Please enter all necessary info");
        }
    }
    @PostMapping("/delete")
    public String deleteStudentFromSection(@RequestParam("current_user_id") int currentUserId,
                                              @RequestParam(value = "student_id",required = false) Integer studentId,
                                              @RequestParam(value = "section_id",required = false) Integer sectionId){
        this.currentUserId = currentUserId;
        if(filled(sectionId) && filled(studentId)){
            if(enrollmentService.studentSectionExists(studentId,sectionId)) {
                enrollmentService.removeStudentFromSection(studentId,sectionId);
                return redirectSuccess("Student removed from Section successfully");
            }else {
                return redirectError("No record exists with the given IDs");
            }
        }else {
            return redirectError("Please enter all necessary info");
        }
    }
    private String redirectSuccess(String msg){
        return "redirect:/admin/crud_view?table=student_section&current_user_id="+currentUserId+"&success="+msg;
    }
    private String redirectError(String msg){
        return "redirect:/admin/crud_view?table=student_section&current_user_id="+currentUserId+"&error="+msg;
    }

    private boolean filled(Object o){
        if (o instanceof String)
            return !((String)o).isEmpty();
        return o != null;
    }
}
