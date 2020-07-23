import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * 预处理，将txt转化成矩阵，再输出txt
 */
public class proConduct {

    public int maxItem = 0;

    public int tidsCount =0;

    int[][] arr;

    ArrayList<Integer> group =new ArrayList<Integer>();

    ArrayList<ArrayList<Integer>> arrs=new ArrayList<ArrayList<Integer>>();
    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = proConduct.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }

    //读取文件(离散化处理)
    public void loadFile(String path) throws IOException {
        BufferedReader myInput = null;
        BufferedReader myInput2 = null;
        try {
            FileInputStream fin1 = new FileInputStream(new File(path));
            FileInputStream fin2 = new FileInputStream(new File(path));
            myInput = new BufferedReader(new InputStreamReader(fin1));
            String thisLine;
            int t=0;
            while ((thisLine = myInput.readLine()) != null) {
                if (thisLine.isEmpty() ||
                        thisLine.charAt(0) == '#' || thisLine.charAt(0) == '%'
                        || thisLine.charAt(0) == '@') {
                    continue;
                }
                count(thisLine.split(" "));
            }
            arr = new int[tidsCount][maxItem];
            String[] line;
            myInput2 = new BufferedReader(new InputStreamReader(fin2));
            while ((thisLine = myInput2.readLine()) != null) {
                if (thisLine.isEmpty() ||
                        thisLine.charAt(0) == '#' || thisLine.charAt(0) == '%'
                        || thisLine.charAt(0) == '@') {
                    continue;
                }
              line=thisLine.split(" ");
                for (String in:line){
                    arr[t][Integer.parseInt(in)-1]=1;
                }
                t++;
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (myInput != null&& null!=myInput2) {
                myInput.close();
                myInput2.close();
            }
        }
    }
    //读取文件（读取原数据集文件和对应的分组结果文件）
    public void loadFile2(String path1,String path2) throws IOException {
        BufferedReader myInput = null;
        BufferedReader myInput2 = null;
        BufferedReader myInput3 = null;
        try {
            FileInputStream fin1 = new FileInputStream(new File(path1));
            FileInputStream fin2 = new FileInputStream(new File(path1));
            FileInputStream fin3 = new FileInputStream(new File(path2));
            myInput = new BufferedReader(new InputStreamReader(fin1));
            String thisLine;
            int i=0;
            int j=0;
            while ((thisLine = myInput.readLine()) != null) {
                if (thisLine.isEmpty() ||
                        thisLine.charAt(0) == '#' || thisLine.charAt(0) == '%'
                        || thisLine.charAt(0) == '@') {
                    continue;
                }
                count(thisLine.split(" "));
            }
            String[] line;
            myInput2 = new BufferedReader(new InputStreamReader(fin2));
            while ((thisLine = myInput2.readLine()) != null) {
                if (thisLine.isEmpty() ||
                        thisLine.charAt(0) == '#' || thisLine.charAt(0) == '%'
                        || thisLine.charAt(0) == '@') {
                    continue;
                }
                line=thisLine.split(" ");
                ArrayList<Integer> a= new ArrayList<Integer>();
                for (String in:line){
                    a.add(Integer.parseInt(in));
                }
                arrs.add(a);
            }
            myInput3 = new BufferedReader(new InputStreamReader(fin3));
            while ((thisLine = myInput3.readLine()) != null) {
                if (thisLine.isEmpty() ||
                        thisLine.charAt(0) == '#' || thisLine.charAt(0) == '%'
                        || thisLine.charAt(0) == '@') {
                    continue;
                }
                line=thisLine.split(" ");
                for (String in:line){
                    group.add(Integer.parseInt(in));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (myInput != null&& null!=myInput2) {
                myInput.close();
                myInput2.close();
            }
        }
    }

    public void loadFile3(String path1,String group,String outPath) throws IOException {
        BufferedReader myInput = null;
        BufferedReader groupInput = null;
        FileWriter fw =null;
        try {
            FileInputStream fin1 = new FileInputStream(new File(path1));
            FileInputStream groupFin = new FileInputStream(new File(group));
            myInput = new BufferedReader(new InputStreamReader(fin1));
            groupInput = new BufferedReader(new InputStreamReader(groupFin));

            fw = new FileWriter(new File(outPath));
            BufferedWriter bw = new BufferedWriter(fw);
            String thisLine1;
            String thisLine2;
            while (null != ((thisLine1 = myInput.readLine())) && (null!=(thisLine2=groupInput.readLine()))){
                String temp=null;
                System.out.println(thisLine1);
                switch (thisLine2){
                    case "0 ":
                        temp="one";
                        break;
                    case "1 ":
                        temp="two";
                        break;
                    case "2 ":
                        temp="three";
                        break;
                    case "3 ":
                        temp="four";
                        break;
                    case "4 ":
                        temp="five";
                        break;
                    case "5 ":
                        temp="six";
                        break;
                    case "6 ":
                        temp="seven";
                        break;
                    case "7 ":
                        temp="eight";
                        break;
                }
                bw.write(thisLine1+temp+"\n");
                bw.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if ( null!= myInput && null!= groupInput && null!= fw) {
                myInput.close();
                groupInput.close();
                fw.close();
            }
        }

    }

    //输出（离散化输出）
    public void outPut(int[][] arr,String targetFile) throws IOException {
        File file = new File(targetFile);  //存放数组数据的文件

        FileWriter out = new FileWriter(file);  //文件写入流

        for(int i=0;i<tidsCount;i++){
            for(int j=0;j<maxItem;j++){
                out.write(arr[i][j]+" ");
            }
            out.write("\r\n");
        }
        out.close();
    }
    //输出（输出分组结果）
    public void outPut(int[] arr,String targetFile) throws IOException {
        File file = new File(targetFile);  //存放数组数据的文件

        FileWriter out = new FileWriter(file);  //文件写入流

        for (int value : arr) {
            out.write(value + " ");
            out.write("\r\n");
        }
        out.close();
    }

    //生成分组文件
    public void groupOutPut(ArrayList<ArrayList<Integer>> arrs,ArrayList<Integer> group) throws IOException {
        int max=0;
        int temp;
        for (Object in:group){
            temp=Integer.parseInt(in.toString());
            if (temp>max){
                max=temp;
            }
        }

        //构造FileWriter链表，输出到m个分组中
        ArrayList<FileWriter> fileWriters=new ArrayList<FileWriter>();
        try {
            for (int i = 0; i < max + 1; i++) {
                File file = new File("src/main/resources/groupOutPut" + i + ".txt");
                FileWriter fileWriter = new FileWriter(file);
                fileWriters.add(fileWriter);
            }

            for (int i = 0; i < tidsCount; i++) {
                Integer thisGroup = (Integer) group.get(i);
                for (Integer in : arrs.get(i)) {
                    fileWriters.get(thisGroup).write(in + " ");
                }
                fileWriters.get(thisGroup).write("\r\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            for (int i=0;i<max+1;i++){
                fileWriters.get(i).close();
            }
        }
    }


    public void count(String[] itemsString) {
        for (String itemString : itemsString) {
            if ("".equals(itemString)) {
                continue;
            }
            int item = Integer.parseInt(itemString);

            if (item >= maxItem) {
                maxItem = item;
            }
        }
        tidsCount++;
    }

}
