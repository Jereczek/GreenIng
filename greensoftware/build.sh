#!/bin/bash
echo "BUILD OF GREENING APP STARTED"
## Micronaut prefers graalvm native image :|
gradle wrapper && ./gradlew clean shadowJar
echo "BUILD OF GREENING APP FINISHED"
