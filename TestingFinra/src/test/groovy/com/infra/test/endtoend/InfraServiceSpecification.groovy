package com.infra.test.endtoend

import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import spock.lang.Shared
import spock.lang.Specification

class InfraServiceSpecification extends Specification {
    RequestSpecification requestSpecification = RestAssured.given()
    @Shared
    private String baseUri = "http://deckofcardsapi.com/"

    @Shared
    private String newDeckApi = "api/deck/new"

    @Shared
    private String drawDeckApi = "api/deck/<<deck_id>>>/draw"
    /**
     * Call the deckofcardsapi.com to get a new deck
     * @return the api response
     */
    Response createNewDeck(boolean jokersEnabled) {
        RestAssured.baseURI = baseUri
        Response responseNewDeck = RestAssured.given()
                .given()
                .queryParam("jokers_enabled", jokersEnabled)
                .get(newDeckApi)
        return responseNewDeck;
    }

    /**
     * Call the draw APi
     * @param deckId the deckid
     * @param cardsNumber number of cards
     * @return Response with cards
     */
    Response drawCardsFromDeck(String deckId, int cardsNumber) {
        RestAssured.baseURI = baseUri

        String drawCardApi = drawDeckApi
        drawCardApi = drawCardApi.replaceAll("<<deck_id>>>", deckId)
        Response responseNewDeck = RestAssured.given()
                .given()
                .queryParam("count", cardsNumber)
                .get(drawCardApi)
        return responseNewDeck
    }

}
