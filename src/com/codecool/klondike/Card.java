package com.codecool.klondike;


import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.layout.Region;
import java.awt.GraphicsEnvironment;

import java.awt.GraphicsDevice;
import java.util.*; //niepotrzebne importowanie wszystkiego!

public class Card extends ImageView {

    private Suit suit;
    private Rank rank;
    private boolean faceDown;

    private Image backFace;
    private Image frontFace;
    private Pile containingPile;
    private DropShadow dropShadow;

    static Image cardBackImage;
    private static final Map<String, Image> cardFaceImages = new HashMap<>();
    public static int WIDTH = 150;
    public static int HEIGHT = 215;

    public Card(Suit suit, Rank rank, boolean faceDown) {
        this.suit = suit;
        this.rank = rank;
        this.faceDown = faceDown;
        this.dropShadow = new DropShadow(2, Color.gray(0, 0.75));
        backFace = cardBackImage;
        frontFace = cardFaceImages.get(getShortName());
        setImage(faceDown ? backFace : frontFace);
        setEffect(dropShadow);

    }
    //zwraca wyglad
    public Suit getSuit() {
        return this.suit;
    }

    public Rank getRank() {
        return this.rank;
    }
//zwraca wartosc boolean na podstawie ktorej sprawdza się czy którą stroną karta ma być obrócona
    public boolean isFaceDown() {
        return faceDown;
    }
    public void setFrontFace(Image newImage){
        frontFace = newImage;
    }
    public void setBackFace(Image newImage){
        backFace = newImage;
    }

    public String getShortName() {
        return "S" + getStringSuit(suit) + "R" + rank.getRankInt();
    }

    public DropShadow getDropShadow() {
        return dropShadow;
    }
//zwraca zmienna przechowujaca pile'a w ktorym sie znajduje
    public Pile getContainingPile() {
        return containingPile;
    }
//ustawia pile ktory przechowuje ta karte
    public void setContainingPile(Pile containingPile) {
        this.containingPile = containingPile;
    }
//dodaje tą kartę do innego pile i usuwa z tego
    public void moveToPile(Pile destPile) {
        this.getContainingPile().getCards().remove(this);
        destPile.addCard(this);
    }
    
    public boolean getFaceDown(){
        return faceDown;
    }

    public void flip() {
        faceDown = !faceDown;
        setImage(faceDown ? backFace : frontFace);
    }

    @Override
    public String toString() {
        return "The " + "Rank" + rank + " of " + "Suit" + suit;
    }
//metoda ktora zwraca wartosc boolean w zaleznosci od tego czy card1 jest rozny od card2 
    public static boolean isOppositeColor(Card card1, Card card2) {
        //TODO
        if(!(((card1.getSuit().getIntSuit() + card2.getSuit().getIntSuit()) % 2) == 0)){
            System.out.println(card1.getSuit().getIntSuit() + card2.getSuit().getIntSuit());
            return true;
        }else{
            System.out.println(card1.getSuit().getIntSuit() + card2.getSuit().getIntSuit());
            return false;
        }

    }

    public static boolean isSameSuit(Card card1, Card card2) {
        return card1.getSuit() == card2.getSuit();
    } 

    public static List<Card> createNewDeck() {
        List<Card> result = new ArrayList<>();
        for (Suit suit : Suit.values()) { 
            for (Rank rank : Rank.values()) { //??tworzy sie deck ta funkcja chyba a nie nowy obiekt clasy deck
                result.add(new Card(suit, rank, true));
            }
        }
        return result;
    }
//tworzy wyglad kart i przypisuje je do hashmapy
    public static void loadCardImages(String cardBackFile, String cardFile) {
        
        cardBackImage = new Image(cardBackFile);
        for (Suit suit: Suit.values()) {
            // suitName = suit.toString();
            for (Rank rank: Rank.values()) {
                String cardName =  getStringSuit(suit) +  rank.getRankInt();//suitName + rank.rank;
                System.out.println(cardName);
                String cardId = "S" + getStringSuit(suit) + "R" + rank.getRankInt();
                String imageFileName = cardFile + cardName + ".png";
                System.out.println(imageFileName);
                cardFaceImages.put(cardId, new Image(imageFileName));
                
            }
        }
    }
  
    public enum Suit {
        hearts(1), diamonds(3), spades(2), clubs(4);
        final int numberOfSuit;

        private Suit(final int numberOfSuit){
            this.numberOfSuit = numberOfSuit;        
        }

        public int getIntSuit(){
            return this.numberOfSuit;
        }


    }

    public enum Rank {
        ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7),
         EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13);
        final int rank;

         private Rank(final int rank) {
             this.rank = rank;
        }
        public int getRankInt(){
            return this.rank;
        }
    }

    public static String getStringSuit(Suit suit){
        String result = "";
        if(suit.getIntSuit() == 1){
            result = "hearts";
        }else if(suit.getIntSuit() == 3){
            result = "diamonds";
        }else if(suit.getIntSuit() == 2){
            result = "spades";
        }else if(suit.getIntSuit() == 4){
            result = "clubs";
        }
        return result;
    }

    

}


