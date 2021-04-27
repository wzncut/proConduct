import com.alibaba.fastjson.JSON;
import sun.plugin2.message.JavaScriptBaseMessage;

class BallTreeStruct {
    //递归调用建立ball tree
    static SuperHy buildAnInstance(SuperHy cur){
        if(cur == null){
            cur = new SuperHy();
            for(int i = 0; i < Process.INSTANCES.size(); ++i){
                cur.addInstance(i);
            }
            //构造圆
            cur.endAdding();
        }
        //以上代码构建了一个大超球体
        //下面进行分割，将大球内部分割小球
        SuperHy[] ch = cur.split();
        if (cur.getChildren()!=null){
            System.out.println("构建成功:"+cur.toString());
        }
        for(SuperHy hp : ch){
            if(hp.size() <= Process.MAX_INSTANCE_NUM_NOT_SPLIT){
                continue;
            }
            //递归构造
            buildAnInstance(hp);
        }
        return cur;
    }
}
