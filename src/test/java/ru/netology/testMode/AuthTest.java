package ru.netology.testMode;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import ru.netology.Data.DataGenerator;
import ru.netology.Data.RegistrationInfo;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.Data.DataGenerator.Registration.*;


public class AuthTest {


    @Test
    void shouldLoginActiveUser() {
        //Активный пользователь
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("active");
        $x("//input[@type='text']").val(user.getLogin());
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $x("//*[contains(text(), 'Личный кабинет')]").should(Condition.visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldLoginBlockedUser() {
        //Заблокированный пользователь
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(user.getLogin());
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']").should(Condition.visible, Duration.ofSeconds(10));


    }

    @Test
    void shouldWrongLogin() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(getRandomLogin()); //Введен рандомный логин
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));


    }

    @Test
    void shouldWrongPassword() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(user.getLogin());
        $x("//input[@type='password']").val(getRandomPassword());  //Введен рандомный паспорт
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));


    }

    @Test
    void shouldNoPassword() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(user.getLogin());
        // воод паспорта отсутствует
        $x("//span[@class='button__text']").click();
        $("[data-test-id='password'].input_invalid .input__sub")
                .shouldHave(Condition.text("Поле обязательно для заполнения"), Duration.ofSeconds(10));


    }

    @Test
    void shouldNoLogin() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        //Ввод логина отсутствует
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $("[data-test-id='login'].input_invalid .input__sub")
                .shouldHave(Condition.text("Поле обязательно для заполнения"), Duration.ofSeconds(10));
    }
}
