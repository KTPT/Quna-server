#!/usr/bin/env bash

cd ..

docker rm $(docker container ps -a -q --filter ancestor=back)

docker rmi $(docker images --filter=reference='back:*' -qa)

pwd

docker build --tag back .

docker run -d --name back-container back