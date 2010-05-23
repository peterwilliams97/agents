REM Clean out old versions

del examples\bookTrading\*.class
del bookTrading.jar
del bookTrading.zip

REM Make the agents and copy them to launch directory

javac examples/bookTrading/BookBuyerAgent.java
javac examples/bookTrading/BookSellerAgent.java
javac examples/bookTrading/BookSellerGui.java 

jar cfm BookBuyerAgent.jar BookBuyerAgent.mf examples/bookTrading/BookBuyerAgent*.class
jar cfm BookSellerAgent.jar BookSellerAgent.mf examples/bookTrading/BookSellerAgent*.class
jar cfm BookSellerGui.jar BookSellerGui.mf examples/bookTrading/BookSellerGui*.class

copy *.jar ..

REM Check that classes are visible in classpath and well-formed
java examples.bookTrading.BookBuyerAgent
java examples.bookTrading.BookSellerAgent
java examples.bookTrading.BookSellerGui

copy book*.jar book*.zip
