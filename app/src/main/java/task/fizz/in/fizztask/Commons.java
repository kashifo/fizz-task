package task.fizz.in.fizztask;

import java.util.List;


//I keep common code here
public class Commons {

    public static boolean notEmpty(String str){
        return str != null && !str.isEmpty() ? true : false;
    }

    public static boolean notEmpty(List<?> list){
        return list != null && !list.isEmpty() ? true : false;
    }

}
