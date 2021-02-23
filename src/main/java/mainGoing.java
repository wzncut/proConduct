import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class mainGoing {
    public static void main(String[] args) throws IOException {

//先离散化
//        proConduct pro=new proConduct();
//        pro.loadFile(proConduct.fileToPath("/moocfinal4.txt"));
//        //离散化处理
//        pro.outPut(pro.arr,"src/main/resources/result.txt");


//        确定分组数，取8
        Process.loadData("src/main/resources/result.txt");
        Map.Entry<ArrayList<Double>, ArrayList<Double>> res= Process.cluster(2,15);
        System.out.println(JSON.toJSONString(res));


//        Process.loadData("src/main/resources/result.txt");
//
//        Map.Entry<Integer[],Double> re= Process.cluster(8);
//        int[] arr =new int[re.getKey().length];
//        int temp=0;
//        for (Integer in:re.getKey()){
//            arr[temp]=in;
//            temp++;
//        }
//        //输出分组结果
//        pro.outPut(arr,"src/main/resources/group.txt");
//



//        proConduct conduct=new proConduct();
//        conduct.loadFile2(proConduct.fileToPath("/moocfinal4.txt"),proConduct.fileToPath("./group.txt"));
//        //对数据集进行聚类（输出）
//        conduct.groupOutPut(conduct.arrs,conduct.group);


        proConduct conduct = new proConduct();
        conduct.loadFile3(proConduct.fileToPath("/result.txt"),proConduct.fileToPath("/group.txt"),"src/main/resources/ANN.txt");
    }
}
