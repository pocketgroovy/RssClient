FROM openjdk:22-slim
MAINTAINER pocketgroovy.com
COPY RssReaderDemoClient-0.0.1-SNAPSHOT.jar RssReaderDemoClient-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/RssReaderDemoClient-0.0.1-SNAPSHOT.jar"]