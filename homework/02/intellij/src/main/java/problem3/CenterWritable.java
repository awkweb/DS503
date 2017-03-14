package problem3;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by tom on 2/18/17.
 */
public final class CenterWritable implements WritableComparable<CenterWritable> {

    private PointWritable center;
    private int kTimesIncremented = 1;
    private int clusterIndex;

    public CenterWritable() {
        super();
    }

    public CenterWritable(CenterWritable centerWritable) {
        super();
        this.center = new PointWritable(centerWritable.center.getFirst(), centerWritable.center.getSecond());
    }

    public CenterWritable(PointWritable center, int kTimesIncremented, int clusterIndex) {
        super();
        this.center = center;
        this.kTimesIncremented = kTimesIncremented;
        this.clusterIndex = clusterIndex;
    }

    public final void plus(PointWritable pointWritable) {
        int first = Integer.parseInt(center.getFirst().toString()) + Integer.parseInt(pointWritable.getFirst().toString());
        int second = Integer.parseInt(center.getSecond().toString()) + Integer.parseInt(pointWritable.getSecond().toString());
        center = new PointWritable(first, second);
        kTimesIncremented++;
    }

    public final void divideByK() {
        int first = Integer.parseInt(center.getFirst().toString()) / kTimesIncremented;
        int second = Integer.parseInt(center.getSecond().toString()) / kTimesIncremented;
        center = new PointWritable(first, second);
    }

    public final boolean converged(CenterWritable centerWritable) {
        return calculateError(centerWritable.getCenterVector()) > 0;
    }

    public final boolean converged(CenterWritable centerWritable, double error) {
        return calculateError(centerWritable.getCenterVector()) > error;
    }

    public final double calculateError(PointWritable pointWritable) {
        double first = Double.parseDouble(center.getFirst().toString()) - Double.parseDouble(pointWritable.getFirst().toString());
        double second = Double.parseDouble(center.getSecond().toString()) - Double.parseDouble(pointWritable.getSecond().toString());
        double sum = Math.abs(first) + Math.abs(second);
        return Math.sqrt(sum);
    }

    public final void write(DataOutput out) throws IOException {
        center.write(out);
        out.writeInt(kTimesIncremented);
        out.writeInt(clusterIndex);
    }

    public final void readFields(DataInput in) throws IOException {
        this.center.readFields(in);
        kTimesIncremented = in.readInt();
        clusterIndex = in.readInt();
    }

    public final int compareTo(CenterWritable o) {
        return Integer.compare(clusterIndex, o.clusterIndex);
    }

    /**
     * @return the center
     */
    public final PointWritable getCenterVector() {
        return center;
    }

    /**
     * @return the index of the cluster in a datastructure.
     */
    public int getClusterIndex() {
        return clusterIndex;
    }

    public void setClusterIndex(int clusterIndex) {
        this.clusterIndex = clusterIndex;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((center == null) ? 0 : center.hashCode());
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CenterWritable other = (CenterWritable) obj;
        if (center == null) {
            if (other.center != null)
                return false;
        } else if (!center.equals(other.center))
            return false;
        return true;
    }

    @Override
    public final String toString() {
        return "ClusterCenter [center=" + center + "]";
    }

}