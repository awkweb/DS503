customers = LOAD '/input/customers.csv' USING PigStorage(',')
    AS (customerId:int, name:chararray, age:int, countryCode: int, salary:float);
transactions = LOAD '/input/transactions.csv' USING PigStorage(',')
    AS (transactionId:int, customerId:int, total:float, itemCount:int, description:chararray);
A = JOIN customers BY customerId, transactions BY customerId USING 'replicated';
B = GROUP A BY (customers::customerId, customers::name, customers::salary);
C = FOREACH B GENERATE group.customerId, group.name, group.salary, COUNT(A), SUM(A.total), MIN(A.itemCount);
store C into '/output/job2';