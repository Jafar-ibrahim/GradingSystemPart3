package com.example.gradingsystempart3.Exceptions;

import java.sql.SQLException;

public class RecordNotFoundException extends SQLException {
    public RecordNotFoundException() {
        super("No record with the given (id)s is found , please try again with different input");
    }
}
