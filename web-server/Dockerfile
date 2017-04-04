FROM java

LABEL author="Alekseev Aleksey" \
      email="alekseevmsc@gmail.com"

COPY target/web-server.jar /app/web-server.jar
WORKDIR /app/

EXPOSE 8081
ENTRYPOINT ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "web-server.jar"]