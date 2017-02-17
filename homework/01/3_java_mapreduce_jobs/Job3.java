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
import java.util.Iterator;

public class Job3 {

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

        protected void reduce(TextPair key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Iterator<Text> iter = values.iterator();
            String customerString = iter.next().toString();
            String[] customerData = customerString.split(",");
            String customerId = customerData[0];
            String customerName = customerData[1];
            String customerSalary = customerData[4];

            int numTransactions = 0;
            float totalSum = 0;
            int minItems = -1;
            while (iter.hasNext()) {
                numTransactions += 1;
                String transactionString = iter.next().toString();
                String[] transactionData = transactionString.split(",");

                float sum = Float.parseFloat(transactionData[2]);
                totalSum += sum;

                int itemCount = Integer.parseInt(transactionData[3]);
                if (minItems == -1 || minItems > itemCount) {
                    minItems = itemCount;
                }
            }

            String transactionString = String.format("%s, %s, %s, %s, %s, %s", customerId, customerName, customerSalary,
                    numTransactions, totalSum, minItems);
            Text outValue = new Text(transactionString);
            context.write(null, outValue);
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
        job.setJarByClass(Job3.class);

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