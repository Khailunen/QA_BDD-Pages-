package ru.netology.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.remote.tracing.EventAttribute.setValue;

public class LoginPage {
    private final SelenideElement LoginField = $("[data-test-id='login'] input");
    private final SelenideElement PasswordField = $("[data-test-id='password'] input");
    private final SelenideElement loginButton = $("[data-test-id='action-login']");

    /**(Данные вынесены в класс DataHelper)
     * заполнить поле ввода логин
     * заполнить поле ввода пароль
     * нажать кнопку отправки
     * ОТКРЫВАЕТСЯ СТРАНИЦА С ПОЛЕМ ВВОДА КОДА ПОДТВЕРЖДАНИЯ
      **/
    public VerificationPage validLogin (DataHelper.AuthInfo info){
        LoginField.setValue(info.getLogin());
        PasswordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }
}
