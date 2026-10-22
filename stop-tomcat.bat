@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.10"
set "CATALINA_HOME=C:\apache-tomcat-10.1.60"
echo Stopping Apache Tomcat...
call "%CATALINA_HOME%\bin\shutdown.bat"
