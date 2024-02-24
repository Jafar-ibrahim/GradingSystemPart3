package com.example.gradingsystempart3.DAO;

import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Model.Role;

import java.sql.SQLException;
import java.util.List;

public class RoleDAO {

    private static final Database database = Database.getInstance();
    private static final String TABLE_NAME = "role";

    public boolean insertRole(int roleId , String roleName) throws SQLException {
        return database.insertRecord("role",roleId,roleName);
    }

    public boolean deleteRole(int roleId) throws SQLException {
        return database.deleteRecord(TABLE_NAME,roleId);
    }
    public boolean updateName(int roleId, String newName){
        return database.updateRecord(TABLE_NAME,"name",newName,roleId);
    }
    public Role getById(int roleId) throws SQLException {
        if(roleId -1 <= Role.values().length)
            return Role.values()[roleId-1];
        throw new IllegalArgumentException();
    }
    public List<String> getColumnsNames(){
        return database.getTableColumnsNames(TABLE_NAME);
    }

}
