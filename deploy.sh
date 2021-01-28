#!/usr/bin/env bash

docker stop $(docker container ps -a -q --filter name=back-container)

docker rm $(docker container ps -a -q --filter name=back-container)

docker rmi $(docker images --filter=reference='back:*' -qa)

docker build --tag back /home/ubuntu/docker/back

docker run -d -p 8080:8080 --name back-container back