package com.codecool.klondike;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

import java.io.IOException;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.lang.Cloneable;

import com.codecool.klondike.Pile.PileType;

public class Game extends Pane{


    private Integer beforePileIndex = null;
    private Integer afterPileIndex = null;
    private Integer numberOfCards = null;
    


    private Button refillStockButton = new Button("Refill");
    private Button undoButton = new Button("Undo");

    private Pile stockPile;
    private Pile discardPile;


    private List<Pile> foundationPiles = FXCollections.observableArrayList();
    private List<Pile> tableauPiles = FXCollections.observableArrayList();
    private List<Card> deck = new ArrayList<>();
    private List<Card> draggedCards;

    private String pileListName = "";
    
    private double dragStartX, dragStartY;
    private static double STOCK_GAP = 1;
    private static double FOUNDATION_GAP = 0;
    private static double TABLEAU_GAP = 30;
    

    public Game(){
        deck = Card.createNewDeck();
        Collections.shuffle(deck);
        draggedCards = FXCollections.observableArrayList();
        undoChoiceButton();
        refillStockButton();
        initPiles();
        dealCards();
        
    }


    
    private void initPiles() {
        stockPile = new Pile(Pile.PileType.STOCK, "Stock", STOCK_GAP);
        stockPile.setBlurredBackground();
        stockPile.setLayoutX(95);
        stockPile.setLayoutY(20);
        stockPile.setOnMouseClicked(stockReverseCardsHandler);
        getChildren().add(stockPile);

        discardPile = new Pile(Pile.PileType.DISCARD, "Discard", STOCK_GAP);
        discardPile.setBlurredBackground();
        discardPile.setLayoutX(285);
        discardPile.setLayoutY(20);
        getChildren().add(discardPile);

        for (int i = 0; i < 4; i++) {
            Pile foundationPile = new Pile(Pile.PileType.FOUNDATION, "Foundation " + i, FOUNDATION_GAP);
            foundationPile.setBlurredBackground();
            foundationPile.setLayoutX(610 + i * 180); //610
            foundationPile.setLayoutY(20);
            foundationPiles.add(foundationPile);
            getChildren().add(foundationPile);
        }
        for (int i = 0; i < 7; i++) {
            Pile tableauPile = new Pile(Pile.PileType.TABLEAU, "Tableau " + i, TABLEAU_GAP);
            tableauPile.setBlurredBackground();
            tableauPile.setLayoutX(75 + i * 180);
            tableauPile.setLayoutY(295); //275
            tableauPiles.add(tableauPile);
            getChildren().add(tableauPile);
        }
    }


    public void dealCards() {
        Iterator<Card> deckIterator = deck.iterator();
        deckIterator.forEachRemaining(card -> {
            stockPile.addCard(card);
            addMouseEventHandlers(card);
            getChildren().add(card);
        });

        for(int times = 0; times < 7; times++){
            for(int j = 0; j < times + 1; j++){
                if(j == times){
                    stockPile.getCards().get(j).flip();
                }
                stockPile.getCards().get(j).moveToPile(tableauPiles.get(times));
                
                
            }
        }

    }


    public void setTableBackground(Image tableBackground) {
        setBackground(new Background(new BackgroundImage(tableBackground,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    }



    private EventHandler<MouseEvent> onMouseClickedHandler = e -> {

        Card card = (Card) e.getSource();
        if (e.getClickCount() == 1){
            if (card.getContainingPile().getPileType() == Pile.PileType.STOCK) {
                card.moveToPile(discardPile);
                card.flip();
                card.setMouseTransparent(false);
                System.out.println("Placed " + card + " to the waste.");
            }
        }else if(e.getClickCount() == 2){
            for(Pile foundation : foundationPiles){
                if(allowPushCardToFoundation(card, foundation)){ 
                    flipCard(card);
                    card.moveToPile(foundation);
                    System.out.println("Places " + card + " to the foundation");
                    
                    break;             

                }else{
                    System.out.println("Nothing happened");
                }
            }
        }
    };




    private EventHandler<MouseEvent> stockReverseCardsHandler = e -> {
        refillStockFromDiscard();
    };

    private EventHandler<MouseEvent> onMousePressedHandler = e -> {
        dragStartX = e.getSceneX();
        dragStartY = e.getSceneY();
    };

    private EventHandler<MouseEvent> onMouseDraggedHandler = e -> {
            numberOfCards = 0;
            Card card = (Card) e.getSource();
            Pile tempPile = card.getContainingPile();
            if(tableauPiles.contains(tempPile)){
                beforePileIndex = tableauPiles.indexOf(tempPile);
                pileListName = "tableauPiles";
            }else if(stockPile == tempPile){
                pileListName = "stockPile";
            }else if(discardPile == tempPile){
                pileListName = "discardPile";
            }
            
        if(card == card.getContainingPile().getTopCard()){
            numberOfCards += 1;
            Pile activePile = card.getContainingPile();
            if (activePile.getPileType() == Pile.PileType.STOCK)
                return;
            
            double offsetX = e.getSceneX() - dragStartX;
            double offsetY = e.getSceneY() - dragStartY;
            

            draggedCards.clear();
            draggedCards.add(card);
    
            card.getDropShadow().setRadius(20);
            card.getDropShadow().setOffsetX(10);
            card.getDropShadow().setOffsetY(10);
    
            card.toFront();
            card.setTranslateX(offsetX);
            card.setTranslateY(offsetY);

        }else if(!card.getFaceDown()){
            Pile activePile = card.getContainingPile();
            if (activePile.getPileType() == Pile.PileType.STOCK)
                return;
            double offsetX = e.getSceneX() - dragStartX;
            double offsetY = e.getSceneY() - dragStartY; 
            draggedCards.clear();
            for(int f = activePile.getCards().indexOf(card); f < activePile.getCards().size(); f++){
                draggedCards.add(activePile.getCards().get(f));
            }
            for(Card cardDragg : draggedCards){
                numberOfCards += 1;
                cardDragg.getDropShadow().setRadius(20);
                cardDragg.getDropShadow().setOffsetX(10);
                cardDragg.getDropShadow().setOffsetY(10);
                cardDragg.toFront();
                cardDragg.setTranslateX(offsetX);
                cardDragg.setTranslateY(offsetY);
            }

        }
        
    };

    private EventHandler<MouseEvent> onMouseReleasedHandler = e -> {


        if (draggedCards.isEmpty()){
            return;
        }
            
        Card card = (Card) e.getSource();
        
        
        Pile pile = getValidIntersectingPile(card, tableauPiles);
        if(pile == null){
            draggedCards.forEach(MouseUtil::slideBack);
            draggedCards.remove(card);
            return;
        }

        if (pile != null) {
            if(isMoveValid(draggedCards.get(0), pile)){
                handleValidMove(draggedCards, pile);
                afterPileIndex = tableauPiles.indexOf(pile);
                flipCard(card);
            }
            
        } 
    };


    public void addMouseEventHandlers(Card card) {
        card.setOnMousePressed(onMousePressedHandler);
        card.setOnMouseDragged(onMouseDraggedHandler);
        
        card.setOnMouseReleased(onMouseReleasedHandler);


        card.setOnMouseClicked(onMouseClickedHandler);
    }


    public boolean isMoveValid(Card card, Pile destPile) {
        if(destPile.getCards().size() > 0){
            if (Card.isOppositeColor(card, destPile.getTopCard()) && (destPile.getTopCard().getRank().getRankInt() == (card.getRank().getRankInt() + 1))){
                return true;
            }else{
                    return false;
                   
            } 
            }else if(destPile.isEmpty() && card.getRank().getRankInt() == 13){
                return true;
            }else{
                return false;
            }
    
        }


    private Pile getValidIntersectingPile(Card card, List<Pile> piles) {
        Pile result = null;
        for (Pile pile : piles) {
            if (!pile.equals(card.getContainingPile()) &&
                    isOverPile(card, pile) &&
                    isMoveValid(card, pile)) {

                result = pile;
                }
                
        }
        return result;
    }


    private boolean isOverPile(Card card, Pile pile) {
        if (pile.isEmpty())
            {
                System.out.println("IsOverPile true");
            return card.getBoundsInParent().intersects(pile.getBoundsInParent());
        }
        else{

            return card.getBoundsInParent().intersects(pile.getTopCard().getBoundsInParent());
        }
    }


    private void handleValidMove(List<Card> draggedCards, Pile destPile) {
        MouseUtil.slideToDest(draggedCards, destPile);
        draggedCards.clear();
    }





    public void refillStockFromDiscard() {
        //todo
        if(discardPile.getCards().size() > 0){
            for(int card = discardPile.getCards().size() - 1; card >= 0; card--){
                discardPile.getCards().get(card).flip();
                discardPile.getCards().get(card).moveToPile(stockPile);
            }
          
        }
        System.out.println("Stock refilled from discard pile.");
    }


    public boolean isGameWon() {

        int i = 0;


        for(Pile foundation : foundationPiles){
            if(foundation.getCards().get(foundation.getCards().size() - 1).getRank().getRankInt() == 2){
                i += 1;
            }
        }
        if(i == 4){
            return true;
        }else{
            return false;
        }
        
    }


    private void flipCard(Card card){
        if(card.getContainingPile().getPileType() == Pile.PileType.TABLEAU){
            if((card.getContainingPile().getCards().indexOf(card) - 1) >= 0){
                card.getContainingPile().getCards().get(card.getContainingPile().getCards().indexOf(card) - 1).flip();
            }
        }
    }


    private boolean allowPushCardToFoundation(Card card, Pile foundation){
        if        ((card.getRank() == Card.Rank.ACE && foundation.isEmpty()
        && card.getContainingPile().getPileType() != Pile.PileType.STOCK
        && card.getContainingPile().getTopCard() == card)
        || (card.getRank().getRankInt() == foundation.getTopCard().getRank().getRankInt() + 1
        && Card.isOppositeColor(card, foundation.getTopCard())
        && card.getContainingPile().getPileType() != Pile.PileType.STOCK
        && card.getContainingPile().getTopCard() == card)){
            return true;
        }else{
            return false;
        }

    }


    private void refillStockButton(){
        refillStockButton.setLayoutX(65);
        refillStockButton.setLayoutY(0);
        getChildren().add(refillStockButton);
        refillStockButton.setOnAction(new EventHandler<ActionEvent>(){
            //anonymous class
            @Override
            public void handle(ActionEvent event){
                refillStockFromDiscard();

        }
    }); 
    }


    private void undoChoiceButton(){
        undoButton.setLayoutX(0);
        undoButton.setLayoutY(0);
        getChildren().add(undoButton);
        undoButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                if(pileListName.equals("tableauPiles")){
                    if(tableauPiles.get(beforePileIndex).getTopCard() == null){
                        for(int index = tableauPiles.get(afterPileIndex).getCards().size() - numberOfCards; index < tableauPiles.get(afterPileIndex).getCards().size(); index ++){ //tableauPile.get(beforePileIndex)

                            tableauPiles.get(afterPileIndex).getCards().get(index).moveToPile(tableauPiles.get(beforePileIndex));
                            System.out.println("DOne");
                            afterPileIndex = null;
                            beforePileIndex = null;
                            numberOfCards = null;
                        }
                    }else{
                        for(int index = tableauPiles.get(afterPileIndex).getCards().size() - numberOfCards; index < tableauPiles.get(afterPileIndex).getCards().size(); index ++){ //tableauPile.get(beforePileIndex)
                            tableauPiles.get(beforePileIndex).getTopCard().flip();
                            tableauPiles.get(afterPileIndex).getCards().get(index).moveToPile(tableauPiles.get(beforePileIndex));
                            System.out.println("DOne");
                            afterPileIndex = null;
                            beforePileIndex = null;
                            numberOfCards = null;
                        }
                    }
                    
                }else if(pileListName.equals("discardPile") && numberOfCards != null){
                    tableauPiles.get(afterPileIndex).getTopCard().moveToPile(discardPile);
                    System.out.println("DOne");
                    afterPileIndex = null;
                    beforePileIndex = null;
                    numberOfCards = null;

                }else if(pileListName.equals("stockPile") && numberOfCards != null){
                    discardPile.getTopCard().flip();
                    discardPile.getTopCard().moveToPile(stockPile);
                    afterPileIndex = null;
                    beforePileIndex = null;
                    numberOfCards = null;
                }
            
            }
        });
    }





}
