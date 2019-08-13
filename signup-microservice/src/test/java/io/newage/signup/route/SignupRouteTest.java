package io.newage.signup.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.newage.signup.SignupApp;
import io.newage.signup.model.ErrorConstant;
import io.newage.signup.model.ErrorInfo;
import io.newage.signup.model.SignupRequest;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignupApp.class)
public class SignupRouteTest {

    private static final String SIGNUP_URI = "http://localhost:8080/api/signup";
    private static final String TEST_EMAIL = "test@com.ua";
    private static final String TEST_PASSWORD = "123456";

    @Autowired
    private ObjectMapper objectMapper;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka =
            new EmbeddedKafkaRule(1, true, "signup");

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testSignupRouteOk() throws JsonProcessingException {

        ResponseEntity response = invokeSignup(SignupRequest.builder()
                        .email(TEST_EMAIL)
                        .password(TEST_PASSWORD)
                        .build()
                , String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testSignupRouteBadRequest() throws JsonProcessingException {

        ResponseEntity<ErrorInfo> response = invokeSignup(SignupRequest.builder().password(TEST_PASSWORD).build(), ErrorInfo.class);

        validateResponseBadRequest(response);

        response = invokeSignup(SignupRequest.builder().email(TEST_EMAIL).build(), ErrorInfo.class);

        validateResponseBadRequest(response);

        response = invokeSignup(SignupRequest.builder().build(), ErrorInfo.class);

        validateResponseBadRequest(response);
    }

    private <T> ResponseEntity<T> invokeSignup(SignupRequest signupRequest, Class<T> clazz) throws JsonProcessingException {
        return restTemplate.postForEntity(SIGNUP_URI, objectMapper.writeValueAsString(signupRequest), clazz);
    }

    private void validateResponseBadRequest(ResponseEntity<ErrorInfo> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(ErrorConstant.ERR_VALIDATION.getCode());
        assertThat(response.getBody().getErrorId()).isEqualTo(ErrorConstant.ERR_VALIDATION.getErrorId());
        assertThat(response.getBody().getMessage()).isEqualTo(ErrorConstant.ERR_VALIDATION.getMessage());
        assertThat(response.getBody().getVariables()).isNotEmpty();
    }

}