Intruduction:
1. This is a SpringBoot project, it's the backend of a real-estate website. 
   It includes the server and MySQL database.

2. This is a real commercial project I did as an intern in Mikason group.
   (I delete some part to ensure the security of the system)

3. It can intuitively show information related to real estates, including price, 
   address, room type, rental and trading information, inspection record etc.

4. The structure of the database is shown inside DatabaseStructure.jpg.

Setting up:
1. This project is a Maven project using Java 1.8, Mysql database, JPA and SpringBoot framework, 
   the dependencies are inside the pom.xml file, please install all dependencies before running the project.

2. The src/main/resources/application.property is the file contains database setting information. 
   It has 2 options. 

3. The first option is to use real MySQL database. This method needs to install MySQL database
   first, then comment out line 1-3 amd use line 5-13 in application.property. Please change line 5-7 
   is to the path, username and password of your MySQL database.

4. The second option is to use H2 simulator, instead of creating a real database, it simulate 
   the performance of a database and does not need to install MySQL. If using this, comment out 
   line 5-7 of application.property and use line 1-3 & 10-13.

Project content:
1. This is a test-driven development project. 
2. The server is inside src/main/java/com/mikason/PropView/controller folder.
3. The JPA classes and interfaces (database tables) are inside src/main/java/com/mikason/PropView/dataaccess 
   and src/main/java/com/mikason/PropView/services folder.
4. The test files are inside src/test/java/com/mikason/PropView/controller folder, it tests the operation of each database table.