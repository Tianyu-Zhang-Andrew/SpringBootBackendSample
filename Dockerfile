FROM openjdk:8-jdk-alpine
VOLUME /tmp

# Make port 9999 available to the world outside this container
#EXPOSE 9999

# The application's jar file
ARG JAR_FILE=target/PropView-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","/app.jar"]