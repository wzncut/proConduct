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
            cur.endAdding();
        }
        SuperHy[] ch = cur.split();
        if (cur.getChildren()!=null){
            System.out.println("构建成功:"+cur.toString());
        }
        for(SuperHy hp : ch){
            if(hp.size() <= Process.MAX_INSTANCE_NUM_NOT_SPLIT){
                continue;
            }
            buildAnInstance(hp);
        }
        return cur;
    }
}
