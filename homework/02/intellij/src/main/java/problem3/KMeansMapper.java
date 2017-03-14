package problem3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 2/18/17.
 */
public class KMeansMapper extends Mapper<CenterWritable, PointWritable, CenterWritable, PointWritable>  {

    private final List<CenterWritable> centers = new ArrayList<CenterWritable>();
    private DistanceCalculator distanceCalculator = new DistanceCalculator();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        Configuration conf = context.getConfiguration();
        Path centroids = new Path(conf.get("centroid.path"));
        FileSystem fs = FileSystem.get(conf);

        SequenceFile.Reader reader = new SequenceFile.Reader(fs, centroids, conf);
        CenterWritable key = new CenterWritable();
        IntWritable value = new IntWritable();
        int index = 0;
        while (reader.next(key, value)) {
            CenterWritable centerWritable = new CenterWritable(key);
            centerWritable.setClusterIndex(index++);
            centers.add(centerWritable);
        }
    }

    @Override
    public void map(CenterWritable key, PointWritable value, Context context) throws IOException, InterruptedException {
        CenterWritable closestCenter = null;
        double nearestDistance = Double.MAX_VALUE;
        for (CenterWritable c : centers) {
            double dist = distanceCalculator.measureDistance(c.getCenterVector(), value);
            if (closestCenter == null) {
                closestCenter = c;
                nearestDistance = dist;
            } else {
                if (nearestDistance > dist) {
                    closestCenter = c;
                    nearestDistance = dist;
                }
            }
        }
        context.write(closestCenter, value);
    }
}
