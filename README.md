# n26-challenge
n26-challenge

SpringBoot application exposing 2 rest endpoint 

POST /transaction  
{  
 "amount" : 12.3,  
 "timestamp" : 1478192204000   
}

GET /statistics
{  
  "sum": 1000,  
  "avg": 100,  
  "max": 200,  
  "min": 50,  
  "count": 10  
}


## The project
This project is based on SpringBoot and maven.   

## Build it
mvn clean install   

## run it
java -jar target/challenge-realtime-statistis-1.0-SNAPSHOT.jar   
