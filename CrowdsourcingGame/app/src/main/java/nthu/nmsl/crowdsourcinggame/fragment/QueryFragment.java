package nthu.nmsl.crowdsourcinggame.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

import nthu.nmsl.crowdsourcinggame.R;
import nthu.nmsl.crowdsourcinggame.map.QueryMap;
import nthu.nmsl.crowdsourcinggame.objects.Query;
import nthu.nmsl.crowdsourcinggame.tools.CommunicateSocket;
import nthu.nmsl.crowdsourcinggame.tools.LocationSensor;

public class QueryFragment extends Fragment {
	private static final String TAG = "tab.QueryFragment";

	// sensing contexts
	private static final String[] CONTEXTS = { "Image", "Video", "Noise", "Air Quality" };

	// UI Widgets
	private MapFragment queryMap;		// google map
	private QueryDraw queryDraw;	// rectangle drawing on the map
	private EditText queriedContextEdit;
	private EditText startDateEdit, startTimeEdit;
	private EditText endDateEdit, endTimeEdit;
	private Button btnQueryList, btnSubmit;
	private ProgressDialog dialog = null;
	private Calendar today;
	private String startYear, startMonth, startDay, startHour, startMinute;
	private String endYear, endMonth, endDay, endHour, endMinute;
	private int recWidth = 480, recHeight = 480;
	private Handler handler;
    private SupportMapFragment beyondarFragment;
    private View view;
	public QueryFragment() {
		today = Calendar.getInstance();
		startYear 	= endYear 	= dateFix(today.get(Calendar.YEAR));
		startMonth 	= endMonth 	= dateFix(today.get(Calendar.MONTH) + 1);
		startDay 	= endDay 	= dateFix(today.get(Calendar.DAY_OF_MONTH));
		startHour 	= endHour 	= dateFix(today.get(Calendar.HOUR_OF_DAY));
		startMinute = endMinute = dateFix(today.get(Calendar.MINUTE));
		this.handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what){
				case 0:
					dialog.dismiss();
					showToast((String) msg.obj);
					break;
				case 1:		// submit button pressed
					submit();
					showDialog();
					break;
				default:
					break;
				}
			}
		};

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// setup the UI components
        if (view==null) {
            view = inflater.inflate(R.layout.query_page, null);
        }
		queriedContextEdit = (EditText) view.findViewById(R.id.queried_context);
		startDateEdit = (EditText) view.findViewById(R.id.start_date_edit);
		startTimeEdit = (EditText) view.findViewById(R.id.start_time_edit);
		endDateEdit = (EditText) view.findViewById(R.id.end_date_edit);
		endTimeEdit = (EditText) view.findViewById(R.id.end_time_edit);
		btnQueryList = (Button) view.findViewById(R.id.list_button);
		btnSubmit = (Button) view.findViewById(R.id.submit_query_button);
		// setup map view
		//setupMap();
		setupOverlayView(view);
		updateDateTimeContents();
		// register all the listeners
		queriedContextEdit.setText(CONTEXTS[0]);
		queriedContextEdit.setOnClickListener(queryEditListener);
		queriedContextEdit.setFocusable(false);
		startDateEdit.setOnClickListener(pickStartDateListener);
		startDateEdit.setFocusable(false);
		startTimeEdit.setOnClickListener(pickStartTimeListener);
		startTimeEdit.setFocusable(false);
		endDateEdit.setOnClickListener(pickEndDateListener);
		endDateEdit.setFocusable(false);
		endTimeEdit.setOnClickListener(pickEndTimeListener);
		endTimeEdit.setFocusable(false);
		btnQueryList.setOnClickListener(queriedContextListener);
		btnSubmit.setOnClickListener(submitListener);
		return view;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //beyondarFragment = (SupportMapFragment) ((FragmentActivity)getActivity()).getSupportFragmentManager().findFragmentById(R.id.beyondarFragment);
    }

    public void setupOverlayView(View view) {
		RelativeLayout relative = (RelativeLayout) view
				.findViewById(R.id.relative_draw_map);
		queryDraw = new QueryDraw(getActivity());
		queryDraw.invalidate();

		queryDraw.setAlpha((float) 0.2);
		relative.addView(queryDraw);

		final ToggleButton t = new ToggleButton(getActivity());
		t.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton button,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
                    beyondarFragment.getMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);
					t.setBackgroundColor(0xBBFFFFFF);
				} else {
                    beyondarFragment.getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
					t.setBackgroundColor(0xBBBBBBBB);
				}
			}
		});

		//p.setMargins(10, 10, 10, 10);

		//t.setLayoutParams(p);
		t.setBackgroundColor(0xBBBBBBBB);
		// t.setAlpha((float) 1);

		relative.addView(t);
	}

	public void updateDateTimeContents() {
		startDateEdit.setText(startYear + "/" + startMonth + "/" + startDay);
		startTimeEdit.setText(startHour + ":" + startMinute);
		endDateEdit.setText(endYear + "/" + endMonth + "/" + endDay);
		endTimeEdit.setText(endHour + ":" + endMinute);
	}

	public OnClickListener submitListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			new Thread() {
				public void run() {
					handler.sendMessage(setupMessage(1, null));
				}
			}.start();

		}

	};

	public OnClickListener queryEditListener = new OnClickListener() {
		public void onClick(View v) {
			showQueryInputDialog();
		}
	};

	public OnClickListener queriedContextListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setItems(CONTEXTS, contextListListener);
			builder.create().show();
		}
	};
	public DialogInterface.OnClickListener contextListListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			queriedContextEdit.setText(CONTEXTS[which]);
			// selectedContext = contexts[which];
		}
	};

	public OnClickListener pickStartDateListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			DatePickerDialog dialog = new DatePickerDialog(getActivity(),
					startDateSetListener, today.get(Calendar.YEAR),
					today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
			dialog.show();
		}
	};
	public DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			startYear = dateFix(year);
			startMonth = dateFix(monthOfYear + 1);
			startDay = dateFix(dayOfMonth);
			updateDateTimeContents();
		}
	};

	public OnClickListener pickStartTimeListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			TimePickerDialog dialog = new TimePickerDialog(getActivity(),
					startTimeSetListener, today.get(Calendar.HOUR_OF_DAY),
					today.get(Calendar.MINUTE), true);
			dialog.show();
		}

	};
	public TimePickerDialog.OnTimeSetListener startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			startHour = dateFix(hourOfDay);
			startMinute = dateFix(minute);
			updateDateTimeContents();
		}
	};

	public OnClickListener pickEndDateListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			DatePickerDialog dialog = new DatePickerDialog(getActivity(),
					endDateSetListener, today.get(Calendar.YEAR),
					today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
			dialog.show();
		}
	};
	public DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			endYear = dateFix(year);
			endMonth = dateFix(monthOfYear + 1);
			endDay = dateFix(dayOfMonth);
			updateDateTimeContents();
		}
	};

	public OnClickListener pickEndTimeListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			TimePickerDialog dialog = new TimePickerDialog(getActivity(),
					endTimeSetListener, today.get(Calendar.HOUR_OF_DAY),
					today.get(Calendar.MINUTE), true);
			dialog.show();
		}
	};
	public TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			endHour = dateFix(hourOfDay);
			endMinute = dateFix(minute);
			updateDateTimeContents();
		}
	};

	
	public void submit() {
		/*Log.d(TAG, "left top: (" + fm.getLeft() + "," + fm.getTop()
				+ "),  right bottm: (" + fm.getRight() + "," + fm.getBottom()
				+ ")");*/
		//queryMap.addRectangleCornerMarkers(fm.getRight(), fm.getBottom(), recWidth, recHeight);

		new Thread() {
			public void run() {

				/*LatLng[] corners = queryMap.getCorners();
				Query query = setupQuery(corners);

				NetworkInstance.communicate.submitQuery(query,
						QueryFragment.this);*/
			}
		}.start();

	}

	public Query setupQuery(LatLng[] corners) {
		Query q = setQueryCorners(corners[0], corners[1], corners[2],
				corners[3]);
		q.setTitle(queriedContextEdit.getText().toString());
		q.setStartDate(startDateEdit.getText().toString());
		q.setStartTime(startTimeEdit.getText().toString());
		q.setEndDate(endDateEdit.getText().toString());
		q.setEndTime(endTimeEdit.getText().toString());
        Location l = LocationSensor.getInstance().getCurrentLocation();
        double[] d = new double[2];

		if (l != null) {
			d[0] = l.getLatitude();
			d[1] = l.getLongitude();
		}

		q.setCurrentLocation(d);

		return q;
	}

	public Query setQueryCorners(LatLng lt, LatLng lb, LatLng rt, LatLng rb) {
		Query q = new Query();

		q.setLeftTop(lt.latitude, lt.longitude);
		q.setLeftBottom(lb.latitude, lb.longitude);
		q.setRightTop(rt.latitude, rt.longitude);
		q.setRightBottom(rb.latitude, rb.longitude);

		return q;
	}

	
	private static String dateFix(int c) {
		if (c >= 10) {
			return String.valueOf(c);
		} else {
			return "0" + String.valueOf(c);
		}
	}

	public Message setupMessage(int what, Object obj) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;

		return msg;
	}

	private void showDialog() {
		dialog = new ProgressDialog(getActivity());
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("please wait...");
		dialog.setCancelable(false);// cannot be canceled
		dialog.show();
	}

	public void showQueryInputDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		final View view = inflater.inflate(R.layout.query_dialog, null);
		EditText e = (EditText) view.findViewById(R.id.edit_content);
		e.setText(queriedContextEdit.getText().toString());
		builder.setView(view);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				EditText ed = (EditText) view.findViewById(R.id.edit_content);
				queriedContextEdit.setText(ed.getText().toString());
			}
		});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});

		builder.create().show();
	}

	public void showToast(String str) {
		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
	}

}
