README FILE:

> LOGGING INTO THE SYSTEM:
****************************** 	
	USER:hadoop
	PASSWORD: hadoop@12
******************************

 ****ROOT PASSWORD: root@12****

OPERATING SYSTEM - Ubuntu 12.04 32 bit

JAVA DEVELOPMENT KIT(JDK)
	- Version" Oracle JDK 1.7_45
	- Location: /usr/local/java/jdk1.7.0_45


********************************
	HADOOP ECOSYSTEM
********************************
1.APACHE HADOOP
	- Version: 1.2.1	
		
	- Location: /usr/share/hadoop

	- Configuration files location: /etc/hadoop

	- Binaries(startup scripts) - /usr/sbin

  Starting Hadoop (// You will be asked for password: hadoop@12)
  ------------------	 	
	  - To Start HDFS services:      
		- In terminal, type, sudo start-dfs.sh
	  
	  - To start MR services:       
		- In terminal type, sudo start-mapred.sh

	 - To Start Both HDFS and MapReduce services (in one command):
		- In terminal, type, sudo start-all.sh

  Stopping Hadoop (// You will be asked for password: hadoop@12)
  ----------------
	 - To Stop HDFS services:
		- In terminal, type,  sudo stop-dfs.sh
	  
	  - To stop MR services:
		- In terminal type,  sudo stop-mapred.sh

	 - To Stop both HDFS and MapReduce services  (in one command):
		- In terminal, type,  sudo stop-all.sh
   		 
   Tracking the cluster status and jobs
   -------------------------------------
	  - To see the progress of MR jobs:
		- In Mozilla firefox, From the FireFox menue--> Bookmarks---> Hadoop Map/Reduce Adminstration

	  - To see the NameNode and File System UI:
		- In Mozilla firefox, From the FireFox menue--> Bookmarks---> Hadoop NameNode
	

  Testing a MapReduce job
  ------------------------	
	- An input file is present in the /user/hadoop directory on HDFS.
	- To run a Wordcount job, go to /usr/share/hadoop.
		- Run, hadoop jar hadoop-examples-1.2.1.jar wordcount /user/hadoop/input /user/hadoop/output

   Compiling your own Java code
   ---------------------
	- First create your java file (Example WordCount.java is found under "Documents" folder)
        - Go to the directory containing your code (E.g., "D    
	> mkdir wordcount_classes
	> javac -classpath /usr/share/hadoop/hadoop-core-1.2.1.jar -d wordcount_classes ./WordCount.java
	> jar -cvf ./wordcount.jar -C wordcount_classes/ .

	- Now a jar file is created and you are ready to submit a job as follows:
	- Notice that "org.apache.hadoop.examples" in the command is the package name you defined in your code.
	> hadoop jar ./wordcount.jar org.apache.hadoop.examples.WordCount <path to input file in HDFS> <path to outout file in HDFS>







++++++++++++++++++++++++++++
2. APACHE HIVE:
	- Version: 0.13.1
	- Location: /usr/local/hive/hive
	- Metastore: MySQL

     - To Run Hive Scripts:
	- In a terminal, type    $ hive       to enter the hive terminal. You should get the hive> prompt
	- There is an empty table named testmysql(a int, b int) present. When you run "> SHOW TABLES;" , this table should show up.
	- You can start typing Hive commands
	- Type   > exit;       to leave the Hive terminal





++++++++++++++++++++++++++++
3. APACHE PIG:
	- Version: 0.13.0
	- Location: /usr/local/pig/pig
  
  - To Run Pig  (Have Hadoop clutser running first):
	- In a terminal, type  $ pig     to enter the grunt shell. You should get the grunt> prompt
	- You can start typing Pig commands
	- Type  > quit     to leave the grunt shell.	





++++++++++++++++++++++++++++
4. APACHE HBASE 
	- Version: 0.94.26
	- Location: /usr/local/hbase/hbase

  - To Start HBase services:
	- In a terminal, type sudo /usr/local/hbase/hbase/bin/start-hbase.sh. 3 services namely, Master, Regionserver and Zookeeper will start up 

  - To run HBase commands
	- In a terminal type, hbase shell to enter into the hbase terminal. You should get the hbase(main):001:0> prompt.
	- A table called test is created; on typing list, you should see this table.
	- Type  > exit        to quit the shell
  
  - To Stop HBase services:
	- In a terminal, type sudo /usr/local/hbase/hbase/bin/stop-hbase.sh


++++++++++++++++++++++++++++
5. RHADOOP
  - Softwares and packages installed for RHadoop:
	- R : Version - 3.1.3
	- RStudio : Version - 0.98.1103
	- rmr2 package : Version - 3.3.1
	- rhdfs package: Version - 1.0.8

  - To run RHadoop jobs:
	- Open RStudio or R in the terminal by typing  > R
	- Load the required libraries:
		library("rmr2")
		library("rhdfs")
	- Run the init command:
		- hdfs.init()

  Example RHadoop
   ------------------
	- Run R by typing > R in the terminal
	- Under "Documents" folder you will find code for word-count example (file called "r") that you can copy and paste. You only need to   
                  change the "inputPath" and "outputPath" variables to your desired files. 




++++++++++++++++++++++++++++
6. Hadoop Streaming
      - The jar file is located in /usr/share/hadoop/contrib/streaming/hadoop-streaming-1.2.1.jar
      - To run Hadoop Streaming from the command line, have Hadoop cluster working, and then write command:
	>  hadoop jar /usr/share/hadoop/contrib/streaming/hadoop-streaming-1.2.1.jar  <the rest of the command and parameters>	      
			



++++++++++++++++++++++++++++
7. MONGODB
	- Version: 2.0.4
	- Control Scripts location: /etc/init.d
	- Data location: /var/lib/mongodb

    - To start MongoDB services:
	- In a terminal, type    > sudo service mongodb start

    - To run MongoDB:
	- Type  > mongo   from a terminal to enter the Mongo shell

    - To exit MongoDB:
	- From the mongo shell type  > exit

    - To stop MongoDB services:
	- In a terminal, type    > sudo service mongodb stop




++++++++++++++++++++++++++++
8. MAHOUT
	- Version: 0.9

	- Location: /usr/local/mahout/mahout

	- To run mahout:
		- In a terminal, type mahout <name_of_the_program> <parameters>
	





