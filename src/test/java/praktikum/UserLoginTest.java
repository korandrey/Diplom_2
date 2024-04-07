package praktikum;

import io.qameta.allure.Feature;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserLoginTest {
    private UserActions userActions;
    private User user;
    private String token;

    @Before
    public void createTestData() {
        userActions = new UserActions();
        user = DataGeneration.generatingDataToCreateValidUser();
    }

    @After
    public void cleanup() {
        userActions.delete(token).statusCode(202);
    }

    @Test
    @Feature("Авторизация валидного пользователя")
    public void userAuthorization() {
        user = DataGeneration.generatingDataToCreateValidUser();
        ValidatableResponse response = userActions.create(user);
        response.assertThat().statusCode(200);
        token = response.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        ValidatableResponse responseLogin = userActions.login(user);
        responseLogin.assertThat().statusCode(200);
    }
}
