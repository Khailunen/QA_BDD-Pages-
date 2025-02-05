package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement header = $("[data-test-id=dashboard]");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private final SelenideElement buttonReload = $("[data-test-id='action-reload']");
    private ElementsCollection cards = $$(".list__item div");

    /**
     * (Данные вынесены в класс DataHelper) методами перебираются карты, извлекается баланс, выбирается карта, кот.будет пополняться
     * нажать кнопку "пополнить"
     * ОТКРЫВАЕТСЯ СТРАНИЦА С формой перевода денег с карты на карту
     **/
    public DashboardPage() {

        header.shouldBe(visible);
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        // TODO: перебрать все карты и найти по атрибуту data-test-id. получаем баланс карты id
        var text = cards.findBy(Condition.attribute("data-test-id", cardInfo.getCardId())).getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public TransferPage selectTransferCard(DataHelper.CardInfo cardInfo) {
        cards.findBy(Condition.attribute("data-test-id", cardInfo.getCardId())).$("button").click();
        return new TransferPage();
    }

    public DashboardPage reloadButton() {
        buttonReload.click();
        return new DashboardPage();
    }
}
