package orderTests;


import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.*;

public class OrderGetTest {
    private OrderActions orderActions;
    private Order order;
    private UserActions userActions;
    private User user;
    private String token;

    @Before
    public void createTestData() {
        orderActions = new OrderActions();
        userActions = new UserActions();
        user = DataGeneration.generatingDataToCreateValidUser();
        order = DataGeneration.creatingValidOrder();
    }

    @After
    public void cleanup() {
        userActions.delete(token).statusCode(202);
    }

    @Test
    @DisplayName("Получить заказ, когда пользователь авторизован")
    public void getOrderAuthUser() {
        //Создали пользователя
        ValidatableResponse responseCreateUser = userActions.create(user);
        responseCreateUser.assertThat().statusCode(200);
        //Запомнили токен
        token = responseCreateUser.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        //Создали заказ, когда пользователь авторизован
        ValidatableResponse responseCreateOrder = orderActions.create(order,token);
        responseCreateOrder.assertThat().statusCode(200);
        //Проверяем, что можем получить заказ данного пользователя
        ValidatableResponse responseGetOrder = orderActions.get(token);
        responseGetOrder.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Получить заказ, когда пользователь неавторизован")
    public void getOrderUnauthorized() {
        //Создали пользователя
        ValidatableResponse responseCreateUser = userActions.create(user);
        responseCreateUser.assertThat().statusCode(200);
        //Запомнили токен
        token = responseCreateUser.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        //Создали заказ, когда пользователь авторизован
        ValidatableResponse responseCreateOrder = orderActions.create(order,token);
        responseCreateOrder.assertThat().statusCode(200);
        //Проверяем, что не можем получить заказ данного пользователя без авторизации
        ValidatableResponse responseGetOrder = orderActions.get("");
        responseGetOrder.assertThat().statusCode(401);
    }
}
