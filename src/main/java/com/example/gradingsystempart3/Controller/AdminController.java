package com.example.gradingsystempart3.Controller;

import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller("/admin")
public class AdminController {

    private final UserService userService ;
    Database db = Database.getInstance();

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin_view")
    public String adminView(@RequestParam(name = "user_id", required = false) int userId , Model model){
        model.addAttribute("user_fullName",userService.getUserFullName(userId));
        return "admin_view";
    }
    @GetMapping("/crud_view")
    public String adminCrudView(@RequestParam(name = "table", required = false) String table,Model model){
        List<String> columns = db.getTableColumnsNames(table);
        List<String[]> content = db.getTableContent(table);
        model.addAttribute("columns",columns);
        model.addAttribute("content",content);
        model.addAttribute("table",table);
        model.addAttribute("tableCapitalized",table.substring(0, 1).toUpperCase() + table.substring(1));

        return "admin_crud_view";
    }
}
