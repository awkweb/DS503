package problem3;

import org.apache.hadoop.io.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by tom on 2/18/17.
 */
public final class PointWritable implements WritableComparable<PointWritable> {

    private IntWritable first;
    private IntWritable second;

    public PointWritable(int first, int second) {
        super();
        set(new IntWritable(first), new IntWritable(second));
    }

    public PointWritable(IntWritable first, IntWritable second) {
        set(first, second);
    }

    private void set(IntWritable first, IntWritable second) {
        this.first = first;
        this.second = second;
    }

    public IntWritable getFirst() {
        return first;
    }

    public IntWritable getSecond() {
        return second;
    }

    public final void write(DataOutput out) throws IOException {
        first.write(out);
        second.write(out);
    }

    public final void readFields(DataInput in) throws IOException {
        first.readFields(in);
        second.readFields(in);
    }

    @Override
    public int hashCode() {
        return first.hashCode() * 163 + second.hashCode();
    }

    public final int compareTo(PointWritable pw) {
        int cmp = first.compareTo(pw.first);
        if(cmp != 0) {
            return cmp;
        }
        return second.compareTo(pw.second);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PointWritable other = (PointWritable) obj;
        if (first == null && second == null) {
            if (other.first != null && other.second != null)
                return false;
        } else if (!first.equals(other.first) && !second.equals(other.second))
            return false;
        return true;
    }

    public static class Comparator extends WritableComparator {

        private static final IntWritable.Comparator INT_WRITABLE_COMPARATOR = new IntWritable.Comparator();

        public Comparator() {
            super(PointWritable.class);
        }

        @Override
        public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {

            try {
                int firstL1 = WritableUtils.decodeVIntSize(b1[s1]) + readVInt(b1, s1);
                int firstL2 = WritableUtils.decodeVIntSize(b2[s2]) + readVInt(b2, s2);
                int cmp = INT_WRITABLE_COMPARATOR.compare(b1, s1, firstL1, b2, s2, firstL2);
                if (cmp != 0) {
                    return cmp;
                }
                return INT_WRITABLE_COMPARATOR.compare(b1, s1 + firstL1, l1 - firstL1, b2, s2 + firstL2, l2 - firstL2);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    static {
        WritableComparator.define(PointWritable.class, new Comparator());
    }

    public static class FirstComparator extends WritableComparator {

        private static final IntWritable.Comparator INT_WRITABLE_COMPARATOR = new IntWritable.Comparator();

        public FirstComparator() {
            super(PointWritable.class);
        }

        @Override
        public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
            try {
                int firstL1 = WritableUtils.decodeVIntSize(b1[s1]) + readVInt(b1, s1);
                int firstL2 = WritableUtils.decodeVIntSize(b2[s2]) + readVInt(b2, s2);
                return INT_WRITABLE_COMPARATOR.compare(b1, s1, firstL1, b2, s2, firstL2);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            if (a instanceof PointWritable && b instanceof PointWritable) {
                return ((PointWritable) a).first.compareTo(((PointWritable) b).first);
            }
            return super.compare(a, b);
        }
    }

}
