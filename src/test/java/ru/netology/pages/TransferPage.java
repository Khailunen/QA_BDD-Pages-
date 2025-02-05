package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.getSelectedText;

public class TransferPage {
    private final SelenideElement heading = $("h1").shouldBe(Condition.text("Пополнение карты"));
    private final SelenideElement amountField = $("[data-test-id='amount'] input");
    private final SelenideElement fromField = $("[data-test-id='from'] input");
    private final SelenideElement buttonTransfer = $("[data-test-id='action-transfer']");
    private final SelenideElement buttonCancel = $("[data-test-id='action-cancel']");
    private final SelenideElement errorMassage = $("[data-test-id='error-notification']");

    /**(Данные вынесены в класс DataHelper) методами пополняется карта валидными данными, пополняется карта невалидными данными, отображается сообщение об ошибке
     * заполнить поле ввода сумма
     * заполнить поле ввода номер карты
     * нажать кнопку "пополнить"
     * ОТКРЫВАЕТСЯ СТРАНИЦА СО СПИСКОМ ДОСТУПНЫХ БАНКОВСКИХ КАРТ и С ИЗМЕНЕННЫМ БАЛАНСОМ (возввращается на DashboardPage)
     **/
    public TransferPage() {
        heading.shouldBe(Condition.visible);
    }
    public DashboardPage validTransferMoney (String amount, DataHelper.CardInfo cardInfo) {
        transferMoney(amount, cardInfo);
        return new DashboardPage();
    }
    public void transferMoney (String amount, DataHelper.CardInfo cardInfo) {
        amountField.setValue(amount);
        fromField.setValue(cardInfo.getCardNumber());
        buttonTransfer.click();
    }

    public void errorMassage (String textError) {
        errorMassage.shouldBe(Condition.text(textError)).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }
}
