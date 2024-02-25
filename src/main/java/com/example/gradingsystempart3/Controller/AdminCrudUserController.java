package com.example.gradingsystempart3.Controller;

import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Service.CourseService;
import com.example.gradingsystempart3.Service.SectionService;
import com.example.gradingsystempart3.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.gradingsystempart3.Util.PasswordAuthenticator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/crud/user")
public class AdminCrudUserController {
    private final UserService userService;
    private int currentUserId;
    @Autowired
    public AdminCrudUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public String addUser(@RequestParam("current_user_id") int currentUserId,
                             @RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("first_name") String firstName,
                          @RequestParam("last_name") String lastName,
                          @RequestParam(value = "role_id",required = false) Integer roleId){
        this.currentUserId = currentUserId;
        if(filled(username) && filled(password) && filled(firstName) && filled(lastName) && filled(roleId)){
            if(userService.getIdByUsername(username) == -1){
                if(roleId == 2){
                    userService.addInstructor(username,password,firstName,lastName);
                    return redirectSuccess("Instructor added successfully");
                }else if (roleId == 3){
                    userService.addStudent(username,password,firstName,lastName);
                    return redirectSuccess("Student added successfully");
                }else {
                    return redirectError("Invalid role ID , enter 2 or 3");
                }
            }else{
                return redirectError("A user exists with this username");
            }
        }else {
            return redirectError("Please enter all necessary info");
        }
    }
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("current_user_id") int currentUserId,
                                @RequestParam(value = "user_id",required = false) Integer userId){
        this.currentUserId = currentUserId;
        if(filled(userId)){
            if(UserService.userExists(userId)) {
                userService.deleteUser(userId);
                return redirectSuccess("User deleted successfully");
            }else {
                return redirectError("User does not exist");
            }
        }else {
            return redirectError("Please enter the user ID");
        }
    }
    @PostMapping("/update")
    public String updateUser(@RequestParam("current_user_id") int currentUserId,
                             @RequestParam("user_id") int userId,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("first_name") String firstName,
                             @RequestParam("last_name") String lastName,
                             @RequestParam(value = "role_id",required = false) Integer roleId){
        this.currentUserId = currentUserId;

        Database database = Database.getInstance();
        Map<String,Object> columns = new HashMap<>();
        columns.put("user_id",userId);
        columns.put("username",username);
        columns.put("password",password);
        columns.put("first_name",firstName);
        columns.put("last_name",lastName);
        columns.put("role_id",roleId);

        if(filled(userId)){
            if(UserService.userExists(userId)) {
                boolean updated = false;
                for (String column : columns.keySet()){
                    if(!column.equals("user_id")) {
                        Object newValue = columns.get(column);
                        if (filled(newValue)) {
                            if (column.equals("password")){
                                newValue = PasswordAuthenticator.hashPassword((String) newValue);
                            }
                            if (column.equals("username") && userService.getIdByUsername(username) != -1){
                                return redirectError("A user exists with this username");
                            }
                            database.updateRecord("user", column, newValue, userId);
                            updated = true;
                        }
                    }
                }
                if (updated){
                    return redirectSuccess("User updated successfully");
                }else
                    return redirectSuccess("No changes were made.");
            }else {
                return redirectError("User does not exist");
            }
        }else {
            return redirectError("Please enter the user ID");
        }
    }
    private String redirectSuccess(String msg){
        return "redirect:/admin/crud_view?table=user&current_user_id="+currentUserId+"&success="+msg;
    }
    private String redirectError(String msg){
        return "redirect:/admin/crud_view?table=user&current_user_id="+currentUserId+"&error="+msg;
    }

    private boolean filled(Object o){
        if (o instanceof String)
            return !((String)o).isEmpty();
        return o != null;
    }
}
