#!/usr/bin/env bash

cd ..

docker rm $(docker container ps -a -q --filter ancestor=back)

docker rmi back

docker build --tag back

docker run -d --name back-container back