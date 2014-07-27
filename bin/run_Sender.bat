@echo off


REM cd IDACS_Common
REM del *.class

REM cd ..
REM cd IDACS_Connection
REM del *.class

REM cd ..
REM del Sender.class




javac Sender.java
java Sender 192.168.0.106 1