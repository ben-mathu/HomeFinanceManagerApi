#!/bin/bash

rm -v /opt/tomcat/latest/webapps/hfms-1.0.0.war
cp -v target/hfms-1.0.0.war /opt/tomcat/latest/webapps/
tail -fv /opt/tomcat/latest/logs/catalina.out
