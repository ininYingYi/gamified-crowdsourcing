package nthu.nmsl.crowdsourcinggame.objects;

import java.util.ArrayList;

/**
 * Created by inin6 on 2015/11/8.
 */
public class TaskDataManager {
    private static TaskDataManager objectSelf = null;
    private ArrayList<TaskData> datas = new ArrayList<TaskData>();

    public static TaskDataManager getInstance() {
        if (objectSelf == null) {
            objectSelf = new TaskDataManager();
        }
        return objectSelf;
    }

    public TaskDataManager() {

    }

    public void insert(TaskData object) {
        if ( !datas.contains(object) ) {
            datas.add(object);
        }
    }

    public TaskData get(int i) {
        return datas.get(i);
    }
    public ArrayList<TaskData> getAll() {
        return datas;
    }
    public int getSize() {return datas.size();}
}
