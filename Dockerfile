FROM openjdk:17-jdk-alpine

WORKDIR /apps

ARG JAR_FILE=./jars/ai-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /apps/mortgages-ui.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/apps/mortgages-ui.jar"]