#!/bin/bash

mvn clean install
rm $CATALINA_HOME/hfmsapp/ROOT.war
mv target/hfms.war $CATALINA_HOME/hfmsapp/ROOT.war
sudo service tomcat restart
tail -fv $CATALINA_HOME/logs/catalina.out
