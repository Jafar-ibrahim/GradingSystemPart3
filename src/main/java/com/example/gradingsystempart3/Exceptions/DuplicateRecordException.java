package com.example.gradingsystempart3.Exceptions;

import java.sql.SQLException;

public class DuplicateRecordException extends SQLException {
    public DuplicateRecordException() {
        super("An entry with the specified attributes is already present");
    }
}
