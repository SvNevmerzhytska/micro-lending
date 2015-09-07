package ua.nevmerzhytska.resources;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.jersey.params.LongParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nevmerzhytska.model.LoanRequest;
import ua.nevmerzhytska.model.LoanResponse;
import ua.nevmerzhytska.services.LoanService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Service
@Path("/loans")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/loans", description = "Operations on loans")
public class LoanResource {

    @Autowired
    LoanService loanService;

    @POST
    @ApiOperation(value = "Create loan", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Loan was successfully created"),
            @ApiResponse(code = 400, message = "Invalid input JSON"),
            @ApiResponse(code = 422, message = "Entity cannot be processed according to logical constraints"),
            @ApiResponse(code = 500, message = "Internal service problem (lost DB connection, constraint violation etc.)")
    })
    public Response applyLoan(@ApiParam(value = "Requested loan", required = true) LoanRequest loan,
                              @Context HttpServletRequest req) {
        System.out.println(req.getRemoteAddr());
        return Response.ok().build();
    }

    @POST
    @Path("/{loanId}/extend")
    @ApiOperation(value = "Extend loan", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Given loan was extended"),
            @ApiResponse(code = 400, message = "Id is not integer"),
            @ApiResponse(code = 404, message = "Loan was not found in DB"),
            @ApiResponse(code = 422, message = "Entity cannot be processed according to logical constraints"),
            @ApiResponse(code = 500, message = "Internal service problem (lost DB connection, etc.)")
    })
    public Response extendLoan(@ApiParam(value = "Id of loan to extend", required = true) @PathParam("loanId")LongParam loanId,
                                  @Context HttpServletRequest req) {
        System.out.println(req.getRemoteAddr());
        return Response.ok().build();
    }

    @GET
    @ApiOperation(value = "List all loans", response = LoanResponse.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of loans was successfully retrieved"),
            @ApiResponse(code = 500, message = "Internal service problem (lost DB connection, etc.)")
    })
    public Response getLoans() {
        return Response.ok().build();
    }
}
