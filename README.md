# BancX - Tech Test

## Overview
The project is built using maven and consist of mutliple modules, in order to separate all concerns completely the project was divided into 4 modules. 
* CPCommon - all the classes shared between the modules
* CPDatabaseService - H2 in memory database, configured to accept network connections (on port: 9090)
* CPLoanService - The loan service endpoint exposed at http://localhost:8090/CreditProvider
* CPPaymentService - The payment service endpoint exposed at http://localhost:8091/CreditProvider 

## Building
### Build requirements
The following was used to build the project, it is doubtful that the are absolutely crictical seeing no advanced features were used.
* Windows 11 
* Java JDK 21 
* Maven 3.9
* Docker desktop 4.23.0
* curl 8.10.1

### Steps to build
* Ensure that there are no programs running that are using the ports 8080, 8090, 8091, 9090.
* Make sure that docker service is running.
* Create a directory to house the project files in for the sake of brevity it's assumed to be `C:\workspace`.
* Open a command prompt and navigate to the directory and execute the following commands.
> `C:\workspace>git clone https://github.com/twoents/CreditAppBX.git`  
> --git output--  
> `C:\workspace>cd CreditAppBX`  
> `C:\workspace\CreditAppBX>mvn clean install`  
> --maven output--
* That will build all the projects and run the unit tests, all tests are performed against a "live" database and/or "live" services.

## Commandline testing
* Still assuming the directory layout above, open 4 Command prompt windows.
* Using Command prompt 1, go to the directory `C:\workspace\CreditAppBX\CPDatabaseService\target` and execute `java -jar CPDatabaseServer-0.0.1-SNAPSHOT-exec.jar`, that will start the h2 database server accepting connections on port 9090, the H2-console is available at http://localhost:8080/h2-console.
* Using Command prompt 2, go to the directory `C:\workspace\CreditAppBX\CPLoanService\target` and execute `java -jar CPLoanService-0.0.1-SNAPSHOT-exec.jar`. that will start the loan end point service.
* Using Command prompt 3, go to the directory `C:\workspace\CreditAppBX\CPPaymentService\target` and execute `java -jar CPPaymentService-0.0.1-SNAPSHOT-exec.jar`. that will start the payment end point service.
* Using Command prompt 4 the application can be tested from the command line using the curl tool.

The following shows the loans in the database, two loans are automatically created when the database starts.  
> `C:\workspace>curl.exe http://localhost:8090/CreditProvider/loans`
>   
> `[{"loanId":1,"loanAmount":10000.00,"term":12,"status":"ACTIVE"},{"loanId":2,"loanAmount":5000.00,"term":6,"status":"ACTIVE"}]`

The following command creates a third loan with an amount outstanding of 4000. this creates a loan with an id of 3 as the output shows.
> `C:\workspace>curl -X POST -H "Content-Type: application/json" -d "{\"loanAmount\": 4000.00, \"term\": 12, \"status\": \"ACTIVE\" }" http://localhost:8090/CreditProvider/loans`
>   
> `{"loanId":3,"loanAmount":4000.00,"term":12,"status":"ACTIVE"}`  

The next command will try to overpay the loan with an id of 3 by trying to make a payment of 6000.
> `C:\workspace>curl.exe -X POST -H "Content-Type: application/json" -d "{\"loanId\": 3, \"paymentAmount\": 6000.00 }" http://localhost:8091/Credi
tProvider/payments`  
>
> `{"loanId":3,"message":"Payment amount too large, total payments would exceed loan amount"}`

Now a sucessful payment where the amount is reduced but the loan remains active. 
> `C:\workspace>curl.exe -X POST -H "Content-Type: application/json" -d "{\"loanId\": 3, \"paymentAmount\": 2000.00 }" http://localhost:8091/Credi
tProvider/payments`
>
> `{"loanId":3,"amountRemaining":2000.00,"loanStatus":"ACTIVE"}`

Lastly a final payment of 2000 will settle the loan. 
> `C:\workspace>curl.exe -X POST -H "Content-Type: application/json" -d "{\"loanId\": 3, \"paymentAmount\": 2000.00 }" http://localhost:8091/Credi
tProvider/payments`
>
> `{"loanId":3,"amountRemaining":0.00,"loanStatus":"SETTLED"}`

Pressing Ctrl-C in Command prompt 1, 2 & 3 will shutdown the application.
