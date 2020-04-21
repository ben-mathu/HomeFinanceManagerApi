# Installation Instructions

## Requirements

### PostgreSQL

Download the PostgreSQL DBMS from here -> [PostgreSQL](https://www.postgresql.org/download/)

#### PostgreSQL installation instructions

Extract the files then move into the extracted file directory
Run these commands, for those running Linux.

$ ./configure

$ make

$ su

$ make install

$ adduser postgres

$ mkdir /usr/local/pgsql/data

$ chown postgres /usr/local/pgsql/data

$ su - postgres

$ /usr/local/pgsql/bin/initdb -D /usr/local/pgsql/data

$ /usr/local/pgsql/bin/pg_ctl -D /usr/local/pgsql/data -l logfile start

$ /usr/local/pgsql/bin/createdb test

$ /usr/local/pgsql/bin/psql test

Documentation can be found here: <https://www.postgresql.org/docs/11/install-short.html>

Method II

import the GPG key for PostgreSQL packages

$ wget --quiet -O - <https://www.postgresql.org/media/keys/ACCC4CF8.asc> | sudo apt-key add -

add the repository to your system

$ sudo sh -c 'echo "deb <http://apt.postgresql.org/pub/repos/apt/> `lsb_release -cs`-pgdg main" >> /etc/apt/sources.list.d/pgdg.list'

install postgresql on ubuntu

apt install postgresql postgresql-contrib -y

### Tomcat

Download tomcat from here -> [Tomcat](https://tomcat.apache.org/download-80.cgi)

### Web Application Instruction

- Find the .war file under ./target folder
- Move the .war file to the installation tomcat directory under webapp.

Tomcat will extract the .war file, then deploy the application for you.

On a browser, navigate to [http://localhost:8080/hfms](http://localhost:8080/hfms)
