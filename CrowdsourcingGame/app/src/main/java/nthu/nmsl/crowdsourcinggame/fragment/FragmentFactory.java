package nthu.nmsl.crowdsourcinggame.fragment;

import android.app.Fragment;

import nthu.nmsl.crowdsourcinggame.R;

/*
 * static class for accessing the four fragments for Home Activity
 * QueryFragment
 * MapFragment
 * TaskFragment
 * SettingFragment
 */
public class FragmentFactory {
	public static ARFragmentV2 arFragment = null;
	public static MapFragment mapFragment = null;
	public static TaskFragment taskFragment = null;
	public static SettingFragment settingFragment = null;
    public static Fragment fragment = null;

	public static Fragment getInstanceByIndex(int index) {
		switch (index) {
			case R.id.radioButton1:
				if (arFragment==null) {
                    arFragment = new ARFragmentV2();
				}
				fragment =  arFragment;
				break;
			case R.id.radioButton2:
				if (mapFragment ==null) {
					mapFragment = new MapFragment();
				}
				fragment = mapFragment;
				break;
			case R.id.radioButton3:
				if (taskFragment==null) {
					taskFragment = new TaskFragment();
				}
				fragment = taskFragment;
				break;
			case R.id.radioButton4:
				if (settingFragment==null) {
					settingFragment = new SettingFragment();
				}
				fragment = settingFragment;
				break;
		}
		return fragment;
	}
}
