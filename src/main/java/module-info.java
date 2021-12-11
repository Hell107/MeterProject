module com.university.semenenko.meterproject {
    //тут всякие зависимости для приложения
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;

    requires javafx.graphics;
    requires lombok;
    requires java.sql;
    requires mysql.connector.java;
    requires mybatis;

    opens com.university.semenenko.meterproject to javafx.fxml;
    exports com.university.semenenko.meterproject;
    exports com.university.semenenko.meterproject.Controller;
    opens com.university.semenenko.meterproject.Controller to javafx.fxml;
}