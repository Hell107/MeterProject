package com.university.semenenko.meterproject.Db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.university.semenenko.meterproject.Entity.Payment;
import com.university.semenenko.meterproject.Entity.Person;

public class DataBaseHelper {

    // тут все связано с бд, если этот класс будет валится ошибками, то скоре всего тебе надо
    // скачать mysql и создать там какое нибудь соеденение

    private static final String DATABASE_NAME = "meter";

    private static final String USERNAME = "root";
    private static final String PASSWORD = "Passw0rd";

    private static final DataSource DATA_SOURCE = getDataSource();

    // тут запускается скрипт инициализации
    private static final boolean SCRIPT_IS_RUN = runInitScript();


    private static final String SQL_INSERT_PERSON = "INSERT INTO person (name, city) VALUES (?, ?)";

    private static final String SQL_INSERT_PAYMENT =
            "INSERT INTO payment (amount, payment_date, month_of_calculation, person_id) VALUES (?, ?, ?, ?)";

    private static final String SQL_SELECT_PERSON_ID = "SELECT person_id FROM person WHERE name = ?;";

    private static final String SQL_SELECT_PERSON_AND_PAYMENT =
            "SELECT amount, payment, month_of_calculation, person.name, person.city FROM payment "
                    + "JOIN person USING (person_id) WHERE person.name = ? "
                    + " ORDER BY payment.payment_id DESC LIMIT 1;";

    // вставка пользователя в бд если его там нету
    public void sqlInsertPersonIfPersonNotFound(Person person) {
        try (PreparedStatement statement = getConnection().prepareStatement(SQL_INSERT_PERSON)) {
            statement.setString(1, person.getPersonName());
            statement.setString(2, person.getPersonCity());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // вставка инфы об оплате в бд
    public void sqlInsertPayment(Payment payment) {
        try (PreparedStatement statement = getConnection().prepareStatement(SQL_INSERT_PAYMENT)) {
            statement.setInt(1, payment.getAmount());
            statement.setDate(2, payment.getPaymentDate());
            statement.setInt(3, payment.getMonthOfCalculation());
            statement.setLong(4, payment.getPersonId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // возвращает id пользователя по имени
    public Long sqlSelectPersonIdByPersonName(String personName) {
        try (PreparedStatement statement = getConnection().prepareStatement(SQL_SELECT_PERSON_ID)) {
            statement.setString(1, personName);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            } else
                return rs.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //это для связи с бд
    private static DataSource getDataSource() {
        try {
            final MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setDatabaseName(DATABASE_NAME);
            dataSource.setUser(USERNAME);
            dataSource.setPassword(PASSWORD);
            dataSource.setPort(3306);
            dataSource.setUseSSL(false);
            return dataSource;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //это возвращает соеденение
    static Connection getConnection() {
        try {
            return DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    static boolean runInitScript() {
        try {
            ScriptRunner sr = new ScriptRunner(DATA_SOURCE.getConnection());
            Reader reader = new BufferedReader(
                    new FileReader("src/main/resources/com/university/semenenko/meterproject/init.sql"));
            sr.runScript(reader);
            return true;
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
