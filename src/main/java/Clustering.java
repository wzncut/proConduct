import java.util.ArrayList;

//ÿ�ε�����ʼ��clusterPointsΪ�գ�����Ϊ�µ�mean��
class Clustering extends Point {
    private ArrayList<Integer> clusterPoints;
    private double[] sumOfPoints;
    Clustering(Point p){
        super(p.pos);
        clusterPoints = new ArrayList<Integer>();
        this.sumOfPoints = new double[this.dimension];
    }
    void addPointToCluster(int index){
        Point p = Process.INSTANCES.get(index);
        clusterPoints.add(index);
        double[] po = p.getPosition();
        for(int i = 0; i < this.dimension; ++i){
            sumOfPoints[i] += po[i];
        }
    }

    Clustering getNewCenter(){
        double[] pos = new double[Process.DIMENSION];
        for(int i = 0; i < this.dimension; ++i){
            pos[i] = sumOfPoints[i] / this.clusterPoints.size();
        }
        return new Clustering(new Point(pos));
    }

    double evaluate(){
        double ret = 0.0;
        for(int in : clusterPoints){
            ret += Point.squareDistance(Process.INSTANCES.get(in), this);
        }
        return ret;
    }
    ArrayList<Integer> belongedPoints(){
        return new ArrayList<Integer>(this.clusterPoints);
    }
}
