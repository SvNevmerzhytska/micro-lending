package ua.nevmerzhytska.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import ua.nevmerzhytska.services.LoanService;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LoanResourceTest {
    private static String RESOURCE_PATH = "/loans";

    private static String PATH_TO_LOAN_REQUEST = "src/test/resources/json/apply_loan_request.json";
    private static String PATH_TO_INVALID_LOAN_REQUEST = "src/test/resources/json/apply_loan_request_invalid.json";

    @InjectMocks
    private LoanResource loanResource = new LoanResource();

    @Mock
    private LoanService loanService;

    @Spy
    private ObjectMapper objectMapper = Jackson.newObjectMapper();

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addResource(loanResource).build();

    @Before
    public void setUp() {
        objectMapper.registerModule(new JSR310Module());
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testApplyLoanOk() throws IOException {
        Response response = resources.getJerseyTest().target(RESOURCE_PATH).request()
                .post(Entity.json(objectMapper.readTree(new File(PATH_TO_LOAN_REQUEST))));

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(response.getEntity().toString(), not(isEmptyOrNullString()));
    }

    /*
    TODO: configure exception mapping as dropwizard does
    @Test(expected = ConstraintViolationException.class)
    public void testApplyLoanConstraintViolation() throws IOException {
        Response response = resources.getJerseyTest().target(RESOURCE_PATH).request()
                .post(Entity.json(objectMapper.readTree(new File(PATH_TO_INVALID_LOAN_REQUEST))));
    }*/
}
