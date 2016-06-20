package nthu.nmsl.crowdsourcinggame.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.hardware.SensorListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import com.google.android.gms.maps.model.LatLng;
import android.widget.AbsListView.OnScrollListener;
import java.util.ArrayList;
import java.util.HashMap;

import nthu.nmsl.crowdsourcinggame.R;
import nthu.nmsl.crowdsourcinggame.activities.BackgroundManager;
import nthu.nmsl.crowdsourcinggame.instances.CreatureList;
import nthu.nmsl.crowdsourcinggame.objects.TaskData;
import nthu.nmsl.crowdsourcinggame.objects.TaskDataManager;
import nthu.nmsl.crowdsourcinggame.tools.CommunicateSocket;


public class TaskFragment extends Fragment {
	private static final String TAG = "Crowdsourcing-client";
	private SwipeRefreshLayout laySwipe;
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private ListView lstData;
    private boolean isGetTaskFinish = false;
	public TaskFragment() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.task_page, null);
		laySwipe = (SwipeRefreshLayout) view.findViewById(R.id.laySwipe);
		laySwipe.setOnRefreshListener(onSwipeToRefresh);
		laySwipe.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        lstData = (ListView) view.findViewById(R.id.lstData);
		lstData.setAdapter(getAdapter());
		lstData.setOnScrollListener(onListScroll);
        lstData.setOnItemClickListener(onClickList);

        fragmentManager = getFragmentManager();
		return view;
	}

    private AdapterView.OnItemClickListener onClickList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub
            ListView listView = (ListView) arg0;
            HashMap<String,Object> item = (HashMap<String,Object>)listView.getItemAtPosition(arg2);

            Toast.makeText(getActivity(), "ID：" + arg3 +
                    "   選單文字：" + item.get("taskName"),
                    Toast.LENGTH_LONG).show();
            //((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, new Fragment());
            transaction.commit();
        }
    };
	private OnRefreshListener onSwipeToRefresh = new OnRefreshListener() {
		@Override
		public void onRefresh() {
			laySwipe.setRefreshing(true);
            isGetTaskFinish = false;
            getTaskDataThread.start();
		}
	};

	private Thread getTaskDataThread = new Thread() {
		public void run() { // override Thread's run()
            while (true) {
                if (BackgroundManager.getInstance(getActivity()).updateTask()) {
                    isGetTaskFinish = true;
                    mHandler.sendEmptyMessage(1);
                    break;
                }
                try {
                    this.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e("thread","thread");
                    e.printStackTrace();
                }
            }
		}
	};

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    lstData.setAdapter(getAdapter());
                    laySwipe.setRefreshing(false);
                    break;
            }
        }
    };

	private SimpleAdapter getAdapter() {
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        TaskDataManager taskDataManager = TaskDataManager.getInstance();
        for(int i=0; i < taskDataManager.getSize(); i++){
            HashMap<String,Object> item = new HashMap<String,Object>();
            item.put( "picture", CreatureList.pictures[taskDataManager.get(i).getCreatureID()]);
            item.put( "taskName", taskDataManager.get(i).getTaskName());
            item.put( "distance", "");
            item.put( "hiddenLatitude", taskDataManager.get(i).getLatitude());
            item.put( "hiddenLongitude", taskDataManager.get(i).getLongitude());
            list.add(item);
        }
        SimpleAdapter  adapter = new SimpleAdapter(getActivity(), list, R.layout.task_list_layout,
                new String[] { "picture", "taskName", "distance"},
                new int[] { R.id.task_list_picture, R.id.task_list_name, R.id.task_list_distance } );
		return adapter;
	}

	private OnScrollListener onListScroll = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
							 int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem == 0) {
				laySwipe.setEnabled(true);
			}else{
				laySwipe.setEnabled(false);
			}
		}
	};
}
