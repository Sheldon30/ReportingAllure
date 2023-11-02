package ru.netology.delivery.test;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {
    @BeforeAll
    static void setupAll(){
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll(){
        SelenideLogger.removeListener("allure");
    }

    private static Faker faker;
    @BeforeAll
    static void setUpAll() {
        faker = new Faker(new Locale("ru"));
    }


    @Test
    public void shouldDeliveryTest(){
        open("http://localhost:9999");
        var User = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeeting = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 10;
        var secondMeeting = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[placeholder='Город']").setValue(User.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeeting);
        $("[data-test-id='name'] input").setValue(User.getName());
        $("[name='phone']").setValue(User.getPhone());
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(Condition.exactText("Запланировать")).click();
        String planningDate1 = firstMeeting;
        String planningDate2 = secondMeeting;
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на  " + planningDate1), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondMeeting);
        $$("[type='button']").find(Condition.exactText("Запланировать")).click();
        $$(".button__text").find(Condition.exactText("Перепланировать")).click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на  " + planningDate2), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

}
