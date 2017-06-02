FROM java

LABEL author="Alekseev Aleksey" \
      email="alekseevmsc@gmail.com"

COPY target/twitter-listener.jar /app/twitter-listener.jar
WORKDIR /app/

ENTRYPOINT ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "twitter-listener.jar"]