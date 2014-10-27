#####################################
# Shell script to deply glassfish
#
#
######################################

#!/bin/bash

sudo apt-get -y update
sudo apt-get -y install python-software-properties wget curl git unzip

sudo apt-get -y install openjdk-7-jre
sudo apt-get -y install openjdk-7-jdk
cd ~

wget download.java.net/glassfish/4.0/release/glassfish-4.0.zip

#sudo apt-get -y install unzip
sudo unzip glassfish-4.0.zip -d /opt
export PATH=/opt/glassfish4/bin:$PATH
sudo chmod 777 -R /opt/*

#/opt/glassfish4/glassfish/domains/domain1/config/domain.xml

#sudo cat /opt/glassfish4/glassfish/domains/domain1/config/domain.xml | sed 's/"8080"/"80"/' > temp
#cp temp /opt/glassfish4/glassfish/domains/domain1/config/domain.xml
asadmin start-domain
sudo rm -r Amazons3upload
#cd ~
git clone https://github.com/saipramod/Amazons3upload.git
#wget https://github.com/saipramod/Amazons3upload/blob/master/target/ITMD544ASS3-1.0-SNAPSHOT.war
asadmin deploy --force=true /home/ubuntu/Amazons3upload/target/ITMD544ASS3-1.0-SNAPSHOT.war

