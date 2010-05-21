javac HelloWorldAgent.java
del hello.jar
jar cfm hello.jar hello.mf HelloWorldAgent.class
copy hello.jar ..\..\..
java HelloWorldAgent

