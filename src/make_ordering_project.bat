REM Clean out old versions
del examples\ordering\*.class
del ordering.jar
del ordering.zip

REM Make the agents and copy them to launch directory
javac -cp \java\jade\lib\jade.jar;\java\jade\lib\jadeTools.jar;\java\jade\lib\iiop.jar;\java\jade\lib\commons-codec\commons-codec-1.3.jar;.; examples/ordering/PartItemList.java 
javac -cp \java\jade\lib\jade.jar;\java\jade\lib\jadeTools.jar;\java\jade\lib\iiop.jar;\java\jade\lib\commons-codec\commons-codec-1.3.jar;.; examples/ordering/ItemList.java 
javac -cp \java\jade\lib\jade.jar;\java\jade\lib\jadeTools.jar;\java\jade\lib\iiop.jar;\java\jade\lib\commons-codec\commons-codec-1.3.jar;.; examples/ordering/BookSellerGui.java 
javac -cp \java\jade\lib\jade.jar;\java\jade\lib\jadeTools.jar;\java\jade\lib\iiop.jar;\java\jade\lib\commons-codec\commons-codec-1.3.jar;.; examples/ordering/BuyerAgent.java
javac -cp \java\jade\lib\jade.jar;\java\jade\lib\jadeTools.jar;\java\jade\lib\iiop.jar;\java\jade\lib\commons-codec\commons-codec-1.3.jar;.; examples/ordering/SupplierAgent.java

REM Pack the agents classes in a jar file
jar cf ordering.jar examples/ordering/*.class

REM Copy the jar file into Jade directory
copy ordering.jar ..

REM Check that classes are visible in classpath and well-formed
java -cp \java\jade\lib\jade.jar;\java\jade\lib\jadeTools.jar;\java\jade\lib\iiop.jar;\java\jade\lib\commons-codec\commons-codec-1.3.jar;.; examples.ordering.PartItemList
java -cp \java\jade\lib\jade.jar;\java\jade\lib\jadeTools.jar;\java\jade\lib\iiop.jar;\java\jade\lib\commons-codec\commons-codec-1.3.jar;.; examples.ordering.ItemList
java -cp \java\jade\lib\jade.jar;\java\jade\lib\jadeTools.jar;\java\jade\lib\iiop.jar;\java\jade\lib\commons-codec\commons-codec-1.3.jar;.; examples.ordering.BuyerAgent
java -cp \java\jade\lib\jade.jar;\java\jade\lib\jadeTools.jar;\java\jade\lib\iiop.jar;\java\jade\lib\commons-codec\commons-codec-1.3.jar;.; examples.ordering.SupplierAgent
java -cp \java\jade\lib\jade.jar;\java\jade\lib\jadeTools.jar;\java\jade\lib\iiop.jar;\java\jade\lib\commons-codec\commons-codec-1.3.jar;.; examples.ordering.BookSellerGui

copy ordering.jar ordering.zip
