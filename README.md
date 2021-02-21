# cmlteam-tech-task
This is technical task for CML Team (Full Stack / Java Developer willing to develop Full Stack expertise)

# Task Condition
[Task Condition](https://github.com/MikhailUsatenko/cmlteam-tech-task/blob/master/src/main/resources/File%20Storage%20REST%20Service%20-%20Java.docx.pdf)

# How to run app
1. docker run -d --name es762 -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.6.2
2. mvn clean install -DskipTests
3. java -jar demo-0.0.1-SNAPSHOT.jar

# Overview

<a href="https://www.baeldung.com/spring-data-elasticsearch-tutorial" 
target="_blank">Introduction to Spring Data Elasticsearch Tutorial</a>
and
<a href="https://piotrminkowski.com/2019/03/29/elasticsearch-with-spring-boot/" 
target="_blank">Elasticsearch with Spring Boot Tutorial (Embedded Configuration Included)</a>.
