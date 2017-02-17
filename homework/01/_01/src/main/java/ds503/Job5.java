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
import java.util.*;

public class Job5 {

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

        private class CustomerTracker {
            int transactionCount;
            String customerName;

            CustomerTracker(int transactionCount, String customerName) {
                this.transactionCount = transactionCount;
                this.customerName = customerName;
            }
        }

        private List<CustomerTracker> customers = new ArrayList<CustomerTracker>();

        private int totalTransactions = 0;
        private int totalCustomers = 0;
        private int transactionCount = 0;

        protected void reduce(TextPair key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Iterator<Text> iter = values.iterator();
            String customerString = iter.next().toString();
            String[] customerData = customerString.split(",");
            String customerName = customerData[1];

            transactionCount = 0;
            while (iter.hasNext()) {
                iter.next();
                transactionCount += 1;
            }

            totalTransactions += transactionCount;
            totalCustomers += 1;

            CustomerTracker customerTracker = new CustomerTracker(transactionCount, customerName);
            customers.add(customerTracker);
        }

        protected void cleanup(Context context) throws IOException, InterruptedException {
            int meanTransactions = totalTransactions / totalCustomers;
            for (CustomerTracker customerTracker : customers) {
                if (customerTracker.transactionCount > meanTransactions) {
                    Text keyOut = new Text("");
                    Text valueOut = new Text(customerTracker.customerName);
                    context.write(null, valueOut);
                }
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
        job.setJarByClass(Job5.class);

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