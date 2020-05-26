package com.infra.test.endtoend

import groovy.json.JsonSlurper
import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Unroll

@Unroll
@SpringBootTest(classes = Application.class)
class EndToEnd extends InfraServiceSpecification {
    RequestSpecification requestSpecification = RestAssured.given()
    JsonSlurper jsonSlurper = new JsonSlurper()


    @Shared
    String deckId = ""

    def setup() {
        boolean jokersEnabled = false;
        Response newDeckSetup = createNewDeck(jokersEnabled)
        def newDeck = jsonSlurper.parseText(newDeckSetup.body().asString())
        deckId = newDeck?.deck_id;
    }

    def "1 - Create a Brand New Deck with jokersEnabled = #jokersEnabled"() {
        given: ""
        when: ""
        Response responseNewDeck = createNewDeck(jokersEnabled)
        assert responseNewDeck.statusCode() == expectedResponseCode
        def response = jsonSlurper.parseText(responseNewDeck.body().asString())
        then: "Validate Response"
        assert response?.success == expectedSuccess: "The operations did not succeed"
        assert response?.deck_id != null: "Response does not contain a valid deck id"
        assert response?.remaining == expectedRemaining
        where: "Adding params"
        jokersEnabled | expectedResponseCode | expectedSuccess | expectedRemaining
        false         | 200                  | true            | 52
        true          | 200                  | true            | 54

    }

    def "2 - Draw cards for a deck with [cards =#cardsNumber deckId=#expectedDeckId]"() {
        given: "A valid deck Id"
        assert deckId != null: "Global deck Id is not valid"
        when: "Request draw for a deck"
        Response responseDrawCard = drawCardsFromDeck(expectedDeckId, cardsNumber)
        assert responseDrawCard.statusCode() == expectedResponseCode
        def response = jsonSlurper.parseText(responseDrawCard.body().asString())
        then: "Validate Response"
        assert responseDrawCard.statusCode() == expectedResponseCode
        assert response?.success == expectedSuccess: "The operations did not succeed"
        if (!expectedDeckId?.equals("new")) //New will return a dynamic id
            assert response?.deck_id == expectedDeckId: "The response does not match the deck id"
        assert response?.cards != null: "Response does not contain valid cards"
        if (cardsNumber <= 52) //only for numbers inside the valid range of cards
            assert response?.cards?.size() == cardsNumber: "Response with invalid num of cards"
        where:
        cardsNumber | expectedResponseCode | expectedSuccess | expectedDeckId
        1           | 200                  | true            | deckId
        2           | 200                  | true            | deckId
        3           | 200                  | true            | deckId
        4           | 200                  | true            | "new"
        52          | 200                  | true            | "new"
        100         | 200                  | false           | "new"
        300         | 200                  | false           | "new"
    }

    def "3 - Draw cards for a deck(Negative Scenario) with [cards =#cardsNumber deckId=#expectedDeckId]"() {
        given: "A test deck Id"
        assert deckId != null: "Global deck Id is not valid"
        when: "Request draw for a deck"
        Response responseDrawCard = drawCardsFromDeck(expectedDeckId, cardsNumber)
        then: "Validate Response"
        assert responseDrawCard.statusCode() == expectedResponseCode
        where:
        cardsNumber | expectedResponseCode | expectedDeckId
        4           | 500                  | "wrong_id"
        4           | 500                  | "wrong_id"
        300         | 200                  | "new"
    }
}
