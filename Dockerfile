FROM eclipse-temurin:21
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# docker build -t springinventory:1.0 .
# docker image list
# docker run -p 8080:8080 --name springapp -d -t springinventory:1.0
# docker stop springapp
# docker start springapp
# docker stats
# docker logs -f springapp
# docker rm springapp
# docker login
# docker tag springbootapp:1.0 mfernandez69/springinventory:1.0
# docker push mfernandez69/springinventory:1.0

# Desde un servidor:

# docker pull mfernandez69/springinventory:1.0
# docker run -p 8080:8080 --name springapp -d -t mfernandez69/springinventory:1.0
# docker rmi mfernandez69/springinventory:1.0