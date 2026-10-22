@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.10"
set "CATALINA_HOME=C:\apache-tomcat-10.1.60"
echo ===================================================
echo   Starting Apache Tomcat 10.1 for ReturnHub...
echo ===================================================
call "%CATALINA_HOME%\bin\startup.bat"
echo.
echo Tomcat is now running in its own console window!
echo NOTE: Do NOT close the Tomcat window while using the application.
echo Access the site at: http://localhost:8080/productreturn/
echo ===================================================
