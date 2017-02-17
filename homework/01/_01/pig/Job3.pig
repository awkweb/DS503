customers = LOAD '/input/customers.csv' USING PigStorage(',')
    AS (customerId:int, name:chararray, age:int, countryCode: int, salary:float);
A = GROUP customers BY countryCode;
B = FOREACH A GENERATE group, COUNT(customers) as countryCodeCount;
C = FILTER B BY (countryCodeCount < 2000) OR (countryCodeCount > 5000);
store C into '/output/job3';