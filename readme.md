# TrainsAndTowns test application

## Requirements
* Windows/Linux/Mac computer
* Java 8

## Compilation
The project is made under IntelliJ IDEA.
For the best result, I suggest downloading it using the [suggested link](https://www.jetbrains.com/idea/download/). 

If for some reason this is not possible, then the project can be 
build using Maven. To do this, download Maven from the link below and 
follow the instructions on the [Maven's page](http://maven.apache.org/download.cgi).

## Running
In the TransAndTowns folder there are 2 test graphs in different formats.
matrix.txt in the matrix, list.txt in the list.
To run tests on a graph in a matrix format, execute the following 
command in the console:
```
java -cp <JAR filename>.jar ws.skif.Main -f "<Path to matrix file>" -t m
```

To run tests on a graph in a list format, execute the following 
command in the console:
```
java -cp <JAR filename>.jar ws.skif.Main -f "<Path to list file>" -t l
```


**I hope that you will not have problems with testing**