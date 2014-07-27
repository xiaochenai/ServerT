@echo off


REM cd IDACS_Common
REM del *.class

REM cd ..
REM cd IDACS_Connection
REM del *.class

REM cd ..
REM del Listener.class

javac IDACS_Common/Timeout.java
javac IDACS_Common/IDACSPacketRecord.java
javac IDACS_Common/IDACSCommon.java
javac IDACS_Common/IDACSSession.java

javac IDACS_Server_Connection/IDACSServerListener.java
javac IDACS_Server_Connection/IDACSServerConnection.java

javac runServer.java

java runServer