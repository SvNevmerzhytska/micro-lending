package ua.nevmerzhytska.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;

public class LoanNotFoundException extends WebApplicationException {

    public static String NOT_FOUND = "Requested loan with id {0} was not found";

    public LoanNotFoundException(String loanId) {
        super(Response.status(404).entity(MessageFormat.format(NOT_FOUND, loanId)).type("text/plain").build());
    }
}
