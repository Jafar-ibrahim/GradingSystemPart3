package com.example.gradingsystempart3.Controller;

import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Service.CourseService;
import com.example.gradingsystempart3.Service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/crud/section")
public class AdminCrudSectionController {
    private final SectionService sectionService;
    private int currentUserId;

    @Autowired
    public AdminCrudSectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/add")
    public String addSection(@RequestParam("current_user_id") int currentUserId,
                            @RequestParam(value = "course_id",required = false) Integer courseId){
        this.currentUserId = currentUserId;
        if(filled(courseId)){
            if(CourseService.courseExists(courseId)){
                sectionService.addSection(courseId);
                return redirectSuccess("Section added successfully");
            }else{
                return redirectError("No Course exists with this ID");
            }
        }else{
            return redirectError("Please enter all necessary info");
        }
    }
    @PostMapping("/delete")
    public String deleteSection(@RequestParam("current_user_id") int currentUserId,
                               @RequestParam(value = "section_id",required = false) Integer sectionId){
        this.currentUserId = currentUserId;
        if(filled(sectionId)){
            if(SectionService.sectionExists(sectionId)) {
                sectionService.deleteSection(sectionId);
                return redirectSuccess("Section deleted successfully");
            }else {
                return redirectError("Section does not exist");
            }
        }else {
            return redirectError("Please enter all necessary info");
        }
    }
    @PostMapping("/update")
    public String updateSection(@RequestParam("current_user_id") int currentUserId,
                                @RequestParam(value = "section_id",required = false) Integer sectionId,
                                @RequestParam(value = "course_id",required = false) Integer courseId){
        Database database = Database.getInstance();

        if(filled(sectionId)){
            if (filled(courseId)){
                if(CourseService.courseExists(courseId)) {
                    database.updateRecord("section", "course_id", courseId, sectionId);
                    return redirectSuccess("Section updated successfully");
                }else {
                    return redirectError("Course does not exist");
                }
            }else{
                return redirectSuccess("No changes were made.");
            }
        }else {
            return redirectError("Please enter the Section ID");
        }
    }
    private String redirectSuccess(String msg){
        return "redirect:/admin/crud_view?table=section&current_user_id="+currentUserId+"&success="+msg;
    }
    private String redirectError(String msg){
        return "redirect:/admin/crud_view?table=section&current_user_id="+currentUserId+"&error="+msg;
    }

    private boolean filled(Object o){
        if (o instanceof String)
            return !((String)o).isEmpty();
        return o != null;
    }
}
