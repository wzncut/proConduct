import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.AbstractMap.SimpleEntry;

class SuperHy extends Point {
    private double radius;
    private LinkedList<Integer> instances;
    private SuperHy[] children;

    @Override
    public String toString() {
        return "SuperHy{" +
                "radius=" + radius +
                ", instances=" + instances +
                ", children=" + Arrays.toString(children) +
                ", sumOfPoints=" + Arrays.toString(sumOfPoints) +
                '}';
    }

    private double[] sumOfPoints;
    static int COUNT = 0, ALL_COUNT = 0;

    SuperHy(Point center, double r, LinkedList<Integer> ins){
        super(center.pos);
        this.radius = r;
        this.instances = ins;
        sumOfPoints = new double[Process.DIMENSION];
    }

    SuperHy(){
        super(new double[Process.DIMENSION]);
        instances = new LinkedList<Integer>();
        sumOfPoints = new double[Process.DIMENSION];
    }

    //计算每一个特征出现的个数存在sumOfPoints中
    void addInstance(int index){
        instances.add(index);
        double[] pos = Process.INSTANCES.get(index).getPosition();
        for(int i = 0; i < Process.DIMENSION; i++){
            sumOfPoints[i] += pos[i];
        }
    }

    void endAdding(){
        int size = instances.size();
        for(int i = 0; i < Process.DIMENSION; i++){
            this.pos[i] = this.sumOfPoints[i] / size;
        }
        //计算半径
        this.radius = Point.euclideanDistance(this, Process.INSTANCES.get(this.getFarestPoint(this)));
    }

    int size(){
        return instances.size();
    }

    double maxDistance(Point p){
        return radius + Point.euclideanDistance(p, this);
    }

    double minDistance(Point p){
        return Point.euclideanDistance(p, this) - radius;
    }

    //如果不落在单独的cluster中，就返回-1 否则返回cluster center的index
    int isInSingleCluster(){
        ALL_COUNT++;
        PriorityQueue<Entry<Integer, Double>> maxpq = new PriorityQueue<Entry<Integer, Double>>(Process.CENTERS.size(), new Comparator<Entry<Integer, Double>>(){
            public int compare(Entry<Integer, Double> e1, Entry<Integer, Double> e2){
                double d1 = e1.getValue(), d2 = e2.getValue();
                if(d1 > d2){
                    return 1;
                }
                if(d1 < d2){
                    return -1;
                }
                return 0;
            }
        });
        PriorityQueue<Entry<Integer, Double>> minpq = new PriorityQueue<Entry<Integer, Double>>(Process.CENTERS.size(), new Comparator<Entry<Integer, Double>>(){
            public int compare(Entry<Integer, Double> e1, Entry<Integer, Double> e2){
                double d1 = e1.getValue(), d2 = e2.getValue();
                if(d1 > d2){
                    return 1;
                }
                if(d1 < d2){
                    return -1;
                }
                return 0;
            }
        });
        int index = 0;
        for(Clustering cen : Process.CENTERS){

            maxpq.add(new SimpleEntry<Integer, Double>(index, this.maxDistance(cen)));
            minpq.add(new SimpleEntry<Integer, Double>(index, this.minDistance(cen)));
            index++;
        }
        Entry<Integer, Double> the = maxpq.poll();
        index = the.getKey();
        double theDist = the.getValue();
        while((the = minpq.poll()) != null){
            int ind = the.getKey();
            double dis = the.getValue();
            if(theDist < dis){
                if(ind != index){
                    COUNT++;
                    return index;
                }
                else
                    continue;
            }
            else{
                if(ind == index)
                    continue;
                return -1;
            }
        }
        return -1;
    }

    //最小圆覆盖问题，
    private int getFarestPoint(Point p){
        double maxDist = 0.0;
        int maxIndex = -1;
        for(int i : this.instances){
            Point pp = Process.INSTANCES.get(i);
            double dist = Point.euclideanDistance(p, pp);
            if(dist >= maxDist){
                maxDist = dist;
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    //split and store it to this node's children field, & return the children.
    //将大球分成小球
    SuperHy[] split(){
        int firstCenter = this.getFarestPoint(this);
        Point fir = Process.INSTANCES.get(firstCenter);
        int secondCenter = this.getFarestPoint(fir);
        Point sec = Process.INSTANCES.get(secondCenter);
        this.children = new SuperHy[2];
        this.children[0] = new SuperHy();
        this.children[1] = new SuperHy();
        this.children[0].addInstance(firstCenter);
        this.children[1].addInstance(secondCenter);
        for(int i : this.instances){
            if(i == firstCenter || i == secondCenter)
                continue;
            Point p = Process.INSTANCES.get(i);
            double dist1 = Point.euclideanDistance(p, fir),
                    dist2 = Point.euclideanDistance(p, sec);
            if(dist1 < dist2){
                this.children[0].addInstance(i);
            }
            else{
                this.children[1].addInstance(i);
            }
        }
        this.children[0].endAdding();
        this.children[1].endAdding();
        return this.children;
    }

    SuperHy[] getChildren(){
        return this.children;
    }

    static void locateAndAssign(SuperHy hp){
        int clusterIndex = hp.isInSingleCluster();
        if(clusterIndex != -1){
            Clustering cc = Process.CENTERS.get(clusterIndex);
            for(int pi : hp.instances){
                cc.addPointToCluster(pi);
            }
            return;
        }
        if(hp.children == null){
            for(int pi : hp.instances){
                Point p = Process.INSTANCES.get(pi);
                double minDist = Double.MAX_VALUE;
                int minCenIndex = 0, index = 0;
                for(Clustering cc : Process.CENTERS){
                    double dist = Point.euclideanDistance(p, cc);
                    if(dist < minDist){
                        minDist = dist;
                        minCenIndex = index;
                    }
                    index++;
                }
                Clustering cen = Process.CENTERS.get(minCenIndex);
                cen.addPointToCluster(pi);
            }
        }
        else{
            for(SuperHy chp : hp.children){
                SuperHy.locateAndAssign(chp);
            }
        }
    }

}
