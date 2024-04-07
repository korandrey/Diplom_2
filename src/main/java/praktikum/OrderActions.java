package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderActions extends Rest {
    private static final String ORDER_PATH = "/api/orders";


    @Step("Создать заказ неавторизованным пользователем")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Создать заказ авторизованным пользователем")
    public ValidatableResponse create(Order order, String accessToken) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получить заказ")
    public ValidatableResponse get(String accessToken) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
                .when()
                .get(ORDER_PATH)
                .then();
    }
}
