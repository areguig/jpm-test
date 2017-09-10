Command line application written in java 8 using [jline3](https://github.com/jline/jline3) library.


|command|   |
| ---- | ---- |
| help | displays the help text |
| load file.csv  |loads the data from a comma separated csv file with header (entity,type,agreedfx,currency,instructionDate,settlementDate,units,price) (or just use sample as file name to load a sample data set)   |
| showall | displays all the instructions. |
| report | displays a report based on all the instructions. |
| exit/quit/q | closes the application. |   

### Requirements : 
- Maven 
- Java 8 

### Run the tests.

`mvn test`


### Build the application.

`mvn package`

this will generate a jar with dependencies under `target` folder named : `jpm-test1-1.0-SNAPSHOT-jar-with-dependencies.jar`

### Run the application

`java -jar jpm-test1-1.0-SNAPSHOT-jar-with-dependencies.jar`
