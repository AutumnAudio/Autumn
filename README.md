# Autumn_Audio
Javascript Audio Streaming Platform

https://autumn-audio.herokuapp.com/

TO RUN BACKEND:
1. mvn package
2. java -jar ./target/autumn-0.0.1-SNAPSHOT-jar-with-dependencies.jar

TO RUN REACT FRONTEND:
1. cd react
2. npm install
3. npm start

Deploy without running the test:
mvn -Dmaven.test.skip=true heroku:deploy
