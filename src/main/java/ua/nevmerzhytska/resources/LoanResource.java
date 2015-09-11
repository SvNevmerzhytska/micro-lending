package ua.nevmerzhytska.resources;

import com.wordnik.swagger.annotations.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nevmerzhytska.entities.Loan;
import ua.nevmerzhytska.model.LoanRequest;
import ua.nevmerzhytska.services.LoanService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Service
@Path("/loans")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
@Api(value = "/loans", description = "Operations on loans")
public class LoanResource {

    @Autowired
    LoanService loanService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create loan", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Loan was successfully created"),
            @ApiResponse(code = 400, message = "Invalid input JSON"),
            @ApiResponse(code = 422, message = "Entity cannot be processed according to logical constraints"),
            @ApiResponse(code = 500, message = "Internal service problem (lost DB connection, constraint violation etc.)")
    })
    public Response applyLoan(@ApiParam(value = "Requested loan", required = true) @Valid LoanRequest loan,
                              @Context HttpServletRequest req) {
        return Response.ok().entity(loanService.applyForLoan(req.getRemoteAddr(), loan)).build();
    }

    @POST
    @Path("/{loanId}/extend")
    @Consumes(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Extend loan", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Given loan was extended"),
            @ApiResponse(code = 400, message = "Id is not integer"),
            @ApiResponse(code = 404, message = "Loan was not found in DB"),
            @ApiResponse(code = 422, message = "Entity cannot be processed according to logical constraints"),
            @ApiResponse(code = 500, message = "Internal service problem (lost DB connection, etc.)")
    })
    public Response extendLoan(@ApiParam(value = "Id of loan to extend", required = true) @PathParam("loanId") @NotEmpty String loanId,
                                  @Context HttpServletRequest req) {
        return Response.ok(loanService.extendLoan(req.getRemoteAddr(), loanId)).build();
    }

    @GET
    @ApiOperation(value = "List all loans", response = Loan.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of loans was successfully retrieved"),
            @ApiResponse(code = 500, message = "Internal service problem (lost DB connection, etc.)")
    })
    public Response getLoans(@ApiParam(value = "User id") @QueryParam("userId") String userId) {
        return Response.ok().entity(loanService.getLoanHistory(userId)).build();
    }
}
