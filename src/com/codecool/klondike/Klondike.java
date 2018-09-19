package com.codecool.klondike;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.IOException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.net.URL;

public class Klondike extends Application {

    private static double WINDOW_WIDTH = 1400;
    private static double WINDOW_HEIGHT = 900;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) 
    throws IOException{
        
        setWindowSize();
        FirstWindow window = new FirstWindow(primaryStage, WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setTableBackground(new Image("/table/gwent.jpg"));
        primaryStage.setTitle("Klondike Solitaire");
        primaryStage.setScene(new Scene(window, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
 

        
    }

    private void setWindowSize() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Klondike.WINDOW_WIDTH = gd.getDisplayMode().getWidth();
        Klondike.WINDOW_HEIGHT = gd.getDisplayMode().getHeight();
    }


}
