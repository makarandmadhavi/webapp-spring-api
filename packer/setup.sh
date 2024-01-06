#!/bin/bash

# Set the -e option
set -e
echo "===================== UPDATE AND UPGRADE ============================="
# Update package list
sudo apt-get update
sudo apt-get upgrade -y

echo "===================== OPEN JDK 17 ============================="
# Install OpenJDK 17
sudo apt-get install -y openjdk-17-jdk

echo "===================== CREATE USER ============================="
# Create user for app
sudo groupadd csye6225
sudo useradd -s /bin/false -g csye6225 -d /opt/csye6225 -m csye6225

echo "===================== COPY APP ============================="
sudo mv /opt/webapp.jar /opt/csye6225/webapp.jar
sudo mv /opt/users.csv /opt/csye6225/users.csv

echo "===================== ENABLE APP SERVICE ============================="
sudo systemctl daemon-reload
sudo systemctl enable webapp.service

echo "===================== INSTALLING CLOUDWATCH AGENT ============================="
sudo wget https://amazoncloudwatch-agent.s3.amazonaws.com/debian/amd64/latest/amazon-cloudwatch-agent.deb
sudo dpkg -i -E ./amazon-cloudwatch-agent.deb

echo "Server setup completed."
