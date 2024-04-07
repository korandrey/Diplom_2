package praktikum;

import io.qameta.allure.Feature;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserCreateTest {
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
    @Feature("Создание и проверка валидного пользователя")
    public void userCanBeCreate() {
        user = DataGeneration.generatingDataToCreateValidUser();
        ValidatableResponse response = userActions.create(user);
        response.assertThat().statusCode(200);
        token = response.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
    }

    @Test
    @Feature("Создание двух одинаковых пользователей")
    public void duplicateUserCannotBeCreated() {
        user = DataGeneration.generatingDataToCreateValidUser();
        ValidatableResponse response = userActions.create(user);
        response.assertThat().statusCode(200);
        token = response.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        ValidatableResponse responseDuplicate = userActions.create(user);
        responseDuplicate.assertThat().statusCode(403);
    }
}
