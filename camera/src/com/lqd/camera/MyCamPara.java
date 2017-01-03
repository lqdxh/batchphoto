package com.lqd.camera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;

public class MyCamPara {
	private static final String tag = "lqd";
	private CameraSizeComparator sizeComparator = new CameraSizeComparator();
	private static MyCamPara myCamPara = null;

	private MyCamPara() {

	}

	public static MyCamPara getInstance() {
		if (myCamPara == null) {
			myCamPara = new MyCamPara();
			return myCamPara;
		} else {
			return myCamPara;
		}
	}

	public Size getPreviewSize(List<Camera.Size> list, int th) {
//		float rate = (float) ((MainActivity) MainActivity.mainActivity)
//				.getScreenPoint().y
//				/ ((MainActivity) MainActivity.mainActivity).getScreenPoint().x;
		float rate = 1.77f;
		// 取出与屏幕比例类似的分辨率
		List<Size> sizes = new ArrayList<Size>();
		for (Size s : list) {
			if (equalRate(s, rate)) {
				sizes.add(s);
			}
		}
		if (sizes.size() > 0) {
			int i = 0;
			Collections.sort(sizes, sizeComparator);
			for (Size s : sizes) {
				if (s.width <= th) {
					break;
				}
				i++;
			}
			if (sizes.size() == i) {
				return sizes.get(i - 1);
			} else {
				return sizes.get(i);
			}
		} else {

			// 如果没有类似分辨率的则随机了
			Collections.sort(list, sizeComparator);
			int i = 0;
			for (Size s : list) {
				if ((s.width <= th) && equalRate(s, rate)) {
					break;
				}
				i++;
			}
			if (list.size() == i) {
				return list.get(i - 1);
			} else {
				return list.get(i);
			}
		}
	}

	public Size getPictureSize(List<Camera.Size> list, int th) {

//		float rate = (float) ((MainActivity) MainActivity.mainActivity)
//				.getScreenPoint().y
//				/ ((MainActivity) MainActivity.mainActivity).getScreenPoint().x;
		float rate = 1.77f;
		// 取出与屏幕比例类似的分辨率
		List<Size> sizes = new ArrayList<Size>();
		for (Size s : list) {
			if (equalRate(s, rate)) {
				sizes.add(s);
			}
		}
		if (sizes.size() > 0) {
			int i = 0;
			Collections.sort(sizes, sizeComparator);
			for (Size s : sizes) {
				// 未经旋转的width即高度
				if (s.width <= th) {
					break;
				}
				i++;
			}
			Size selSize = null;
			if (sizes.size() == i) {
				selSize = sizes.get(i - 1);
				float b = (float) (Math
						.floor((selSize.width / (float) selSize.height) * 10)) / 10;
				((MainActivity) MainActivity.mainActivity).setMaxWHRate(b);
				return selSize;
			} else {
				selSize = sizes.get(i);
				float b = (float) (Math
						.floor((selSize.width / (float) selSize.height) * 10)) / 10;
				((MainActivity) MainActivity.mainActivity).setMaxWHRate(b);
				return selSize;
			}
		}else{

			// 如果没有类似分辨率的则随机了
			Collections.sort(list, sizeComparator);
			int i = 0;
			for (Size s : list) {
				if ((s.width <= th) && equalRate(s, rate)) {
					break;
				}
				i++;
			}
			Size selSize = null;
			if (list.size() == i) {
				selSize = list.get(i - 1);
				float b = (float) (Math
						.floor((selSize.height / (float) selSize.width) * 10)) / 10;
				((MainActivity) MainActivity.mainActivity).setMaxWHRate(b);
				return selSize;
			} else {
				selSize = list.get(i);
				float b = (float) (Math
						.floor((selSize.height / (float) selSize.width) * 10)) / 10;
				((MainActivity) MainActivity.mainActivity).setMaxWHRate(b);
				return selSize;
			}
		}
	}

	public Size getMaxPictureSize(List<Camera.Size> list) {
		Collections.sort(list, sizeComparator);

		int i = 0;
		for (Size s : list) {
			if (equalRate(
					s,
					(float) ((MainActivity) MainActivity.mainActivity)
							.getScreenPoint().y
							/ ((MainActivity) MainActivity.mainActivity)
									.getScreenPoint().x)) {
				break;
			}
			i++;
		}

		return list.get(i);
	}

	public boolean equalRate(Size s, float rate) {
		float r = (float) (s.width) / (float) (s.height);
		if (Math.abs(r - rate) <= 0.05) {
			return true;
		} else {
			return false;
		}
	}

	public class CameraSizeComparator implements Comparator<Camera.Size> {

		// 按降序排列
		public int compare(Size lhs, Size rhs) {
			// TODO Auto-generated method stub
			if (lhs.width == rhs.width) {
				return 0;
			} else if (lhs.width < rhs.width) {
				return 1;
			} else {
				return -1;
			}
		}

	}
}
