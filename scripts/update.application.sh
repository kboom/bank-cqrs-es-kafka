#!/bin/bash

SCRIPTS_DIR=$(dirname "$0")
APPLICATION_NAME=$1
DEPLOYMENT_K8S="deployment/${APPLICATION_NAME}.k8s.yml"

bash ${SCRIPTS_DIR}/build.application.sh $APPLICATION_NAME && \
kubectl set image -f ${DEPLOYMENT_K8S} bank-${APPLICATION_NAME}=bank-${APPLICATION_NAME}:latest