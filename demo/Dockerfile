FROM openjdk:17
RUN useradd -ms /bin/bash appuser
USER appuser
ADD target/occasion.jar occasion.jar
ENTRYPOINT ["java", "-jar", "occasion.jar"]
