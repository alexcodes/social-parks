#!/usr/bin/env bash

docker build -t social-parks-web ./web-server
docker build -t twitter-listener ./twitter-listener