# Sportsbook Project

The Sportsbook project consists of a set of APIs that will allow you to create Teams, a sports Event and record Scores against that event.

Using the APIs you can also enquire what the latest core is for any given Event.

In addition, the notification controller allows clients to register for notifications of when scores get created.


## Installation

from the command line (or inside your IDE), navigate to the root of the project and run 

```mvn clean install```

This will build the source code into binaries, executing test  cases as part fo the process.

## Creating the database

The project uses an embded H2 database, which will be created each time the project starts.

As per the Spring docuemntation the database can be subsituted by provding a different dependency (eg MYSQL) in the pom.xml and changinthe the appropriate propeties in the ```application.properties``` file.

## Running the project

The project can be run on the command line by execution the following command from the root folder of the project

```mvn spring-boot:run```

## Swagger

It is assumed that the reader is familar with how to use Swagger. Help with the Swagger UI can be found at https://swagger.io/tools/swagger-ui/ 

The Swagger test client can be used once the sportsbook project is running, at the following url:-

```http://localhost:8080/swagger-ui.html#/```

Once the page is accessed it can be used as a test client to run the apis documented within..

In order to record scores, the following steps must be taken first:-

1) Setup your times
2) Setup your event, incuding which teams are playing.

No you are ready to record your scores (they do not have to be "delivered" in aby particular order to the API but they must be correct)

Finally you can use the API to ask hat the latest score is for a given event.

If yuo wish (optionally) you can register for notifications for when the scores are created.


## Running the notifier 

Deploy the scoreNotificationClientEample.html to a web server, ideally on the same machine as the score API (just for demo purposes). 

Once it has oaded, save a score using the approrpiate API, and then go back to the scoreNotificationClientExample.html and it should be updated with the latest score details.

Its is assumed that the client will have the necessary logic to filter out score for events that they are interested in, the notification mechanism currently publishes all scores to and client registrred regardless of which event the score is for.

## Design Assumptions / Considerations

- Scores are not delivered in any particular order but are correct.
- Clients regsitering for notifications will filter out events that they are not interested in.
- Teams and events are setup before hand, rather than on demand.

### Latest Score

The latest score will be that recorded in the database that has the latest (newest) time associataed with it. The time is provided by the client and is assumed to be correct.

## Support

For support with this project please email ```darren.roberts@raptorsoftware.co.uk```
