package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import dev.failsafe.internal.util.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;
import ru.netology.pages.VerificationPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;

public class MoneyTransferTest {
    DashboardPage dashboardPage;
    CardInfo firstCardInfo;
    CardInfo secondCardInfo;
    int firstCardBalance;
    int secondCardBalance;


    @BeforeEach
    void setup (){
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var info = getAuthInfo(); /*переменная с данными юзера*/
//        var LoginPage = new LoginPage(); /*создаем экземпляр класса логина и положили в переменную*/
        var verificationCode = DataHelper.getVerificationCodeFor(info); /*переменная с смс кодом*/
        var verificationPage = loginPage.validLogin(info); /*доменный метод для логина*/
        dashboardPage = verificationPage.validVerify(verificationCode); /*доменный метод для верификации*/
        firstCardInfo = getFirstCardInfo(); /*пременная 1ая карта*/
        secondCardInfo = getSecondCardInfo();/*пременная 2ая карта*/
        firstCardBalance = dashboardPage.getCardBalance(firstCardInfo); /*переменная с балансом первой карты до перевода*/
        secondCardBalance = dashboardPage.getCardBalance(secondCardInfo); /*переменная с балансом второй карты до перевода*/
    }

    @Test
    void shouldTransferMoneyFromSecondToFirst () {
        var amount = DataHelper.generateValidAmount(secondCardBalance);/*переменная суммы перевода (баланс/n) */
        var modifiedBalanceFirstCard = firstCardBalance + amount; /*переменная балас после пополнения на 1 карте*/
        var modifiedBalanceSecondCard = secondCardBalance - amount; /*переменная балас после списания на 2 карте*/
        var transferPage = dashboardPage.selectTransferCard(firstCardInfo);/*доменный метод выбора карты для пополнения*/
        dashboardPage = transferPage.validTransferMoney(String.valueOf(amount), secondCardInfo);/*доменный метод пополнения выбранной карты (сумма, карта списания)*/
        dashboardPage.reloadButton();/*обновились*/
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo); /*баланс после пополнения 1й карты*/
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo); /*баланс после перевода со 2й карты*/

        assertAll(() -> assertEquals(modifiedBalanceFirstCard, actualFirstCardBalance),//   вместо: Assertions.assertEquals(modifiedBalanceFirstCard,actualFirstCardBalance);
                () -> assertEquals(modifiedBalanceSecondCard, actualSecondCardBalance));//        Assertions.assertEquals(modifiedBalanceSecondCard,actualSecondCardBalance);
    }


    @Test
    void shouldBeErrorMassageIfAmountMoreThanBalanceFromSecondToFirst () {
        var amount = DataHelper.generateInvalidAmount(secondCardBalance);/*переменная суммы перевода с карты (баланс карты списания/n) */
        var modifiedBalanceFirstCard = firstCardBalance + amount; /*переменная балас после пополнения на 1 карте*/
        var modifiedBalanceSecondCard = secondCardBalance - amount; /*переменная балас после списания на 2 карте*/
        var transferPage = dashboardPage.selectTransferCard(firstCardInfo);/*доменный метод выбора карты для пополнения*/
        transferPage.transferMoney(String.valueOf(amount), secondCardInfo);/*доменный метод пополнения выбранной карты (сумма, карта списания)*/
        dashboardPage.reloadButton();/*обновились*/
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo); /*баланс после пополнения 1й карты*/
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo); /*баланс после перевода со 2й карты*/

        assertAll(() -> transferPage.errorMassage("Не хватает денег для перевода. " +
                        "Уменьшите сумму или переведите с другой карты или счёта"),
                () -> assertEquals(firstCardBalance, dashboardPage.getCardBalance(firstCardInfo)),
                () -> assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCardInfo)));
        /**перевод выполняется с суммой списания больше, чем остаток на карте (баланс уходт в минус)сообщение об ошибке не появляется**/
    }

    @Test
    void shouldBeErrorMassageIfAmountMoreThanBalanceFromFirstToSecond () {
        var amount = DataHelper.generateInvalidAmount(firstCardBalance);/*переменная суммы перевода "откуда" (баланс карты списания/n) */
        var modifiedBalanceFirstCard = secondCardBalance + amount; /*переменная балас после пополнения на 2 карте*/
        var modifiedBalanceSecondCard = firstCardBalance - amount; /*переменная балас после списания на 1 карте*/
        var transferPage = dashboardPage.selectTransferCard(secondCardInfo);/*доменный метод выбора карты для пополнения*/
        transferPage.transferMoney(String.valueOf(amount), secondCardInfo);/*доменный метод пополнения выбранной карты (сумма, карта списания)*/
        dashboardPage.reloadButton();/*обновились*/
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo); /*баланс после пополнения 1й карты*/
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo); /*баланс после перевода со 2й карты*/

        assertAll(() -> transferPage.errorMassage("Не хватает денег для перевода. " +
                        "Уменьшите сумму или переведите с другой карты или счёта"),
                () -> assertEquals(firstCardBalance, dashboardPage.getCardBalance(firstCardInfo)),
                () -> assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCardInfo)));

        /**сообщение об ошибке не появляется**/
    }
}
