package problem3;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * Created by tom on 2/18/17.
 */
public class KMeansMapper extends Mapper<Object, Text, IntWritable, Text>  {

    private final static IntWritable keyOut = new IntWritable();
    private final static Text valueOut = new Text();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
    }

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        FileSplit fsFileSplit = (FileSplit) context.getInputSplit();
        String filename = context.getConfiguration().get(fsFileSplit.getPath().getParent().getName());

        System.out.println(value);

        String pointString = value.toString();
        String[] pointData = pointString.split(",");

        context.write(keyOut, valueOut);
    }
}
