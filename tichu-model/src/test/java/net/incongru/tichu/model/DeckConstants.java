package net.incongru.tichu.model;

import java.util.Set;

/**
 * @author gjoseph
 */
public class DeckConstants {
    public static void main(String[] args) {
        CardDeck deck = new CardDeck();
        final Set<Card> cards = deck.getCards();
        for (Card card : cards) {
            final String prefix = "public static final Card ";
            if (card.isSpecial()) {
                System.out.print(prefix + card.getVal().niceName());
                System.out.println(" = new Card(Card.CardSpecials." + card.getVal() + ", null);");
            } else {
                System.out.print(prefix + card.getSuit() + "_" + card.getVal().niceName());
                System.out.println(" = new Card(Card.CardNumbers." + card.getVal() + ", Card.CardSuit." + card.getSuit() + ");");
            }
        }
    }

    public static final Card Jade_2 = new Card(Card.CardNumbers.Two, Card.CardSuit.Jade);
    public static final Card Sword_2 = new Card(Card.CardNumbers.Two, Card.CardSuit.Sword);
    public static final Card Pagoda_2 = new Card(Card.CardNumbers.Two, Card.CardSuit.Pagoda);
    public static final Card Star_2 = new Card(Card.CardNumbers.Two, Card.CardSuit.Star);
    public static final Card Jade_3 = new Card(Card.CardNumbers.Three, Card.CardSuit.Jade);
    public static final Card Sword_3 = new Card(Card.CardNumbers.Three, Card.CardSuit.Sword);
    public static final Card Pagoda_3 = new Card(Card.CardNumbers.Three, Card.CardSuit.Pagoda);
    public static final Card Star_3 = new Card(Card.CardNumbers.Three, Card.CardSuit.Star);
    public static final Card Jade_4 = new Card(Card.CardNumbers.Four, Card.CardSuit.Jade);
    public static final Card Sword_4 = new Card(Card.CardNumbers.Four, Card.CardSuit.Sword);
    public static final Card Pagoda_4 = new Card(Card.CardNumbers.Four, Card.CardSuit.Pagoda);
    public static final Card Star_4 = new Card(Card.CardNumbers.Four, Card.CardSuit.Star);
    public static final Card Jade_5 = new Card(Card.CardNumbers.Five, Card.CardSuit.Jade);
    public static final Card Sword_5 = new Card(Card.CardNumbers.Five, Card.CardSuit.Sword);
    public static final Card Pagoda_5 = new Card(Card.CardNumbers.Five, Card.CardSuit.Pagoda);
    public static final Card Star_5 = new Card(Card.CardNumbers.Five, Card.CardSuit.Star);
    public static final Card Jade_6 = new Card(Card.CardNumbers.Six, Card.CardSuit.Jade);
    public static final Card Sword_6 = new Card(Card.CardNumbers.Six, Card.CardSuit.Sword);
    public static final Card Pagoda_6 = new Card(Card.CardNumbers.Six, Card.CardSuit.Pagoda);
    public static final Card Star_6 = new Card(Card.CardNumbers.Six, Card.CardSuit.Star);
    public static final Card Jade_7 = new Card(Card.CardNumbers.Seven, Card.CardSuit.Jade);
    public static final Card Sword_7 = new Card(Card.CardNumbers.Seven, Card.CardSuit.Sword);
    public static final Card Pagoda_7 = new Card(Card.CardNumbers.Seven, Card.CardSuit.Pagoda);
    public static final Card Star_7 = new Card(Card.CardNumbers.Seven, Card.CardSuit.Star);
    public static final Card Jade_8 = new Card(Card.CardNumbers.Eight, Card.CardSuit.Jade);
    public static final Card Sword_8 = new Card(Card.CardNumbers.Eight, Card.CardSuit.Sword);
    public static final Card Pagoda_8 = new Card(Card.CardNumbers.Eight, Card.CardSuit.Pagoda);
    public static final Card Star_8 = new Card(Card.CardNumbers.Eight, Card.CardSuit.Star);
    public static final Card Jade_9 = new Card(Card.CardNumbers.Nine, Card.CardSuit.Jade);
    public static final Card Sword_9 = new Card(Card.CardNumbers.Nine, Card.CardSuit.Sword);
    public static final Card Pagoda_9 = new Card(Card.CardNumbers.Nine, Card.CardSuit.Pagoda);
    public static final Card Star_9 = new Card(Card.CardNumbers.Nine, Card.CardSuit.Star);
    public static final Card Jade_10 = new Card(Card.CardNumbers.Ten, Card.CardSuit.Jade);
    public static final Card Sword_10 = new Card(Card.CardNumbers.Ten, Card.CardSuit.Sword);
    public static final Card Pagoda_10 = new Card(Card.CardNumbers.Ten, Card.CardSuit.Pagoda);
    public static final Card Star_10 = new Card(Card.CardNumbers.Ten, Card.CardSuit.Star);
    public static final Card Jade_Jack = new Card(Card.CardNumbers.Jack, Card.CardSuit.Jade);
    public static final Card Sword_Jack = new Card(Card.CardNumbers.Jack, Card.CardSuit.Sword);
    public static final Card Pagoda_Jack = new Card(Card.CardNumbers.Jack, Card.CardSuit.Pagoda);
    public static final Card Star_Jack = new Card(Card.CardNumbers.Jack, Card.CardSuit.Star);
    public static final Card Jade_Queen = new Card(Card.CardNumbers.Queen, Card.CardSuit.Jade);
    public static final Card Sword_Queen = new Card(Card.CardNumbers.Queen, Card.CardSuit.Sword);
    public static final Card Pagoda_Queen = new Card(Card.CardNumbers.Queen, Card.CardSuit.Pagoda);
    public static final Card Star_Queen = new Card(Card.CardNumbers.Queen, Card.CardSuit.Star);
    public static final Card Jade_King = new Card(Card.CardNumbers.King, Card.CardSuit.Jade);
    public static final Card Sword_King = new Card(Card.CardNumbers.King, Card.CardSuit.Sword);
    public static final Card Pagoda_King = new Card(Card.CardNumbers.King, Card.CardSuit.Pagoda);
    public static final Card Star_King = new Card(Card.CardNumbers.King, Card.CardSuit.Star);
    public static final Card Jade_Ace = new Card(Card.CardNumbers.Ace, Card.CardSuit.Jade);
    public static final Card Sword_Ace = new Card(Card.CardNumbers.Ace, Card.CardSuit.Sword);
    public static final Card Pagoda_Ace = new Card(Card.CardNumbers.Ace, Card.CardSuit.Pagoda);
    public static final Card Star_Ace = new Card(Card.CardNumbers.Ace, Card.CardSuit.Star);
    public static final Card MahJong = new Card(Card.CardSpecials.MahJong, null);
    public static final Card Dog = new Card(Card.CardSpecials.Dog, null);
    public static final Card Phoenix = new Card(Card.CardSpecials.Phoenix, null);
    public static final Card Dragon = new Card(Card.CardSpecials.Dragon, null);

}
