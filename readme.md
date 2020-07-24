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
- Use `/visibleVesselsForMoment` to get list of visible vessels for a given teamName and moment.

Assumptions
-
- Visibility of 5km

Enhancements
-
- Dockerize service to remove mongo prerequisite