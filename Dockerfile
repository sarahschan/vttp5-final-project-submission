FROM node:23 as angular-builder

WORKDIR /src

RUN npm i -g @angular/cli

COPY ./angular/*.json .
COPY ./angular/src src
COPY ./angular/public public

# Create the missing environment.development.ts file (replace placeholder with actual key before building)
RUN mkdir -p src/environments && \
    echo "export const environment = { \
    production: false, \
    googleMapsApiKey: 'abcdefg' \
};" > src/environments/environment.development.ts

RUN npm ci --legacy-peer-deps

RUN ng build

# Stage 2 - Copy the /browser files to spring-boot's static and build springboot
FROM eclipse-temurin:23-noble AS springboot-builder

WORKDIR /app

COPY ./springboot/mvnw .
COPY ./springboot/mvnw.cmd .
COPY ./springboot/pom.xml .
COPY ./springboot/.mvn .mvn
COPY ./springboot/src src

COPY --from=angular-builder /src/dist/angular/browser ./src/main/resources/static/

RUN chmod a+x ./mvnw && ./mvnw clean package -Dmaven.test.skip=true


# Stage 3 - Create final container with jar file
FROM eclipse-temurin:23-jre-noble

WORKDIR /app
COPY --from=springboot-builder /app/target/springboot-0.0.1-SNAPSHOT.jar myApp.jar

ENV SERVER_PORT=8080
EXPOSE ${SERVER_PORT}

ENTRYPOINT java -jar myApp.jar