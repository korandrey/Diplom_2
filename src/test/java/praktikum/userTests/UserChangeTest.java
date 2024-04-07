package praktikum.userTests;


import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.DataGeneration;
import praktikum.User;
import praktikum.UserActions;

@RunWith(Parameterized.class)
public class UserChangeTest {
    private UserActions userActions;
    private final String email;
    private final String password;
    private final String name;
    private static final User user;
    private String token;

    static {
        user = DataGeneration.generatingDataToCreateValidUser();
    }

    @Before
    public void createTestData() {
        userActions = new UserActions();
    }

    @After
    public void cleanup() {
        userActions.delete(token).statusCode(202);
    }

    public UserChangeTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] values() {
        return new Object[][]{
                {"testovyimail@mail.ru", user.getPassword(), user.getName()},
                {user.getEmail(), "testoviyparol", user.getName()},
                {user.getEmail(), user.getPassword(), "testovoeimya"},
        };
    }

    @Test
    @DisplayName("Изменение данных пользователя авторизованным пользователем")
    public void changingUserDataByAnAuthorizedUser() {
        //создаем юзера
        ValidatableResponse response = userActions.create(user);
        response.assertThat().statusCode(200);
        //записываем измененного юзера
        User changeUser = new User(email, password, name);
        //выдергиваем токен из ответа
        token = response.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        //меняем данные у юзера
        ValidatableResponse responseChange = userActions.change(changeUser, token);
        responseChange.assertThat().statusCode(200);
        //проверяем, что измененный юзер может залогиниться, а занчит изменился
        ValidatableResponse responseAuth = userActions.login(changeUser);
        responseAuth.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Изменение данных пользователя неавторизованным пользователем")
    public void changingUserDataByAnUnauthorizedUser() {
        //создаем юзера
        ValidatableResponse response = userActions.create(user);
        response.assertThat().statusCode(200);
        //записываем измененного юзера
        User changeUser = new User(email, password, name);
        //выдергиваем токен из ответа для уделения юзера после автотеста
        token = response.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        //меняем данные у юзера без передачи токена
        ValidatableResponse responseChange = userActions.change(changeUser, "");
        responseChange.assertThat().statusCode(401);
    }
}
