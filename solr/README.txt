1. Download Solr 4.7.0. Location Service is tested with Solr 4.7.0.

http://sourceforge.net/projects/solr.mirror/files/Solr%204.7.0/solr-4.7.0.zip/download

2. Install Solr under Tomcat.

https://wiki.apache.org/solr/SolrTomcat#Installing_Solr_instances_under_Tomcat

3. Replace example/solr/collection1/conf/schema.xml file with schema.xml customized for the Location Service.

4. If Tomcat is not running in port 8080, update the value of the solr.host property in the configuration files listed below.

endpoint/src/main/webapp/WEB-INF/classes/config.properties
admin/src/main/webapp/WEB-INF/classes/config.properties