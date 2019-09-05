1.) Download/clone the Java Api project from "https://github.com/arijitdeb1502/JavaApi.git"
2.) Unzip the project JavaApi-master.zip
3.) Go inside JavaApi-master subolder from window explorer 
4.) Right click on the folder and "Open Folder as Intellij IDEA Project"
5.) Open a Terminal from Intellij IDEA and cd to "JavaApi-master\JavaApi-master"
6.) Inside quora-api submodule change the following configurations of application.yaml file :

	src url of database:
	username: 
    password: 
	
7.) Inside quora-db submodule change the following configurations of localhost.properties file :
	server.host=
	server.port=
	database.name=
	database.user=
	database.password=

8.) Issue "mvn clean install" 
9.) Go to src --> main-->java--> com.upgrad.quora.api.QuoraApplication.java . Right click on this file and run it.

10.) Once the application is up. Send the following url to the server to view the different endpoints of the application using swagger documentation:

http://localhost:8080/api/swagger-ui.html
