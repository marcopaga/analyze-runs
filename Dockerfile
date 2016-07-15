FROM java:8-alpine
COPY  target/uberjar/analyze-runs-0.1.0-SNAPSHOT-standalone.jar /usr/app/app.jar
WORKDIR /usr/app
EXPOSE 3000
CMD ["java", "-jar", "app.jar"]
