# RIP Simulator

A multi-threaded application that simulates the RIP protocol [1] in a topology of simple routers.
Each router is a separate thread that maintains its own separate routing table.

## Compilation

In order to compile you must execute
    mvn compile

The application can be packaged in a jar file
    mvn package

[1]: http://en.wikipedia.org/wiki/Routing_Information_Protocol
