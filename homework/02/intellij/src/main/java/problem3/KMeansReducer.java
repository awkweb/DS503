package problem3;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by tom on 2/18/17.
 */
public class KMeansReducer extends Reducer<Text, Text, IntWritable, Text> {

    private final static IntWritable keyOut = new IntWritable();
    private final static Text valueOut = new Text();

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Iterator<Text> iter = values.iterator();

        while (iter.hasNext()) {
            String pointString = iter.next().toString();
            String[] pointData = pointString.split(",");
        }

        context.write(keyOut, valueOut);
    }

    protected void cleanup(Context context) throws IOException, InterruptedException {
    }
}
