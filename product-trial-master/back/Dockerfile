FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

ENV JAVA_OPTS=""
ENV DEBUG="false"
ENV DEBUG_PORT="5005"

EXPOSE 8080
EXPOSE 5005

ENTRYPOINT ["/bin/sh", "-c", "if [ \"$DEBUG\" = \"true\" ]; then JAVA_OPTS=\"$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:$DEBUG_PORT\"; fi; exec java $JAVA_OPTS -jar app.jar"]
