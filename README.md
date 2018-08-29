Precis V0.2
===========

Precis needs to be build using maven project.

Running Precis in Maven:-
=========================

1) Download & install java, maven (if you don't have).

2) create a folder "precis", go to the folder.

3) Run the command --> "git clone https://github.com/vijaysrajan/precis_for_gist.git"

4) cd precis_for_gist

5) Run maven compile with the command --> "mvn clean package"

6) Run the Programs as below :-

	a) Precis Generation     - java -cp target/precis-0.0.1-SNAPSHOT.jar com.fratics.precis.fis.main.Main
	b) Threshold Generation  - java -cp target/precis-0.0.1-SNAPSHOT.jar com.fratics.precis.fis.threshold.ThresholdMain
	c) Sanitation Generation - java -cp target/precis-0.0.1-SNAPSHOT.jar com.fratics.precis.fis.sanitation.SanitationMain

7) you can change the Precis configuration under "conf/precisconfig.properties"

8) All the configuration and schema files are self explainatory.

Running Precis in eclipse:-
===========================

1) Download & install java, eclipse (if you don't have).

2) create a folder "precis", go to the folder.

3) Run the command --> "git clone https://github.com/vijaysrajan/precis_for_gist.git"

4) Open eclipse, import as maven project (or java project) what ever you wish.

5) Open Run Configurations for this current project, Search Main Class to Run.

6) Select the corresponding main class as required as given below, Apply & Run.

	a) Precis Generation     - com.fratics.precis.fis.main.Main
	b) Threshold Generation  - com.fratics.precis.fis.threshold.ThresholdMain
	c) Sanitation Generation - com.fratics.precis.fis.sanitation.SanitationMain

Happy Preciiiiiing.
