#####################################
# Shell script to deply glassfish
#
#
######################################


sudo apt-get update
sudo apt-get install python-software-properties
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java7-installer


wget download.java.net/glassfish/4.0/release/glassfish-4.0.zip

sudo apt-get install unzip
sudo unzip glassfish-4.0.zip -d /opt
export PATH=/opt/glassfish4/bin:$PATH
sudo chmod 777 -R /opt/*
asadmin start-domain

wget https://glassfish.java.net/downloads/quickstart/hello.war
asadmin deploy /home/ee/glassfish/sample/hello.war
