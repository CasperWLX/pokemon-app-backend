FROM openjdk:22
# Set the working directory in the container
WORKDIR /app
# Copy the JAR file into the container named /app and renames it to 'app.jar'
COPY build/libs/projekt-uppgift-api-0.0.1-SNAPSHOT.jar app.jar
# Expose the port that the application will run on (Must reflect Spring Boot's PORT)
EXPOSE 10001

ENV DATABASE_URL=${DATABASE_URL}
ENV JWT_SECRET=${JWT_SECRET}

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]