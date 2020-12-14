package com.mikason.PropView.Exception.databaseException;

public class DatabaseIsEmptyException extends RuntimeException {
    public DatabaseIsEmptyException() {
        super ("There is no record in the database now, database is empty.");
    }
}