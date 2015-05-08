package org.zywx.wbpalmstar.plugin.uextimemachine;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TimeMachine {

	public static final String JK_ID = "id";
	public static final String JK_DATA = "data";
	public static final String JK_TITLE = "title";
	public static final String JK_URL_IMAGE_URL = "imageUrl";

	/**
	 * 解析TimeMachine相关信息
	 * 
	 * @param msg
	 * @return
	 */
	public static TimeMachine parseTimeMachineJson(String msg) {
		if (msg == null || msg.length() == 0) {
			return null;
		}
		TimeMachine timeMachine = null;
		try {
			JSONObject json = new JSONObject(msg);
			timeMachine = new TimeMachine();
			timeMachine.setTmId(json.getString(JK_ID));
			JSONArray array = json.getJSONArray(JK_DATA);
			for (int i = 0, size = array.length(); i < size; i++) {
				JSONObject jsonItem = array.getJSONObject(i);
				ItemInfo itemInfo = new ItemInfo();
				itemInfo.setTitle(jsonItem.optString(JK_TITLE));
				itemInfo.setImgUrl(jsonItem.getString(JK_URL_IMAGE_URL));
				timeMachine.add(itemInfo);
			}
		} catch (JSONException e) {
			timeMachine = null;
			e.printStackTrace();
		}
		return timeMachine;
	}

	private String tmId;
	private List<ItemInfo> list;

	public TimeMachine() {
		list = new ArrayList<TimeMachine.ItemInfo>();
	}

	public String getTmId() {
		return tmId;
	}

	public void setTmId(String tmId) {
		this.tmId = tmId;
	}

	public void add(ItemInfo item) {
		list.add(item);
	}

	public List<ItemInfo> getList() {
		return list;
	}

	public static class ItemInfo {
		private String title;
		private String imgUrl;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

	}

}
