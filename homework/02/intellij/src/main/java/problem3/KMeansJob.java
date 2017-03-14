package problem3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

/**
 * Created by tom on 2/18/17.
 */
public class KMeansJob {

    public static void main(String[] args) throws Exception {
        //https://thatsilentobserver.wordpress.com/2013/12/29/log4jwarn-no-appenders-could-be-found-for-logger-org-apache-hadoop-conf-configuration-deprecation/
        //https://github.com/thomasjungblut/mapreduce-kmeans/blob/master/src/de/jungblut/clustering/mapreduce/KMeansReducer.java
        //https://github.com/thomasjungblut/mapreduce-kmeans/blob/354dae5293de861c9f803ee62ae1ac13f33b1ec8/src/de/jungblut/clustering/model/ClusterCenter.java

        int iteration = 1;

        Configuration conf = new Configuration();
        conf.set("num.iteration", iteration + "");

        Job job = Job.getInstance(conf);
        String jobName = "Job 3 - K Means Clustering";
        job.setJobName(jobName);

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

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        job.setOutputKeyClass(CenterWritable.class);
        job.setOutputValueClass(PointWritable.class);

        job.waitForCompletion(true);

        long counter = job.getCounters().findCounter(KMeansReducer.Counter.CONVERGED).getValue();
        iteration++;
        while (counter > 0) {
            conf = new Configuration();
            conf.set("centroid.path", center.toString());
            conf.set("num.iteration", iteration + "");
            job = Job.getInstance(conf);
            job.setJobName(jobName + " " + iteration);

            job.setMapperClass(KMeansMapper.class);
            job.setReducerClass(KMeansReducer.class);
            job.setJarByClass(KMeansMapper.class);

            input = new Path("files/clustering/depth_" + (iteration - 1) + "/");
            output = new Path("files/clustering/depth_" + iteration);

            FileInputFormat.addInputPath(job, input);
            if (fs.exists(output)) {
                fs.delete(output, true);
            }

            FileOutputFormat.setOutputPath(job, output);

            job.setInputFormatClass(SequenceFileInputFormat.class);
            job.setOutputFormatClass(SequenceFileOutputFormat.class);

            job.setOutputKeyClass(CenterWritable.class);
            job.setOutputValueClass(PointWritable.class);

            job.waitForCompletion(true);
            iteration++;
            counter = job.getCounters().findCounter(KMeansReducer.Counter.CONVERGED).getValue();
        }

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
