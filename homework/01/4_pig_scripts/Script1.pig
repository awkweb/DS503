customers = LOAD '/input/customers.csv' USING PigStorage(',')
    AS (customerId:int, name:chararray, age:int, countryCode: int, salary:float);
transactions = LOAD '/input/transactions.csv' USING PigStorage(',')
    AS (transactionId:int, customerId:int, total:float, itemCount:int, description:chararray);
A = JOIN customers BY customerId, transactions BY customerId;
B = GROUP A BY (customers::customerId, customers::name);
C = FOREACH B GENERATE group.name, COUNT(A);
store C into '/output/job1';