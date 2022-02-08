# coding-challenge-master application
This project has two main classes. 1. ApplicationServer.java and 2. Main.java


## ApplicationServer
This creates a server instance with port number 4000. It will allow only maximum of 5 clients at any time. Any client request beyond 5 will be just closed.

## Main
Main java program creates 5 concurrent clients for ApplicationServer to send 9 digit number to ApplicationServer.

## Install Java
Minimum jre 11 is required.


## numbers.log and 
this log file is created in the root folder of the project


## coding-challenge-master.log
this log contains the 10 seconds reports.

### Dependencies
No dependencies currently. It is just pure Java language.

## Known defects with this project
1. After the terminate single from client to server, if a new client is created and send a message, the client request process is hung. This is noticeable when server received "terminate" and clearing/writing all the numbers to the destination file in progress.
2. Performance issue - Queue poll is very slow. I tried to use CopyOnWriteArrayList which helped the application server run super fast but concurrent modification is hit so, I reverted as I spent enough time by now.
3. High volume - bug exists. data is getting lost. need to look at.

#Overall - Below is what my plan about the project implementation

1. Application spans multiple threads one for each client request
2. each server client pushes a message to a queue
3. multiple listeners will be reading and clearing the message to the processor that 
	3.1 checks uniqueness and generate the reports and also finally writes to the numbers.log
4. If terminate signal is sent by any client then stops reading new inputs and initiate server shut down
5. Once step #3.1 is finished, proceed with the shut down

Overall - My challenge was mainly with the writing the code for unique check across the cache and stored file. this needs an affective algorithm. Found useful links https://www.toptal.com/algorithms/needle-in-a-haystack-a-nifty-large-scale-text-search-algorithm and https://github.com/marcocast/grep4j etc.
