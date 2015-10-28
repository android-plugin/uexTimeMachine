package org.zywx.wbpalmstar.plugin.uextimemachine;

import java.util.HashMap;
import java.util.HashSet;

import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.widgetone.dataservice.WWidgetData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class EUExTimeMachine extends EUExBase {

	public static final String CALLBACK_LOAD_DATA = "uexTimeMachine.loadData";
	public static final String cbOpenFunName = "uexTimeMachine.cbOpen";
	
	public static final String ON_ITEM_SELECTED = "uexTimeMachine.onItemSelected";
	public static final String TAG = "EUExTimeMachine";
	private static String currentTag = null;
	private HashMap<String, TimeMachineView> views = new HashMap<String, TimeMachineView>();
	private WWidgetData mWWidgetData = null;
    private static final String BUNDLE_DATA = "data";
    private static final int MSG_OPEN = 1;
    private static final int MSG_SET_JSON_DATA = 2;
    private static final int MSG_CLOSE = 3;
	public EUExTimeMachine(Context context, EBrowserView inParent) {
		super(context, inParent);
		mWWidgetData = inParent.getCurrentWidget();
	}

    public void open(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_OPEN;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void openMsg(String[] params) {
        if (params.length != 5) {
            return;
        }
        try {
            final String tmId = params[0];
            final int x = Integer.parseInt(params[1]);
            final int y = Integer.parseInt(params[2]);
            final int w = Integer.parseInt(params[3]);
            final int h = Integer.parseInt(params[4]);
            currentTag = tmId;
            TimeMachineView timeMachineView = new TimeMachineView(mContext);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);
            lp.leftMargin = x;
            lp.topMargin = y;
            addViewToCurrentWindow(timeMachineView, lp);
            views.put(tmId, timeMachineView);
            String js = SCRIPT_HEADER + "if(" + CALLBACK_LOAD_DATA + "){" + CALLBACK_LOAD_DATA + "('" + tmId
                    + "');}";
            onCallback(js);
            js = SCRIPT_HEADER + "if(" + cbOpenFunName + "){" + cbOpenFunName + "('" + tmId
                    + "');}";
            onCallback(js);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void setJsonData(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SET_JSON_DATA;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void setJsonDataMsg(String[] params) {
        if (params.length < 1) {
            return;
        }
        final TimeMachine timeMachine = TimeMachine.parseTimeMachineJson(params[0]);
        if (timeMachine == null) {
            return;
        }
        if (!views.containsKey(timeMachine.getTmId()) || views.get(timeMachine.getTmId()) == null)
            return;
        TimeMachineView view = views.get(timeMachine.getTmId());
        if (view != null) {
            view.setData(mWWidgetData, timeMachine, new CarouselAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(CarouselAdapter<?> parent, View view, int position, long id) {
                    String js = SCRIPT_HEADER + "if(" + ON_ITEM_SELECTED + "){" + ON_ITEM_SELECTED + "('"
                            + timeMachine.getTmId() + "'," + position + ");}";
                    onCallback(js);
                }
            });

        }
    }

    public void close(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CLOSE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void closeMsg(String[] params) {
        if (params.length < 1) {
            return;
        }
        final String tmId = params[0];
        if (tmId == null || tmId.length() == 0) {
            return;
        }
        if (currentTag == null) {
            return;
        }
        String tmIdStr = tmId.substring(0, tmId.length());
        String[] tmIds = tmIdStr.split(",");
        if(tmIds != null){
            for(int i =0,length = tmIds.length; i<length;i++){
                closeTimeMachine(tmIds[i]);
            }
            currentTag = null;
        }
    }

    @Override
    public void onHandleMessage(Message message) {
        if(message == null){
            return;
        }
        Bundle bundle=message.getData();
        switch (message.what) {

            case MSG_OPEN:
                openMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_SET_JSON_DATA:
                setJsonDataMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_CLOSE:
                closeMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            default:
                super.onHandleMessage(message);
        }
    }

	@Override
	protected boolean clean() {
		return true;
	}
	
	
	 private void closeTimeMachine(final String tmId){
         if (views.containsKey(tmId) && views.get(tmId) != null){
             removeViewFromCurrentWindow(views.get(tmId));
             views.remove(tmId);
         }
     }
}
