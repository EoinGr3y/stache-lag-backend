Prerequisites
- 
- MongoDB is installed and running locally.
- java jdk installed.
- maven installed.

Running locally
-
- Run `mvn clean install` to install dependencies.
- Run `mvn spring-boot:run` to start application.
- Open browser on `http://localhost:8080/swagger-ui.html` to view swagger docs.

There are 2 controllers, `race-controller` and `race-controller-ui`. `race-controller` is a standard rest controller, returning json as response. 
`race-controller-ui` is a Spring Thymeleaf controller, providing a basic ui for response data.
Moments are submitted in the format `2017-11-19T19:00:00Z`, days are submitted in the format 2017-11-19.

Sample request urls;
- View average sightings for 12th Dec - http://localhost:8080/averageVesselsPerDaySummary?day=2017-12-12
- View number of sightings at moment - http://localhost:8080/visibleVesselsForMomentSummary?teamName=Vahine&moment=2017-11-19T19:00:00Z

Assumptions
-
- Visibility of 5km
- Moments for visibility checks are rounded to the nearest 10seconds as not all teams log position data at the exact same second moment. 

Enhancements
-
- Dockerize service to remove mongo prerequisite