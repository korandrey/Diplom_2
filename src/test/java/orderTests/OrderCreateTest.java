package orderTests;


import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import praktikum.*;

public class OrderCreateTest {
    private OrderActions orderActions;
    private Order order;
    private UserActions userActions;
    private User user;


    @Before
    public void createTestData() {
        orderActions = new OrderActions();
        userActions = new UserActions();
        user = DataGeneration.generatingDataToCreateValidUser();
        order = DataGeneration.creatingValidOrder();
    }

    @Test
    @DisplayName("Создание валидного заказа без авторизации")
    public void orderCanBeCreateWithoutAuthorization() {
        ValidatableResponse response = orderActions.create(order);
        response.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Создание валидного заказа с авторизацией")
    public void orderCanBeCreateWithAuthorization() {
        //Создали пользователя
        ValidatableResponse responseCreateUser = userActions.create(user);
        responseCreateUser.assertThat().statusCode(200);
        //Запомнили токен
        String token;
        token = responseCreateUser.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        //Создали заказ, когда пользователь авторизован
        ValidatableResponse responseCreateOrder = orderActions.create(order,token);
        responseCreateOrder.assertThat().statusCode(200);
    }
}
