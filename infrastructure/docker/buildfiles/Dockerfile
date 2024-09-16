FROM openjdk:17

COPY target/*.jar shopping-cart.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "shopping-cart.jar"]