#!/bin/sh
./mvnw clean package && docker build -t hardikloriya/spring-security-jwt-refresh-token . && docker push hardikloriya/spring-security-jwt-refresh-token