#!/bin/bash

APPLICATION_NAME=$1
APPLICATION_DIR="application/$APPLICATION_NAME"

mvn clean -f $APPLICATION_DIR/pom.xml package
eval $(minikube docker-env) && docker build -f $APPLICATION_DIR/Dockerfile -t $APPLICATION_NAME:latest $APPLICATION_DIR