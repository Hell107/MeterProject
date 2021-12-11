package com.university.semenenko.meterproject.Controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import com.university.semenenko.meterproject.Entity.Meter;
import com.university.semenenko.meterproject.Entity.Payment;
import com.university.semenenko.meterproject.Entity.Person;
import com.university.semenenko.meterproject.Db.DataBaseHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class MeterController {

    // тут инициализация всех связующих с формой объектов (связь с meterForm.fxml)
    @FXML
    private Label nnLabel1;

    @FXML
    private Label nnLabelText;

    @FXML
    private Button buttonPay;

    @FXML
    private Button buttonGetPayment;

    @FXML
    private TextField city;

    @FXML
    private Label labelNumberOfPayment;

    @FXML
    private Label labelPayment;

    @FXML
    private TextField meterAfter1;

    @FXML
    private TextField meterAfter2;

    @FXML
    private Label labelDateText;

    @FXML
    private Label labelMonthText;

    @FXML
    private TextField meterBefore1;

    @FXML
    private TextField meterBefore2;

    @FXML
    private TextField name;

    @FXML
    private Label dateNow;

    @FXML
    private Label failedInputMeterNumLabel;

    @FXML
    private Label fioLabel;

    @FXML
    private Label fioLabelText;

    @FXML
    private Label successfulPaymentLabel;

    @FXML
    private Label month;

    DataBaseHelper db;

    @FXML
    void initialize() {

        //инициализация базы данных
        db = new DataBaseHelper();

        //иницилизируем person и payment (AtomicReference для целостности данных)
        final AtomicReference<Person> person = new AtomicReference<>();
        final AtomicReference<Payment> payment = new AtomicReference<>();

        //ограничение на ввод в поля показаний счетчика (только цифры)
        meterBefore1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                meterBefore1.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        meterAfter2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                meterAfter2.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        meterBefore2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                meterBefore2.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        meterAfter1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                meterAfter1.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // обработка нажатия на кнопку Получить счет
        buttonGetPayment.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            // показания берем из полей и проверяем являются ли они корректными
            if (checkValidMeterNum(Integer.parseInt(meterBefore1.getText()), Integer.parseInt(meterAfter1.getText()),
                    Integer.parseInt(meterBefore2.getText()), Integer.parseInt(meterAfter2.getText()))) {

                // очистка всяких ворнинг полей
                successfulPaymentLabel.setOpacity(0);
                failedInputMeterNumLabel.setOpacity(0);

                // создаются 2 экземпляра счетчика с введенными данными
                Meter meter1 =
                        new Meter(1, Integer.parseInt(meterBefore1.getText()), Integer.parseInt(meterAfter1.getText()));
                Meter meter2 =
                        new Meter(2, Integer.parseInt(meterBefore2.getText()), Integer.parseInt(meterAfter2.getText()));

                // тут берем нынешнюю дату и месяц прошлый, можно было бы проверять оплачено/не оплачено за месяц
                // но вроде задание не про обращение к бд, так что тут оставила без проверки
                Date date = Date.valueOf(getNowDate());
                int monthOfCalc = getNowMonth();

                // использование паттерна builder
                person.set(Person.newBuilder().setPersonName(name.getText()).setPersonCity(city.getText()).build());

                // использование паттерна expert
                payment.set(new Payment(date, monthOfCalc, getPersonIdFromDBOrAddIn(person.get()), meter1, meter2));

                // показывается поле с инфой об оплате
                showPayPanel(payment.get(), person.get());
            } else {
                // если неверно введены данные счетчиков, они стираются и появляется инфа красными буквами
                successfulPaymentLabel.setOpacity(0);
                failedInputMeterNumLabel.setOpacity(100);
                showAndClearPaymentPanel();
            }

        });
        // обработка нажатия на кнопку Оплатить
        buttonPay.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            // в базу данных вставляется инфа об оплате
            db.sqlInsertPayment(payment.get());
            // возвращаемся обратно на ввод  и выводим сообщение про успешную оплату
            successfulPaymentLabel.setOpacity(100);
            showAndClearPaymentPanel();
        });

    }

    //тут дальше всякие методы смотри по названию

    public boolean checkValidMeterNum(int meterBefore1, int meterAfter1, int meterBefore2, int meterAfter2) {
        return meterAfter1 > meterBefore1 && meterAfter2 > meterBefore2;
    }

    public Long getPersonIdFromDBOrAddIn(Person person) {
        Long personId = db.sqlSelectPersonIdByPersonName(person.getPersonName());
        if (personId == null) {
            db.sqlInsertPersonIfPersonNotFound(person);
            personId = db.sqlSelectPersonIdByPersonName(person.getPersonName());
        }
        return personId;
    }

    public void showAndClearPaymentPanel() {
        labelPayment.setOpacity(0);
        labelNumberOfPayment.setOpacity(0);
        labelDateText.setOpacity(0);
        labelMonthText.setOpacity(0);
        month.setOpacity(0);
        dateNow.setOpacity(0);
        fioLabel.setOpacity(0);
        fioLabelText.setOpacity(0);
        nnLabel1.setOpacity(0);
        nnLabelText.setOpacity(0);
        buttonPay.setOpacity(0);
        buttonGetPayment.setOpacity(100);
        city.setText("");
        name.setText("");
        meterBefore1.setText("");
        meterBefore2.setText("");
        meterAfter1.setText("");
        meterAfter2.setText("");
        meterAfter2.setText("");
    }

    public void showPayPanel(Payment payment, Person person) {
        fioLabel.setText(person.getPersonName());
        labelNumberOfPayment.setText(payment.getAmount() + " p.");
        nnLabel1.setText(UUID.randomUUID().toString().substring(0, 10));
        labelPayment.setOpacity(100);
        labelNumberOfPayment.setOpacity(100);
        labelDateText.setOpacity(100);
        labelMonthText.setOpacity(100);
        month.setOpacity(100);
        dateNow.setOpacity(100);
        fioLabel.setOpacity(100);
        fioLabelText.setOpacity(100);
        nnLabel1.setOpacity(100);
        nnLabelText.setOpacity(100);
        buttonPay.setOpacity(100);
        buttonGetPayment.setOpacity(0);
    }

    public LocalDate getNowDate() {
        LocalDate nowDate = LocalDate.now();
        dateNow.setText(nowDate.toString());
        return nowDate;
    }

    public int getNowMonth() {
        LocalDate nowDate = LocalDate.now();
        int monthOfCalc;
        int year;

        if (nowDate.getMonthValue() != 1) {
            monthOfCalc = LocalDate.now().getMonthValue() - 1;
            year = LocalDate.now().getYear();
        } else {
            monthOfCalc = 12;
            year = LocalDate.now().getYear() - 1;
        }

        month.setText(monthOfCalc + "-" + year);
        return monthOfCalc;
    }

}