# Use an official OpenJDK runtime as a parent image
FROM openjdk:21

# Create a user to run the application
RUN useradd -ms /bin/sh appuser

# Copy the JAR file into the image at /tmp
COPY ./build/libs/KyPoint-1.0-SNAPSHOT.jar /tmp/KyPoint.jar

# Copy the resources folder into the image at /app/resources
COPY ./src/main/resources/ /app/resources/

# Set the working directory to /tmp
WORKDIR /tmp

# Change permissions for the resource files to 600
RUN chmod -R 600 /app/resources

# Change ownership of the resource files to the appuser
RUN chown -R appuser:appuser /app/resources

# Switch to the appuser
USER appuser

# Run the JAR file using the -jar option
ENTRYPOINT ["java", "-jar", "KyPoint.jar"]
