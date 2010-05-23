@echo off
if not "%jade_root%" == "" goto Start
echo Please set the jade_root environment variable to the root of your jade installation
goto Done

:Start
@echo on
REM Clean out old versions
del examples\ordering\*.class
del ordering.jar
del ordering.zip

REM Make the agents and copy them to launch directory
javac -cp %jade_root%\lib\jade.jar;%jade_root%\lib\jadeTools.jar;%jade_root%\lib\iiop.jar;%jade_root%\lib\commons-codec\commons-codec-1.3.jar;.; examples/ordering/PartItemList.java 
javac -cp %jade_root%\lib\jade.jar;%jade_root%\lib\jadeTools.jar;%jade_root%\lib\iiop.jar;%jade_root%\lib\commons-codec\commons-codec-1.3.jar;.; examples/ordering/ItemList.java 

javac -cp %jade_root%\lib\jade.jar;%jade_root%\lib\jadeTools.jar;%jade_root%\lib\iiop.jar;%jade_root%\lib\commons-codec\commons-codec-1.3.jar;.; examples/ordering/BuyerAgent.java
javac -cp %jade_root%\lib\jade.jar;%jade_root%\lib\jadeTools.jar;%jade_root%\lib\iiop.jar;%jade_root%\lib\commons-codec\commons-codec-1.3.jar;.; examples/ordering/SupplierAgent.java



REM Pack the agents classes in a jar file
jar cf ordering.jar examples/ordering/*.class

REM Copy the jar file into Jade directory
copy ordering.jar ..

REM Check that classes are visible in classpath and well-formed
java -cp %jade_root%\lib\jade.jar;%jade_root%\lib\jadeTools.jar;%jade_root%\lib\iiop.jar;%jade_root%\lib\commons-codec\commons-codec-1.3.jar;.; examples.ordering.PartItemList
java -cp %jade_root%\lib\jade.jar;%jade_root%\lib\jadeTools.jar;%jade_root%\lib\iiop.jar;%jade_root%\lib\commons-codec\commons-codec-1.3.jar;.; examples.ordering.ItemList
java -cp %jade_root%\lib\jade.jar;%jade_root%\lib\jadeTools.jar;%jade_root%\lib\iiop.jar;%jade_root%\lib\commons-codec\commons-codec-1.3.jar;.; examples.ordering.BuyerAgent
java -cp %jade_root%\lib\jade.jar;%jade_root%\lib\jadeTools.jar;%jade_root%\lib\iiop.jar;%jade_root%\lib\commons-codec\commons-codec-1.3.jar;.; examples.ordering.SupplierAgent


copy ordering.jar ordering.zip

:Done