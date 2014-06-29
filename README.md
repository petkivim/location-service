Location Service
================

The Location Service is a wayfinding application for libraries. It provides additional information and map-based guidance to books and collections by showing their location on a map, and it can be integrated with any library management system or discovery tool, as the integration is done by adding a link to the Location Service in a library's search interface. The Location Service is a multi-tenant application and it's designed for hosting multiple organizations in a single instance.

The main purpose of the application is to help library users to locate publications in a library by showing their location on a map. In addition to the map-based guidance, the Location Service also provides other information about the location, which should help the user to navigate through stacks of books. Elaborate collection and classification codes often used in the call number are presented in a form which is understandable to any library user. Displaying subject matters or images related to the location are also possible, and the use of images is recommended as it creates a link between the virtual and real word.

The Location Service has two user interfaces: One for customers (endpoint) and one for library staff (admin) for managing the information related to the locations of publications. The UI for customers is fully customizable by libraries, and the customization is done via template files by using the following techniques: HTML, CSS, and Javascript/jQuery. In addition to HTML also XML and JSON output formats are supported. The application supports multiple languages, and libraries have a full control of the languages which they want to support in their environment. All the information related to the locations can be managed via the administrator’s interface, which does not require any technical skills to use.

### Software Requirements

* Linux or Windows
* MySQL 5.X or Postgres 9.x
* Java 6 or 7
* Tomcat 6 or 7
* Solr 4.7.0

### Installation

How to install Solr, Location Service Endpoint and Location Service Admin on single Tomcat instance. MySQL is used as database.

#### Database

* Download [init_db.sql](https://github.com/petkivim/location-service/raw/master/db/mysql/init_db.sql) file that generates the 'location_service' database and creates admin user.

* Import the downloaded SQL file in the database.

```
mysql -u root -p < init_db.sql
```

#### Solr

* Download [solr-data.zip](https://github.com/petkivim/location-service/raw/master/solr/solr-data.zip) that contains Solr 4.7.0 index for the Location Service.

* Extract solr-data.zip into its own directory, e.g. /usr/local/solr-data

* Give the following rights to the tomcat user in solr-data directory.

```
chgrp -R tomcat solr-data/
chmod -R 770 solr-data/
```

* Download [solr-webapp.zip](https://github.com/petkivim/location-service/raw/master/solr/solr-webapp.zip) file that contains solr.war file.

* Create a new "solr" directory under Tomcat's webapps directory (goes under the tomcat.home/webapps) and extract solr-webapp.zip there. After exctracting solr-webapp.zip solr.war file should be located at tomcat.home/webapps/solr/solr.war.

* Extract solr.war under tomcat.home/webapps/solr directory, and remove solr.war file.

```
cd tomcat.home/webapps/solr
jar xvf solr.war
rm solr.war
```

* Update the path of solr-data directory to the tomcat.home/webapps/solr/META-INF/context.xml file. The default path is /usr/local/solr-data.

```
<?xml version="1.0" encoding="UTF-8" ?>
<Context path="/solr">
	<Environment name="solr/home" type="java.lang.String" value="C:/Users/dinky/Documents/Tmp/solr-data/solr-data" override="true" />
	<Environment name="user.language" type="java.lang.String" value="fi" override="true" />
	<Environment name="user.country" type="java.lang.String" value="FI" override="true" />
	<!-- If Tomcat is behind a proxy, e.g. Apache, the IP must be read from the X-Forwarded-For header -->
	<Valve className="org.apache.catalina.valves.RemoteIpValve" remoteIpHeader="X-Forwarded-For" proxiesHeader="X-Forwarded-By" protocolHeader="X-Forwarded-Proto" />
	<!-- Only connections from localhost are allowed -->
	<Valve className="org.apache.catalina.valves.RemoteAddrValve" allow="127\.\d+\.\d+\.\d+|::1|0:0:0:0:0:0:0:1"/>
</Context>
```

* If you want to access Solr Admin UI from your workstation, you need to add your IP address to webapps/solr/META-INF/context.xml file. By default access is restricted to localhost.

#### Location Service Endpoint

* Download endpoint-3.0.0-RELEASE.war file.

* Rename the file to endpoint.war.

* Create a new "endpoint" folder under Tomcat's webapps folder (goes under the tomcat.home/webapps) and extract endpoint.war there.

```
mkdir tomcat.home/webapps/endpoint
cp endpoint.war tomcat.home/webapps/endpoint
cd tomcat.home/webapps/endpoint
jar xvf endpoint.war
rm endpoint.war
```

* Update config.properties configuration file. The following properties must be updated: service.webPath, db.user, db.password, db.jdbcUrl, mail.host, mail.port, mail.user, mail.password.

```
tomcat.home/webapps/endpoint/WEB-INF/classes/config.properties
```

* By default tha application can be accessed at http://localhost:8080/endpoint. For a server install the URL (service.webPath property) needs to be updated from http://localhost:8080/ to an address that will be accessible publicly.

* If you want to use your GMail account for sending email, please use the following configuration.

```
mail.host=smtp.gmail.com
mail.port=587
mail.user=[username]
mail.password=[password]
```

* If Tomcat is not running in port 8080, update the value of the solr.host property.

* Give the following rights to the tomcat user in tomcat.home/webapps/endpoint/owners directory.

```
chgrp -R tomcat tomcat.home/webapps/endpoint/owners/
chmod -R 770 tomcat.home/webapps/endpoint/owners/
```

#### Location Service Admin

* Download admin-3.0.0-RELEASE.war file.

* Rename the file to admin.war.

* Create a new "admin" folder under Tomcat's webapps folder (goes under the tomcat.home/webapps) and extract admin.war there.

```
mkdir tomcat.home/webapps/admin
cp admin.war tomcat.home/webapps/admin
cd tomcat.home/webapps/admin
jar xvf admin.war
rm admin.war
```

* Update config.properties configuration file. The following properties must be updated: service.webPath, db.user, db.password, db.jdbcUrl, mail.host, mail.port, mail.user, mail.password.

```
tomcat.home/webapps/admin/WEB-INF/classes/config.properties
```

* By default tha application can be accessed at http://localhost:8080/admin. For a server install the URL (service.webPath property) needs to be updated from http://localhost:8080/ to an address that will be accessible publicly.

* If you want to use your GMail account for sending email, please use the following configuration.

```
mail.host=smtp.gmail.com
mail.port=587
mail.user=[username]
mail.password=[password]
```

* If Tomcat is not running in port 8080, update the value of the solr.host property.

* Update webapps/admin/META-INF/context.xml configuration file. "jdbc/mysql" resource's "username", "password" and "url" attributes must be updated. They should contain username, password and connection url for the database connection.

```
<Resource name="jdbc/mysql" 
  auth="Container" 
  type="javax.sql.DataSource" 
  maxActive="20" 
  maxIdle="10" 
  maxWait="10000"
  driverClassName="com.mysql.jdbc.Driver" 
  url="jdbc:mysql://localhost/location_service"  
  username="[username]"
  password="[password]"
/>
```

* Give the following rights to the tomcat user in tomcat.home/webapps/admin/owners directory.

```
chgrp -R tomcat tomcat.home/webapps/admin/owners/
chmod -R 770 tomcat.home/webapps/admin/owners/
```

* Copy the database driver MySQL/PostgreSQL from webapps/admin/WEB-INF/lib to Tomcat's lib directory (tomcat.home/lib).

```
mysql-connector-java-5.1.6.jar
postgresql-9.1-901.jdbc4.jar
```

### Running the application

Run Tomcat. Below there are the default URLs of the applications. The default username for the Admin application is "admin" and password "ChangeMe". It's strongly recommended to change the password immediately.

* Solr
  * [http://localhost:8080/solr/](http://localhost:8080/solr/)
* Location Service Endpoint
  * [http://localhost:8080/endpoint](http://localhost:8080/endpoint)
* Location Service Admin
  * [http://localhost:8080/admin](http://localhost:8080/admin)
  
Applications' log files are located in Tomcat's log folder (tomcat.home/logs) and they are named solr.log, location-service.log and location-service-admin.log.