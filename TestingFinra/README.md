README End to End
#Description
**EndToEnd.groovy - test the request of a new deck and draw cards from a deck for the deckofcardsapi Api
**These are the endpoint it hits:
* api/deck/new/ - Create a new deck
* api/deck/<<deck_id>>/draw/?count=3 - Draw cards from an existing deck

The test validates the request response codes and some contents on the json response, there is also a test for negative scenarios

#How to Run
**From IDE**
Create a JUnit test configuration, select end to end task

#TODO
* Fully integrate spring boot tes to be able to use profiling for different environment, so the tests can be used across all develop environments.
* Integrate Categories to be able to select test run for smoke, endtoend, slow,fast tests
* Add test to CI/CD in jenkins and execute them using gradle   