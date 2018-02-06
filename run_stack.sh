#!/usr/bin/env bash

export HOST_IP=`ifconfig en0 | grep inet | grep -v inet6 | cut -d' ' -f2`
mvn -f solution/pom.xml package -Pdocker,release
docker-compose up --build