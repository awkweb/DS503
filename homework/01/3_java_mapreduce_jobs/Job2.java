import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Job2 {

    public static class TransactionMapper extends Mapper<Object, Text, Text, Text>{

        private final static Text customerId = new Text();
        private Text transaction = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String transactionString = value.toString();
            String[] transactionData = transactionString.split(",");

            customerId.set(transactionData[1]);
            transaction.set(value);
            context.write(customerId, transaction);
        }
    }

    public static class TransactionReducer extends Reducer<Text, Text, Text, Text> {

        private class CustomerItem {
            String customerId;
            int numTransactions;
            float totalSum;

            CustomerItem(String customerId, int numTransactions, float totalSum) {
                this.customerId = customerId;
                this.numTransactions = numTransactions;
                this.totalSum = totalSum;
            }
        }

        private List<CustomerItem> customers = new ArrayList<CustomerItem>();

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Iterator<Text> iter = values.iterator();

            int numTransactions = 0;
            float totalSum = 0;
            while (iter.hasNext()) {
                String transactionString = iter.next().toString();
                String[] transactionData = transactionString.split(",");

                numTransactions += 1;

                float sum = Float.parseFloat(transactionData[2]);
                totalSum += sum;
            }

            CustomerItem customerItem = new CustomerItem(key.toString(), numTransactions, totalSum);
            customers.add(customerItem);
        }

        protected void cleanup(Context context) throws IOException, InterruptedException {
            for (CustomerItem customerItem : customers) {
                Text keyOut = new Text(customerItem.customerId);

                String joinString = String.format(", %s, %s", customerItem.numTransactions, customerItem.totalSum);
                Text valueOut = new Text(joinString);
                context.write(null, valueOut);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "job 2");
        job.setJarByClass(Job2.class);

        job.setMapperClass(TransactionMapper.class);

        job.setCombinerClass(TransactionReducer.class);

        job.setReducerClass(TransactionReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
