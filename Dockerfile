FROM gradle:7.6.4-jdk17 AS builder

WORKDIR /app

COPY build.gradle settings.gradle .
COPY src src

ENV JAVA_OPTS="-Dhttps.protocols=TLSv1.2"
RUN gradle build

FROM openjdk:17-oracle

WORKDIR /app
COPY --from=builder /app/build/libs/PlayWordle.jar app.jar

CMD ["java", "-jar", "app.jar"]