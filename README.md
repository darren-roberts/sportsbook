# Sportsbook Project

The Sportsbook project consists of a set of APIs that will allow you to create Teams, a sports Event and record Scores against that event.

Using the APIs you can also enquire what the latest core is for any given Event.

In addition, the notification controller allows clients to register for notifications of when scores get created.


## Installation

from the command line (or inside your IDE), navigate to the root of the project and run 

mvn clean install

This will build the source code into binaries, executing test  cases as part fo the process.

## Creating the database

## Running the project

## Swagger

It is assumed that the reader is familar with how to use Swagger. Help with the Swagger UI can be found at https://swagger.io/tools/swagger-ui/ 

The Swagger test client can be used once the sportsbook project is running, at the following url:-

http://localhost:8080/swagger-ui.html#/

Once the page is accessed it can be used as a test client to run the apis documented within..


## Running the notifier 

Deploy the scoreNotificationClientEample.html to a web server, ideally on the same machine as the score API (just for demo purposes). 

Once it has oaded, save a score using the approrpiate API, and then go back to the scoreNotificationClientExample.html and it should be updated with the latest score details.

Its is assumed that the client will have the necessary logic to filter out score for events that they are interested in, the notification mechanism currently publishes all scores to and client registrred regardless of which event the score is for.

## Design Assumptions

### Latest Score

The latest score will be that recorded in the database that has the latest (newest) time associataed with it. The time is provided by the client and is assumed to be correct.


## Support

For support with this project please email darren.roberts@raptorsoftware.co.uk
