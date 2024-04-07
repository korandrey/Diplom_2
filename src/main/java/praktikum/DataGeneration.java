package praktikum;

import com.github.javafaker.Faker;

public class DataGeneration {

    public static User generatingDataToCreateValidUser() {
        Faker faker = new Faker();
        final String email = faker.internet().emailAddress();
        final String password = faker.internet().password(6, 9);
        final String name = faker.name().firstName();
        return new User(email, password, name);
    }

}
