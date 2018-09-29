#!/bin/bash

realpath() {
    path=`eval echo "$1"`
    folder=$(dirname "$path")
    echo $(cd "$folder"; pwd)/$(basename "$path");
}

DIRECTORY=`realpath $0`
DIRECTORY=`dirname $DIRECTORY`
JAR_FILE=${DIRECTORY}/../dist/jpp.jar

java -jar ${JAR_FILE} $*
