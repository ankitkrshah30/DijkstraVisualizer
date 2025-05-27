package com.dmsproject.dijkstravisualizer; // Ensure this package declaration is correct

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL; // Import URL

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlLocation = null;
        try {
            // Attempt 1: Absolute path from classpath root, matching your package structure
            fxmlLocation = getClass().getResource("/com/dmsproject/dijkstravisualizer/dijkstra-view.fxml");
            if (fxmlLocation != null) {
                System.out.println("DEBUG: FXML location found (Absolute Path): " + fxmlLocation);
            } else {
                System.err.println("DEBUG: FXML not found with Absolute Path: /com/dmsproject/dijkstravisualizer/dijkstra-view.fxml");

                // Attempt 2: Relative path (looks in the same package as MainApplication.class)
                // This assumes dijkstra-view.fxml is directly in src/main/resources/com/dmsproject/dijkstravisualizer/
                fxmlLocation = getClass().getResource("dijkstra-view.fxml");
                if (fxmlLocation != null) {
                    System.out.println("DEBUG: FXML location found (Relative Path): " + fxmlLocation);
                } else {
                    System.err.println("DEBUG: FXML not found with Relative Path: dijkstra-view.fxml (within current package)");
                }
            }

            if (fxmlLocation == null) {
                // If both attempts fail, throw a more specific error or exit gracefully
                throw new IllegalStateException(
                        "FATAL ERROR: Could not find dijkstra-view.fxml. " +
                                "Please ensure it's in src/main/resources/com/dmsproject/dijkstravisualizer/ " +
                                "and that module-info.java has 'opens com.dmsproject.dijkstravisualizer to javafx.fxml;'"
                );
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
            stage.setTitle("Dijkstra Visualizer");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.err.println("Error during application start: " + e.getMessage());
            e.printStackTrace();
            // Re-throw or handle as necessary
            throw e; // Propagate the exception for the launcher to catch
        }
    }

    public static void main(String[] args) {
        launch();
    }
}