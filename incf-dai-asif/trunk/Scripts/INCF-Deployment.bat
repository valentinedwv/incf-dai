rem *************Stopping Tomcat************
cd C:\Asif\work\devtools\Tomcat5.5.17\bin
tomcat5 //SS// Apache Tomcat


rem *************CLEANING UP DEPLOYMENT FILE************

cd C:\Asif\work\devtools\Tomcat5.5.17\webapps
del incf-services.war

echo deleted the incf-services.war file successfully....


rem *************CLEANING UP DEPLOYMENT DIRECTORY************

cd C:\Asif\work\devtools\Tomcat5.5.17\webapps
rmdir /S /Q incf-services


echo deleted the incf-services file successfully....

rem *************BUILDING CLIENTSIDE JAR FILE************

cd C:\Asif\work\projects\birn\smartatlas\services\incfservices-rest\development\Scripts
call INCF-Build.bat

echo compiled successfully....

rem *************COPYING CLIENTSIDE WAR FILE FROM SRC TO DEPLOYMENT************

copy C:\Asif\work\projects\birn\smartatlas\services\incfservices-rest\development\dist\incf-services.war "C:\Asif\work\devtools\Tomcat5.5.17\webapps\incf-services.war"
echo copied incf-services.war successfully....

echo copied in to the deployment directory successfully....


rem *************Starting Tomcat************
#cd C:\Asif\work\devtools\Tomcat5.5.17\bin
tomcat5.exe

echo started tomcat successfully....
