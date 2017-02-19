package problem3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Created by tom on 2/18/17.
 */
public class KMeansJob {

    public static void main(String[] args) throws Exception {
        int iteration = 1;

        Configuration conf = new Configuration();
        conf.set("num.iteration", iteration + "");

        Job job = Job.getInstance(conf);
        job.setJobName("Job 3 - K Means Clustering");

        job.setMapperClass(KMeansMapper.class);
        job.setReducerClass(KMeansReducer.class);
        job.setJarByClass(KMeansJob.class);

        // File input/output
        Path input = new Path("./input/problem3/points_data.csv");
        Path output = new Path("./output/problem3/");
        Path center = new Path("./input/problem3/k_data.csv");
        conf.set("centroid.path", center.toString());

        // Remove output if it exists, so Hadoop doesn't complain
        FileInputFormat.addInputPath(job, input);
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(output)) {
            fs.delete(output, true);
        }

        FileInputFormat.addInputPath(job, input);
        FileOutputFormat.setOutputPath(job, output);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
