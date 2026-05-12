package org.spark.service.exception;

public class RESTSQLWorkflowException extends RuntimeException {
    public RESTSQLWorkflowException(String message) {
        super(message);
    }

    public RESTSQLWorkflowException(String message, Throwable cause) {
        super(message, cause);
    }
}
