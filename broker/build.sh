#!/bin/bash

rm -r ./bin/*
javac -d "./bin" -cp "./src/java:./lib/*" ./src/java/edu/nmsl/crowdsourcing/broker/main/Main.java
cp -r ./lib ./bin
cp ./src/res/* ./bin
