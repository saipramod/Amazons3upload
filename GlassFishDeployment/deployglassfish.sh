#####################################
# Shell script to deply glassfish
#
#
######################################


sudo apt-get -y update
sudo apt-get -y install python-software-properties apache2 wget curl git unzip
service apache2 stop
#sudo add-apt-repository -y ppa:webupd8team/java
#sudo apt-get -y update
#sudo apt-get -y install oracle-java7-installer

sudo apt-get -y install openjdk-7-jre
cd ~

wget download.java.net/glassfish/4.0/release/glassfish-4.0.zip

#sudo apt-get -y install unzip
sudo unzip glassfish-4.0.zip -d /opt
export PATH=/opt/glassfish4/bin:$PATH
sudo chmod 777 -R /opt/*
asadmin start-domain
#cd ~
git clone https://github.com/saipramod/Amazons3upload.git
wget ITMD544ASS3-1.0-SNAPSHOT.war
asadmin deploy ITMD544ASS3-1.0-SNAPSHOT.war
