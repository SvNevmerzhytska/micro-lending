#Micro lending REST Application#

##Requirements##
Java 8

##Run application in IDE##
To run the application, locate and run the `ua.nevmerzhytska.MicroLendingApp` class.
Add:

 * two program arguments (separated with space) when running this class: `server /path/to/dropwizard-config.yml` 
 The path to `signing-rest-api.yml` should be the absolute path to the file.
 * OR just `server` argument (to use default dropwizard configuration)

##Build fat jar and run from console##
Run `mvn clean package` to build fat jar.
To run the application use command `java -jar /path/to/micro-lending-app-version.jar server /path/to/dropwizard-config.yml`

##Access running app##
By dafault the application should be available at [http://localhost:8080/](http://localhost:8080/), 
and a Swagger client (for convenient testing) should be available at [http://localhost:8080/swagger](http://localhost:8080/swagger).
*Notice* Swagger works properly only under default configuration. If application context or port are changed in `*.yml` file, Swagger could be very buggy.

##Should be done##
* write acceptance and integration tests;
* increase test coverage. Notice TODOs in test classes;
* extend validation and add additional checks for corner cases.
