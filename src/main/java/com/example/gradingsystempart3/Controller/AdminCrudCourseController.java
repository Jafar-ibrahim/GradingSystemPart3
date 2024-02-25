package com.example.gradingsystempart3.Controller;

import com.example.gradingsystempart3.Exceptions.CourseNotFoundException;
import com.example.gradingsystempart3.Service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/crud/course")
public class AdminCrudCourseController {
    private final CourseService courseService;
    private int currentUserId;

    public AdminCrudCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/add")
    public String addCourse(@RequestParam("current_user_id") int currentUserId,
                            @RequestParam("course_name") String courseName){
        this.currentUserId = currentUserId;
        if(filled(courseName)){
            courseService.addCourse(courseName);
            return redirectSuccess("Course added successfully");
        }else {
            return redirectError("Please enter all necessary info");
        }
    }
    @PostMapping("/delete")
    public String deleteCourse(@RequestParam("current_user_id") int currentUserId,
                               @RequestParam(value = "course_id" ,required = false) Integer courseId){
        this.currentUserId = currentUserId;
        if(filled(courseId)){
            try {
                courseService.deleteCourse(courseId);
            } catch (CourseNotFoundException e) {
                return redirectError("No course is found with the given id");
            }
            return redirectSuccess("Course deleted successfully");
        }else {
            return redirectError("Please enter all necessary info");
        }
    }
    @PostMapping("/update")
    public String updateCourse(@RequestParam("current_user_id") int currentUserId,
                               @RequestParam(value = "course_id" , required = false) Integer courseId,
                               @RequestParam("course_name") String courseName){
        this.currentUserId = currentUserId;
        if(filled(courseName) && filled(courseId)){
            try {
                courseService.updateCourseName(courseId,courseName);
            } catch (CourseNotFoundException e) {
                return redirectError("No course is found with the given id");
            }
            return redirectSuccess("Course updated successfully");
        }else {
            return redirectError("Please enter all necessary info");
        }
    }
    private String redirectSuccess(String msg){
        return "redirect:/admin/crud_view?table=course&current_user_id="+currentUserId+"&success="+msg;
    }
    private String redirectError(String msg){
        return "redirect:/admin/crud_view?table=course&current_user_id="+currentUserId+"&error="+msg;
    }

    private boolean filled(Object o){
        if (o instanceof String)
            return !((String)o).isEmpty();
        return o != null;
    }
}
