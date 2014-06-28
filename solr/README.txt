1. Download solr-data.zip that contains Solr 4.7.0 index for the Location Service.

2. Extract solr-data.zip into its own directory, e.g. /usr/local/solr-data

3. Give the following rights to the tomcat user in solr-data directory.

chgrp -R tomcat solr-data/
chmod -R 770 solr-data/

3. Download solr-webapp.zip file that contains solr.war file.

4. Create a new folder "solr" folder under Tomcat's webapps folder and extract solr-webapp.zip there.

5. Extract solr.war under webapps/solr directory, and remove solr.war file.

jar xvf solr.war
rm solr.war

6. Update the path of solr-data directory to the webapps/solr/META-INF/context.xml file. The default path is /usr/local/solr-data.

7. If you want to access Solr Admin UI from your workstation, you need to add your IP address to webapps/solr/META-INF/context.xml file. By default access is restricted to localhost.

8. If Tomcat is not running in port 8080, update the value of the solr.host property in the configuration files listed below.

endpoint/src/main/webapp/WEB-INF/classes/config.properties
admin/src/main/webapp/WEB-INF/classes/config.properties