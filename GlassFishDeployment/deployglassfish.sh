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
cd ~
git clone https://github.com/saipramod/Amazons3upload/blob/master/target/ITMD544ASS3-1.0-SNAPSHOT.war
wget ITMD544ASS3-1.0-SNAPSHOT.war
asadmin deploy ITMD544ASS3-1.0-SNAPSHOT.war
