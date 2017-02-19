package problem3;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by tom on 2/18/17.
 */
public final class Center implements WritableComparable<Center> {

    private PointWritable center;
    private int kTimesIncremented = 1;
    private int clusterIndex;

    public Center() {
        super();
    }

    public Center(PointWritable center) {
        super();
        this.center = center.deepCopy();
    }

    public Center(Center center) {
        super();
        this.center = center.center.deepCopy();
        this.kTimesIncremented = center.kTimesIncremented;
    }

    public Center(PointWritable center) {
        super();
        this.center = center.getVector().deepCopy();
    }

    public final void plus(PointWritable c) {
        plus(c.getVector());
    }

    public final void plus(PointWritable c) {
        center = center.add(c);
        kTimesIncremented++;
    }

    public final void plus(Center c) {
        kTimesIncremented += c.kTimesIncremented;
        center = center.add(c.getCenterVector());
    }

    public final void divideByK() {
        center = center.divide(kTimesIncremented);
    }

    public final boolean converged(Center c) {
        return calculateError(c.getCenterVector()) > 0;
    }

    public final boolean converged(Center c, double error) {
        return calculateError(c.getCenterVector()) > error;
    }

    public final double calculateError(PointWritable v) {
        return Math.sqrt(center.subtract(v).abs().sum());
    }

    @Override
    public final void write(DataOutput out) throws IOException {
        PointWritable.writePoint(center, out);
        out.writeInt(kTimesIncremented);
        out.writeInt(clusterIndex);
    }

    @Override
    public final void readFields(DataInput in) throws IOException {
        this.center = PointWritable.readVector(in);
        kTimesIncremented = in.readInt();
        clusterIndex = in.readInt();
    }

    @Override
    public final int compareTo(Center o) {
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
        Center other = (Center) obj;
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