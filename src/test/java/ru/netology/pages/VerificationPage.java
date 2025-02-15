package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeField = $("[data-test-id='code'] input");
    private final SelenideElement verifyButton = $("[data-test-id='action-verify']");

    /**(Данные вынесены в класс DataHelper)
     * заполнить поле ввода кода подтверждения
     * нажать кнопку отправки
     * ОТКРЫВАЕТСЯ СТРАНИЦА СО СПИСКОМ ДОСТУПНЫХ БАНКОВСКИХ КАРТ
     **/
    public VerificationPage () {
        codeField.shouldBe(Condition.visible);
    }

    public DashboardPage validVerify (DataHelper.VerificationCode verificationCode){
        codeField.setValue(verificationCode.getCode());
        verifyButton.click();
        return new DashboardPage();
    }
}
