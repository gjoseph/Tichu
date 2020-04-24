interface CardBase {
    name: string;
    shortName: string;
    scoreValue: number;
}

export interface NormalCard extends CardBase {
    type: "normal";
    suit: CardSuit;
    number: number;
}

export interface SpecialCard extends CardBase {
    type: "special";
    special: SpecialCards;
}

export type Card = NormalCard | SpecialCard;

enum CardSuit {
    Jade,
    Sword,
    Pagoda,
    Star
}

enum SpecialCards {
    MahJong,
    Dog,
    Phoenix,
    Dragon
}

function isSpecialCard(object: any): object is SpecialCard {
    return "member" in object;
}

export const Cards: Array<Card> = [
    {
        name: "Phoenix",
        shortName: "*P",
        scoreValue: -25,
        special: SpecialCards.Phoenix
    } as SpecialCard,
    {
        name: "MahJong",
        shortName: "*1",
        scoreValue: 0,
        special: SpecialCards.MahJong
    } as SpecialCard,
    {
        name: "Dog",
        shortName: "*H",
        scoreValue: 0,
        special: SpecialCards.Dog
    } as SpecialCard,
    {
        name: "Dragon",
        shortName: "*D",
        scoreValue: 25,
        special: SpecialCards.Dragon
    } as SpecialCard,
    {
        name: "2 of Pagoda",
        shortName: "B2",
        scoreValue: 0,
        suit: CardSuit.Pagoda,
        number: 2
    } as NormalCard,
    {
        name: "3 of Pagoda",
        shortName: "B3",
        scoreValue: 0,
        suit: CardSuit.Pagoda,
        number: 3
    } as NormalCard,
    {
        name: "4 of Pagoda",
        shortName: "B4",
        scoreValue: 0,
        suit: CardSuit.Pagoda,
        number: 4
    } as NormalCard,
    {
        name: "5 of Pagoda",
        shortName: "B5",
        scoreValue: 5,
        suit: CardSuit.Pagoda,
        number: 5
    } as NormalCard,
    {
        name: "6 of Pagoda",
        shortName: "B6",
        scoreValue: 0,
        suit: CardSuit.Pagoda,
        number: 6
    } as NormalCard,
    {
        name: "7 of Pagoda",
        shortName: "B7",
        scoreValue: 0,
        suit: CardSuit.Pagoda,
        number: 7
    } as NormalCard,
    {
        name: "8 of Pagoda",
        shortName: "B8",
        scoreValue: 0,
        suit: CardSuit.Pagoda,
        number: 8
    } as NormalCard,
    {
        name: "9 of Pagoda",
        shortName: "B9",
        scoreValue: 0,
        suit: CardSuit.Pagoda,
        number: 9
    } as NormalCard,
    {
        name: "10 of Pagoda",
        shortName: "B0",
        scoreValue: 10,
        suit: CardSuit.Pagoda,
        number: 10
    } as NormalCard,
    {
        name: "Jack of Pagoda",
        shortName: "BJ",
        scoreValue: 0,
        suit: CardSuit.Pagoda,
        number: 11
    } as NormalCard,
    {
        name: "Queen of Pagoda",
        shortName: "BQ",
        scoreValue: 0,
        suit: CardSuit.Pagoda,
        number: 12
    } as NormalCard,
    {
        name: "King of Pagoda",
        shortName: "BK",
        scoreValue: 10,
        suit: CardSuit.Pagoda,
        number: 13
    } as NormalCard,
    {
        name: "Ace of Pagoda",
        shortName: "BA",
        scoreValue: 0,
        suit: CardSuit.Pagoda,
        number: 14
    } as NormalCard,
    {
        name: "2 of Jade",
        shortName: "G2",
        scoreValue: 0,
        suit: CardSuit.Jade,
        number: 2
    } as NormalCard,
    {
        name: "3 of Jade",
        shortName: "G3",
        scoreValue: 0,
        suit: CardSuit.Jade,
        number: 3
    } as NormalCard,
    {
        name: "4 of Jade",
        shortName: "G4",
        scoreValue: 0,
        suit: CardSuit.Jade,
        number: 4
    } as NormalCard,
    {
        name: "5 of Jade",
        shortName: "G5",
        scoreValue: 5,
        suit: CardSuit.Jade,
        number: 5
    } as NormalCard,
    {
        name: "6 of Jade",
        shortName: "G6",
        scoreValue: 0,
        suit: CardSuit.Jade,
        number: 6
    } as NormalCard,
    {
        name: "7 of Jade",
        shortName: "G7",
        scoreValue: 0,
        suit: CardSuit.Jade,
        number: 7
    } as NormalCard,
    {
        name: "8 of Jade",
        shortName: "G8",
        scoreValue: 0,
        suit: CardSuit.Jade,
        number: 8
    } as NormalCard,
    {
        name: "9 of Jade",
        shortName: "G9",
        scoreValue: 0,
        suit: CardSuit.Jade,
        number: 9
    } as NormalCard,
    {
        name: "10 of Jade",
        shortName: "G0",
        scoreValue: 10,
        suit: CardSuit.Jade,
        number: 10
    } as NormalCard,
    {
        name: "Jack of Jade",
        shortName: "GJ",
        scoreValue: 0,
        suit: CardSuit.Jade,
        number: 11
    } as NormalCard,
    {
        name: "Queen of Jade",
        shortName: "GQ",
        scoreValue: 0,
        suit: CardSuit.Jade,
        number: 12
    } as NormalCard,
    {
        name: "King of Jade",
        shortName: "GK",
        scoreValue: 10,
        suit: CardSuit.Jade,
        number: 13
    } as NormalCard,
    {
        name: "Ace of Jade",
        shortName: "GA",
        scoreValue: 0,
        suit: CardSuit.Jade,
        number: 14
    } as NormalCard,
    {
        name: "2 of Sword",
        shortName: "K2",
        scoreValue: 0,
        suit: CardSuit.Sword,
        number: 2
    } as NormalCard,
    {
        name: "3 of Sword",
        shortName: "K3",
        scoreValue: 0,
        suit: CardSuit.Sword,
        number: 3
    } as NormalCard,
    {
        name: "4 of Sword",
        shortName: "K4",
        scoreValue: 0,
        suit: CardSuit.Sword,
        number: 4
    } as NormalCard,
    {
        name: "5 of Sword",
        shortName: "K5",
        scoreValue: 5,
        suit: CardSuit.Sword,
        number: 5
    } as NormalCard,
    {
        name: "6 of Sword",
        shortName: "K6",
        scoreValue: 0,
        suit: CardSuit.Sword,
        number: 6
    } as NormalCard,
    {
        name: "7 of Sword",
        shortName: "K7",
        scoreValue: 0,
        suit: CardSuit.Sword,
        number: 7
    } as NormalCard,
    {
        name: "8 of Sword",
        shortName: "K8",
        scoreValue: 0,
        suit: CardSuit.Sword,
        number: 8
    } as NormalCard,
    {
        name: "9 of Sword",
        shortName: "K9",
        scoreValue: 0,
        suit: CardSuit.Sword,
        number: 9
    } as NormalCard,
    {
        name: "10 of Sword",
        shortName: "K0",
        scoreValue: 10,
        suit: CardSuit.Sword,
        number: 10
    } as NormalCard,
    {
        name: "Jack of Sword",
        shortName: "KJ",
        scoreValue: 0,
        suit: CardSuit.Sword,
        number: 11
    } as NormalCard,
    {
        name: "Queen of Sword",
        shortName: "KQ",
        scoreValue: 0,
        suit: CardSuit.Sword,
        number: 12
    } as NormalCard,
    {
        name: "King of Sword",
        shortName: "KK",
        scoreValue: 10,
        suit: CardSuit.Sword,
        number: 13
    } as NormalCard,
    {
        name: "Ace of Sword",
        shortName: "KA",
        scoreValue: 0,
        suit: CardSuit.Sword,
        number: 14
    } as NormalCard,
    {
        name: "2 of Star",
        shortName: "R2",
        scoreValue: 0,
        suit: CardSuit.Star,
        number: 2
    } as NormalCard,
    {
        name: "3 of Star",
        shortName: "R3",
        scoreValue: 0,
        suit: CardSuit.Star,
        number: 3
    } as NormalCard,
    {
        name: "4 of Star",
        shortName: "R4",
        scoreValue: 0,
        suit: CardSuit.Star,
        number: 4
    } as NormalCard,
    {
        name: "5 of Star",
        shortName: "R5",
        scoreValue: 5,
        suit: CardSuit.Star,
        number: 5
    } as NormalCard,
    {
        name: "6 of Star",
        shortName: "R6",
        scoreValue: 0,
        suit: CardSuit.Star,
        number: 6
    } as NormalCard,
    {
        name: "7 of Star",
        shortName: "R7",
        scoreValue: 0,
        suit: CardSuit.Star,
        number: 7
    } as NormalCard,
    {
        name: "8 of Star",
        shortName: "R8",
        scoreValue: 0,
        suit: CardSuit.Star,
        number: 8
    } as NormalCard,
    {
        name: "9 of Star",
        shortName: "R9",
        scoreValue: 0,
        suit: CardSuit.Star,
        number: 9
    } as NormalCard,
    {
        name: "10 of Star",
        shortName: "R0",
        scoreValue: 10,
        suit: CardSuit.Star,
        number: 10
    } as NormalCard,
    {
        name: "Jack of Star",
        shortName: "RJ",
        scoreValue: 0,
        suit: CardSuit.Star,
        number: 11
    } as NormalCard,
    {
        name: "Queen of Star",
        shortName: "RQ",
        scoreValue: 0,
        suit: CardSuit.Star,
        number: 12
    } as NormalCard,
    {
        name: "King of Star",
        shortName: "RK",
        scoreValue: 10,
        suit: CardSuit.Star,
        number: 13
    } as NormalCard,
    {
        name: "Ace of Star",
        shortName: "RA",
        scoreValue: 0,
        suit: CardSuit.Star,
        number: 14
    } as NormalCard
];
