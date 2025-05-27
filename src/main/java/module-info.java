module com.dmsproject.dijkstravisualizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jgrapht.core;
    requires javafx.graphics;

    opens com.dmsproject.dijkstravisualizer to javafx.fxml;
    exports com.dmsproject.dijkstravisualizer;
}