package ds503;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Job4 {

    public static class CustomerMapper extends Mapper<Object, Text, TextPair, Text> {

        private final static TextPair customerId = new TextPair();
        private Text customer = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String customerString = value.toString();
            String[] customerData = customerString.split(",");
            String id = customerData[0];

            customerId.set(new Text(id), new Text("0"));
            customer.set(value);
            context.write(customerId, customer);
        }
    }

    public static class TransactionMapper extends Mapper<Object, Text, TextPair, Text> {

        private final static TextPair transactionId = new TextPair();
        private Text transaction = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String transactionString = value.toString();
            String[] transactionData = transactionString.split(",");
            String id = transactionData[1];

            transactionId.set(new Text(id), new Text("1"));
            transaction.set(value);
            context.write(transactionId, transaction);
        }
    }

    public static class JoinReducer extends Reducer<TextPair, Text, Text, Text> {

        private class CountryCodeItem {
            int countryCode;
            int numberOfCustomers;
            float minTransTotal;
            float maxTransTotal;

            CountryCodeItem(int countryCode, int numberOfCustomers, float minTransTotal, float maxTransTotal) {
                this.countryCode = countryCode;
                this.numberOfCustomers = numberOfCustomers;
                this.minTransTotal = minTransTotal;
                this.maxTransTotal = maxTransTotal;
            }
        }

        private Map<Integer, CountryCodeItem> countryCodeMap = new HashMap<Integer, CountryCodeItem>();

        protected void reduce(TextPair key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Iterator<Text> iter = values.iterator();
            String customerString = iter.next().toString();
            String[] customerData = customerString.split(",");
            Integer countryCode = Integer.parseInt(customerData[3]);

            float minTransTotal = -1;
            float maxTransTotal = -1;
            while (iter.hasNext()) {
                String transactionString = iter.next().toString();
                String[] transactionData = transactionString.split(",");

                float transTotal = Float.parseFloat(transactionData[2]);
                if (minTransTotal == -1 || minTransTotal > transTotal) {
                    minTransTotal = transTotal;
                }

                if (maxTransTotal == -1 || maxTransTotal < transTotal) {
                    maxTransTotal = transTotal;
                }
            }

            if (countryCodeMap.containsKey(countryCode)) {
                CountryCodeItem countryCodeItem = countryCodeMap.get(countryCode);
                countryCodeItem.numberOfCustomers += 1;
                if (countryCodeItem.minTransTotal > minTransTotal) {
                    countryCodeItem.minTransTotal = minTransTotal;
                }

                if (countryCodeItem.maxTransTotal < maxTransTotal) {
                    countryCodeItem.maxTransTotal = maxTransTotal;
                }
            } else {
                CountryCodeItem countryCodeItem = new CountryCodeItem(countryCode, 1, minTransTotal, maxTransTotal);
                countryCodeMap.put(countryCode, countryCodeItem);
            }
        }

        protected void cleanup(Context context) throws IOException, InterruptedException {
            for (CountryCodeItem countryCodeItem : countryCodeMap.values()) {
                Text keyOut = new Text(Integer.toString(countryCodeItem.countryCode));

                String transactionString = String.format("%s, %s, %s, %s", countryCodeItem.countryCode,
                        countryCodeItem.numberOfCustomers, countryCodeItem.minTransTotal, countryCodeItem.maxTransTotal);
                Text valueOut = new Text(transactionString);

                context.write(null, valueOut);
            }
        }
    }

    public class KeyPartitioner extends Partitioner<TextPair, Text> {
        @Override
        public int getPartition(TextPair key, Text value, int numPartitions) {
            return (key.getFirst().hashCode() & Integer.MAX_VALUE) % numPartitions;
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "job 3");
        job.setJarByClass(Job4.class);

        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, TransactionMapper.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, CustomerMapper.class);

        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        job.setPartitionerClass(KeyPartitioner.class);
        job.setGroupingComparatorClass(TextPair.FirstComparator.class);

        job.setMapOutputKeyClass(TextPair.class);
        job.setReducerClass(JoinReducer.class);
        job.setOutputKeyClass(Text.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}