package testuggine.timepatterns.src;

public class MovingAverage {
	float[] a;
	int N; // size of moving avg filter
	int i;
	float sum;
	
	public MovingAverage(int N) {
		this.N = N;
		this.i = 0;
		this.a = new float[N];
		this.sum = 0;
	}
	/** Advances && returns the new running avg */
	public float advance(float newElement) {
            sum -= a[i % N];
            a[i % N] = newElement;
            sum += a[i % N];
            i++;
            return sum / N;
	}
	public int size() {
		return N;
	}
	/** If less than N values were inserted, the filter is not ready */
	public boolean isBootstrapped() {
		return i >= N;
	}
	
	public double[] a() {
		double[] b = new double[N];
		for (int i=0; i < N; i++)
			b[i] = a[i];
		return b;
	}
	public static float truncate(float val) {
		return (float) (Math.round(val*10000.0)/10000.0); 
	}
}
