import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

class LoadProperties{
    static String load(String p){
        Properties prop = new Properties();
        try {
            InputStream is = new LoadProperties().getClass().getResourceAsStream("conf.properties");
            prop.load(new BufferedReader(new InputStreamReader(is)));
            is.close();
            return prop.getProperty(p);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}

public class Process {
    static ArrayList<Clustering> CENTERS = new ArrayList<Clustering>();
    //数据集总数
    static ArrayList<Point> INSTANCES = new ArrayList<Point>();
    static ArrayList<Clustering> PRE_CENS;
    //特征数
    static int DIMENSION;
    static int MAX_INSTANCE_NUM_NOT_SPLIT = 5;
    static SuperHy BALL_TREE;
    static int TRY_TIMES = 10;
    //map cluster center results to its evaluation
    static ArrayList<Entry<ArrayList<Clustering>, Double>> RESULTS = new ArrayList<Entry<ArrayList<Clustering>, Double>>(TRY_TIMES);

    static boolean timeToEnd(){
        if(PRE_CENS == null)
            return false;
        for(Clustering cc : Process.CENTERS){
            if(!PRE_CENS.contains(cc))
                return false;
        }
        return true;
    }

    //gives your dataset's path and this function will build the internal data structures.
    public static void loadData(String path) throws IOException{
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
        String line;
        while((line = r.readLine()) != null){
            String[] fs = line.split(" +");
            double[] pos = new double[fs.length];

            int i = 0;
            for(String s : fs){
                pos[i++] = Double.valueOf(s + ".0");
            }
            Process.DIMENSION = fs.length;
            Process.INSTANCES.add(new Point(pos));
        }
        r.close();

        BALL_TREE = BallTreeStruct.buildAnInstance(null);
    }

    static double evaluate(ArrayList<Clustering> cens){
        double ret = 0.0;
        for(Clustering cc : cens){
            ret += cc.evaluate();
        }
        return ret;
    }

    /**
     * @param k  the initial number of clustering centers
     * @return an entry:the key is the result of clustering.The label starts from 0.The value is the evaluation of the clustering result
     */
    public static Entry<Integer[], Double> cluster(int k) {
        for(int t = 0; t < Process.TRY_TIMES; t++){
            //random pick the cluster centers
            CENTERS.clear();
            if(PRE_CENS != null)
                PRE_CENS = null;
            Random rand = new Random();
            HashSet<Integer> rSet = new HashSet<Integer>();
            int size = INSTANCES.size();
            //生成k个初始点
            while(rSet.size() < k){
                rSet.add(rand.nextInt(size));
            }
            //将初始点放入Process
            for(int index : rSet){
                Process.CENTERS.add(new Clustering(Process.INSTANCES.get(index)));
            }

            //迭代直到收敛
            while(!timeToEnd()){
                SuperHy.locateAndAssign(BALL_TREE);

                //记录中心点
                PRE_CENS = new ArrayList<Clustering>(CENTERS);
                ArrayList<Clustering> newCenters = new ArrayList<Clustering>();
                for(Clustering cc : CENTERS){
                    cc = cc.getNewCenter();
                    newCenters.add(cc);
                }
                CENTERS = newCenters;
            }
            Process.RESULTS.add(new SimpleEntry<ArrayList<Clustering>, Double>(PRE_CENS, Process.evaluate(PRE_CENS)));
            SuperHy.ALL_COUNT = 0;
            SuperHy.COUNT = 0;
        }

        //找到多次试验中评分最小的
        double minEvaluate = Double.MAX_VALUE;
        int minIndex = 0, i = 0;
        for(Entry<ArrayList<Clustering>, Double> entry : RESULTS){
            double e = entry.getValue();
            if(e < minEvaluate){
                minEvaluate = e;
                minIndex = i;
            }
            i++;
        }
        CENTERS = RESULTS.get(minIndex).getKey();
        double evaluate = RESULTS.get(minIndex).getValue();
        //将instance对应的聚类编号返回
        Integer[] ret = new Integer[INSTANCES.size()];
        for(int cNum = 0; cNum < CENTERS.size(); cNum++){
            Clustering cc = CENTERS.get(cNum);
            for(int pi : cc.belongedPoints()){
                ret[pi] = cNum;
            }
        }
        return new SimpleEntry<Integer[], Double>(ret, evaluate);
    }

    /**gives the evaluation and differential of each k in specific range.you can use these infos to choose a good k for your clustering
     * @param startK  gives the start point of k for the our try on k(inclusive)
     * @param endK    gives the end point(exclusive)
     * @return Entry's key is the evaluation of clustering of each k.The value is the differential of the evaluations--evaluation of k(i) - evaluation of k(i+1) for i in range(startK, endK - 1)
     * */
    public static Entry<ArrayList<Double>, ArrayList<Double>> cluster(int startK, int endK) {
        ArrayList<Integer[]> results = new ArrayList<Integer[]>();
        ArrayList<Double> evals = new ArrayList<Double>();
        for(int k = startK; k < endK; k++){
            System.out.println("now k = " + k);
            Entry<Integer[], Double> en = Process.cluster(k);
            results.add(en.getKey());
            evals.add(en.getValue());
        }

        ArrayList<Double> subs = new ArrayList<Double>();
        for(int i = 0; i < evals.size() - 1; i++){
            subs.add(evals.get(i) - evals.get(i + 1));
        }

        return new SimpleEntry<ArrayList<Double>, ArrayList<Double>>(evals, subs);

    }
}
