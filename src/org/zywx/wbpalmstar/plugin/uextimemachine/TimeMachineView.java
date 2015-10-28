package org.zywx.wbpalmstar.plugin.uextimemachine;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.widgetone.dataservice.WWidgetData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class TimeMachineView extends FrameLayout {

	private PageIndicator pageIndicator;
	private Carousel carousel;
    private Context mContext;

    public TimeMachineView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

	private void initView() {
        LayoutInflater.from(mContext).inflate(EUExUtil
                .getResLayoutID("plugin_timemachine_main_layout"),this,true);
		carousel = (Carousel) findViewById(EUExUtil
				.getResIdID("plugin_timemachine_carousel"));
		pageIndicator = (PageIndicator) findViewById(EUExUtil
				.getResIdID("plugin_timemachine_page_indictor"));
	}

	public void setData(WWidgetData mWWidgetData, TimeMachine timeMachine,
			CarouselAdapter.OnItemClickListener listener) {
		CarouselItemAdapter adapter = new CarouselItemAdapter(mContext,
				timeMachine.getList(), mWWidgetData);
		carousel.setAdapter(adapter);
		pageIndicator.setTotalPageSize(adapter.getCount());
		pageIndicator.setCurrentPage(carousel.getSelectedItemPosition());
		carousel.setOnItemClickListener(listener);
		carousel.setOnItemSelectedListener(new CarouselAdapter.OnItemSelectedListener() {

			@Override
			public void onItemSelected(CarouselAdapter<?> parent, View view,
					int position, long id) {
				pageIndicator.setCurrentPage(position);
			}

			@Override
			public void onNothingSelected(CarouselAdapter<?> parent) {

			}
		});
	}

}
