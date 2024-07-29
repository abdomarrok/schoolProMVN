module com.marrok.schoolmanagermvn {
    requires javafx.controls;
    requires javafx.fxml;
    requires fontawesomefx;
    requires org.controlsfx.controls;
     requires org.kordamp.ikonli.fontawesome;
    requires java.logging;
    requires java.sql;
    requires com.jfoenix;
    requires javafx.base;
    requires com.dlsc.gemsfx;
    requires fr.brouillard.oss.cssfx;

    opens com.marrok.schoolmanagermvn to javafx.fxml;
    exports com.marrok.schoolmanagermvn;
    opens com.marrok.schoolmanagermvn.controllers to javafx.fxml;
    exports com.marrok.schoolmanagermvn.controllers;
    opens com.marrok.schoolmanagermvn.controllers.dashboard to javafx.fxml;
    exports com.marrok.schoolmanagermvn.controllers.dashboard;
    opens com.marrok.schoolmanagermvn.controllers.signin to javafx.fxml;
    exports com.marrok.schoolmanagermvn.controllers.signin;
    opens com.marrok.schoolmanagermvn.controllers.student to javafx.fxml;
    exports com.marrok.schoolmanagermvn.controllers.student;
    opens com.marrok.schoolmanagermvn.model to javafx.base;
}
