package com.codecool.klondike;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.event.ActionEvent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.IOException;


import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class FirstWindow extends Pane{
    private Button changeCardsButton = new Button("Cards");
    private Button start = new Button("Start");
    private int j = 1;
    private int i = 1;
    private Stage primaryStage;
    private double WINDOW_WIDTH = 0;
    private double WINDOW_HEIGHT = 0;
    private Button changeTableButton = new Button("Table");
    Game game;



        FirstWindow(Stage primaryStage, double WINDOW_WIDTH, double WINDOW_HEIGHT){
            changeCardsButton();
            makeTableButton();
            Card.loadCardImages("card_images/card_back.png", "card_images/");


            this.primaryStage = primaryStage;
            startButton();
            this.WINDOW_WIDTH = WINDOW_WIDTH;
            this.WINDOW_HEIGHT = WINDOW_HEIGHT;


        }

        private void changeCardsButton(){
        changeCardsButton.setLayoutX(0);
        changeCardsButton.setLayoutY(0);
        getChildren().add(changeCardsButton);
        changeCardsButton.setOnAction(new EventHandler<ActionEvent>(){
            //anonymous class
            @Override
            public void handle(ActionEvent event){
                if (j == 0){
                    Card.loadCardImages("card_images/card_back.png", "card_images/");
                    System.out.println("j = 0");
                    j+=1;
                }else{
                    Card.loadCardImages("card_images2/card_back.png", "card_images2/");
                    System.out.println("j == 1");
                    j-=1;
                }

        }
        
        
    });}

        private void startButton(){
            start.setLayoutX(115);
            start.setLayoutY(0);
            getChildren().add(start);
                start.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event){
                        game = new Game();
                        game.setTableBackground(new Image("/table/green.png"));
                        if (i == 1){
                            game.setTableBackground(new Image("/table/green.png"));
                        }else if (i==2){
                            game.setTableBackground(new Image("/table/woodentable.jpg"));
                        }else if (i == 3){
                            game.setTableBackground(new Image("/table/mapa.png"));
                        }

                        primaryStage.setScene(new Scene(game, WINDOW_WIDTH, WINDOW_HEIGHT));
                    }
                });
            
        }


        private void makeTableButton(){
        

            changeTableButton.setLayoutX(60);
            changeTableButton.setLayoutY(0);
            getChildren().add(changeTableButton);
            changeTableButton.setOnAction(new EventHandler<ActionEvent>(){
                //anonymous class
                @Override
                public void handle(ActionEvent event){
                    if (i == 3){
                        setTableBackground(new Image("/table/green.png"));
                        i = 1;
                    }else if (i == 1){
                        setTableBackground(new Image("/table/woodentable.jpg"));
                        i = 2;
                    }else if(i == 2){
                        setTableBackground(new Image("/table/mapa.png"));
                        i = 3;
                    }
            }
        });
    
            
    
        }
        public void setTableBackground(Image tableBackground) {
            setBackground(new Background(new BackgroundImage(tableBackground,
                    BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        }


}