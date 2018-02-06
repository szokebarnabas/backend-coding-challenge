FROM openjdk:8-jre

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/expense/expense-api.jar"]

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/expense/expense-api.jar