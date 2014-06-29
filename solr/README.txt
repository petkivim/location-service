1. Download solr-data.zip that contains Solr 4.7.0 index for the Location Service.

2. Extract solr-data.zip into its own directory, e.g. /usr/local/solr-data

3. Give the following rights to the tomcat user in solr-data directory.

chgrp -R tomcat solr-data/
chmod -R 770 solr-data/

3. Download solr-webapp.zip file that contains solr.war file.

4. Create a new "solr" directory under Tomcat's webapps directory (goes under the tomcat.home/webapps) and extract solr-webapp.zip there. After exctracting solr-webapp.zip there should be tomcat.home/webapps/solr/solr.war file.

5. Extract solr.war under tomcat.home/webapps/solr directory, and remove solr.war file.

cd tomcat.home/webapps/solr
jar xvf solr.war
rm solr.war

6. Update the path of solr-data directory to the tomcat.home/webapps/solr/META-INF/context.xml file. The default path is /usr/local/solr-data.

7. If you want to access Solr Admin UI from your workstation, you need to add your IP address to webapps/solr/META-INF/context.xml file. By default access is restricted to localhost.