REM Clean out old versions
del examples/peter/PeterAgent.class
del peter.jar
del peter.zip

REM Make the agent and copy it to launch directory
javac examples/peter/PeterAgent.java
jar cfm peter.jar peter.mf examples/peter/PeterAgent.class
copy peter.jar ..

REM Perform some checks
copy peter.jar peter.zip
java examples.peter.PeterAgent

