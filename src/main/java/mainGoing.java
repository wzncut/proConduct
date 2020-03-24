import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class mainGoing {
    public static void main(String[] args) throws IOException {

        proConduct pro=new proConduct();
        pro.loadFile(proConduct.fileToPath("/moocfinal4.txt"));
        pro.outPut(pro.arr,"src/main/resources/result.txt");

        Process.loadData("src/main/resources/result.txt");
        //先确定分组数，取8
//        Process.loadData("D:\\JAVA\\Clustering\\result.txt");
//        Map.Entry<ArrayList<Double>, ArrayList<Double>> res= Process.cluster(5,16);
//        System.out.println(JSON.toJSONString(res));
        Map.Entry<Integer[],Double> re= Process.cluster(8);
        int[] arr =new int[re.getKey().length];
        int temp=0;
        for (Integer in:re.getKey()){
            arr[temp]=in;
            temp++;
        }

        pro.outPut(arr,"src/main/resources/group.txt");

        proConduct conduct=new proConduct();
        conduct.loadFile2(proConduct.fileToPath("/moocfinal4.txt"),proConduct.fileToPath("./group.txt"));
        conduct.groupOutPut(conduct.arrs,conduct.group);
    }
}
