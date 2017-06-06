FROM java

LABEL author="Alekseev Aleksey" \
      email="alekseevmsc@gmail.com"

COPY target/open-data-loader.jar /app/open-data-loader.jar
WORKDIR /app/

ENTRYPOINT ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "open-data-loader.jar"]