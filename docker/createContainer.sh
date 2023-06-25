#!/bin/bash

echo "Loading compose..."
docker-compose -f ./docker-compose.yml up -d --build --remove-orphans
