package ua.nevmerzhytska.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class HighRiskException extends WebApplicationException {

    public static String TOO_MANY_ATTEMPTS_PER_DAY = "There are too many requests from your device. Try again tomorrow.";
    public static String RISKY_REQUEST = "It is a risky request. Try again with smaller amount or in the morning";

    public HighRiskException(String reason) {
        super(Response.status(422).entity(reason).type("text/plain").build());
    }
}
