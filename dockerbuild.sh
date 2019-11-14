#bin/bash

# delete local containers
sudo docker rm $(sudo docker ps -a -q)

# delete local images
sudo docker rmi --force 721517280680.dkr.ecr.us-east-2.amazonaws.com/mybiblestudywebapp
sudo docker rmi --force mybiblestudywebapp_mybiblestudywebapp

# login to ECR
sudo $(aws ecr get-login --no-include-email --region us-east-2)

# delete repository images
aws ecr batch-delete-image --repository-name mybiblestudywebapp --image-ids imageTag=latest

# maven build
sudo mvn clean install

# build locally
sudo docker-compose build

# push to ECR
sudo docker tag mybiblestudywebapp_mybiblestudywebapp:latest 721517280680.dkr.ecr.us-east-2.amazonaws.com/mybiblestudywebapp:latest
sudo docker push 721517280680.dkr.ecr.us-east-2.amazonaws.com/mybiblestudywebapp:latest

# build zip file to upload to EBS
rm aws.zip
zip aws.zip Dockerrun.aws.json


