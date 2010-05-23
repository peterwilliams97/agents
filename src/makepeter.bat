REM Clean out old versions
del examples/peter/*.class
del peter.jar
del peter.zip

REM Make the agent and copy it to launch directory
javac examples/peter/PeterAgent.java
javac examples/peter/PeterService.java
javac examples/peter/PeterSearch.java
rem jar cfm peter.jar peter.mf examples/peter/PeterAgent.class examples/peter/PeterService.class examples/peter/PeterSearch.class
jar cfm PeterAgent.jar PeterAgent.mf examples/peter/PeterAgent.class
jar cfm PeterService.jar PeterService.mf examples/peter/PeterService.class
jar cfm PeterSearch.jar PeterSearch.mf examples/peter/PeterSearch.class

copy *.jar ..

REM Perform some checks
java examples.peter.PeterAgent
java examples.peter.PeterService
java examples.peter.PeterSearch
