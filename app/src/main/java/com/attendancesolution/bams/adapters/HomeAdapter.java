package com.attendancesolution.bams.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.attendancesolution.bams.R;
import com.attendancesolution.bams.singletonlClasses.Colors2;
import com.attendancesolution.bams.singletonlClasses.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Akshay on 10-May-16.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    DBAttendanceAdapter dbAttendanceAdapter;
    LayoutInflater inflater;
    Context context;
    private static int TYPE_HEADER = 0;
    private static int TYPE_TODAY = 1;
    private static int TYPE_ITEM = 2;
    public static String ACTION_ATTENDANCE_MARKED_RECEIVER = "attendanceMarkedReceiver";

    Cursor c;

    public HomeAdapter(Context context, Cursor c) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.c = c;
        dbAttendanceAdapter = new DBAttendanceAdapter(context);

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_HEADER) {
            view = inflater.inflate(R.layout.item_header_main, parent, false);
            AttendanceHeaderViewHolder holder1 = new AttendanceHeaderViewHolder(view);
            return holder1;
        } else if (viewType == TYPE_TODAY) {
            view = inflater.inflate(R.layout.item_attendance_today, parent, false);
            AttendanceTodayViewHolder holder2 = new AttendanceTodayViewHolder(view);
            return holder2;
        } else {
            view = inflater.inflate(R.layout.item_attendance, parent, false);
            AttendanceViewHolder holder3 = new AttendanceViewHolder(view);
            return holder3;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderItem, int position) {

        if (holderItem instanceof AttendanceViewHolder) {
            AttendanceViewHolder holder = (AttendanceViewHolder) holderItem;
            if (!c.moveToPosition(position - 1)) {
                throw new IllegalStateException("couldn't move cursor to position " + (position - 1));
            } else {
                c.moveToFirst();
                c.moveToPosition(position - 1);

               /* String[] dateMonth = getDateCurrentTimeZone(System.currentTimeMillis());

                Log.e("AttendaceAdapter", "date : " + dateMonth[0] + " month : " + dateMonth[1]);*/

                String day = c.getString(DBAttendanceAdapter.COL_DAY) + "";

                holder.dateLayout.setBackgroundResource(R.drawable.bg_icon_attendance);
                GradientDrawable drawable = (GradientDrawable) holder.dateLayout.getBackground();
                drawable.setColor(Color.parseColor(new Colors2().getColorFromDay(day)));

                holder.date.setText(c.getString(DBAttendanceAdapter.COL_DATE) + "");
                holder.day.setText(c.getString(DBAttendanceAdapter.COL_DAY) + "");
                holder.month.setText(c.getString(DBAttendanceAdapter.COL_MONTH) + "");
                holder.totalClasses.setText(c.getInt(DBAttendanceAdapter.COL_TOTAL_CLASSES) + " classes total");
                holder.attended.setText(c.getInt(DBAttendanceAdapter.COL_ATTENDED_CLASSES) + " classes attended");
                holder.percentage.setText(c.getInt(DBAttendanceAdapter.COL_PERCENTAGE) + "%");

                if (c.getInt(DBAttendanceAdapter.COL_PERCENTAGE) > 70) {
                    holder.percentage.setTextColor(Color.parseColor("#9ce447"));
                } else if (c.getInt(DBAttendanceAdapter.COL_PERCENTAGE) > 50) {
                    holder.percentage.setTextColor(Color.parseColor("#47bde4"));
                } else if (c.getInt(DBAttendanceAdapter.COL_PERCENTAGE) > 30) {
                    holder.percentage.setTextColor(Color.parseColor("#e4b247"));
                } else {
                    holder.percentage.setTextColor(Color.parseColor("#e44747"));
                }

            }


        } else if (holderItem instanceof AttendanceTodayViewHolder) {
            AttendanceTodayViewHolder holder = (AttendanceTodayViewHolder) holderItem;
            if (!c.moveToPosition(position - 1)) {
                throw new IllegalStateException("couldn't move cursor to position " + (position - 1));
            } else {
                c.moveToFirst();
                c.moveToPosition(position - 1);

               /* String[] dateMonth = getDateCurrentTimeZone(System.currentTimeMillis());

                Log.e("AttendaceAdapter", "date : " + dateMonth[0] + " month : " + dateMonth[1]);*/

                String day = c.getString(DBAttendanceAdapter.COL_DAY) + "";

                holder.dateLayout.setBackgroundResource(R.drawable.bg_icon_attendance);
                GradientDrawable drawable = (GradientDrawable) holder.dateLayout.getBackground();
                drawable.setColor(Color.parseColor(new Colors2().getColorFromDay(day)));

                holder.date.setText(c.getString(DBAttendanceAdapter.COL_DATE) + "");
                holder.day.setText(c.getString(DBAttendanceAdapter.COL_DAY) + "");
                holder.month.setText(c.getString(DBAttendanceAdapter.COL_MONTH) + "");
                holder.totalClasses.setText(c.getInt(DBAttendanceAdapter.COL_TOTAL_CLASSES) + " classes total");
                holder.attended.setText(c.getInt(DBAttendanceAdapter.COL_ATTENDED_CLASSES) + " classes attended");
                holder.percentage.setText(c.getInt(DBAttendanceAdapter.COL_PERCENTAGE) + "%");

                if (c.getInt(DBAttendanceAdapter.COL_SLOT1) == 1)
                    holder.iv_1.setImageResource(R.drawable.tick_icon);
                else holder.iv_1.setImageResource(R.drawable.cross_icon);

                if (c.getInt(DBAttendanceAdapter.COL_SLOT2) == 1)
                    holder.iv_2.setImageResource(R.drawable.tick_icon);
                else holder.iv_2.setImageResource(R.drawable.cross_icon);

                if (c.getInt(DBAttendanceAdapter.COL_SLOT3) == 1)
                    holder.iv_3.setImageResource(R.drawable.tick_icon);
                else holder.iv_3.setImageResource(R.drawable.cross_icon);

                if (c.getInt(DBAttendanceAdapter.COL_PERCENTAGE) > 70) {
                    holder.percentage.setTextColor(Color.parseColor("#9ce447"));
                } else if (c.getInt(DBAttendanceAdapter.COL_PERCENTAGE) > 50) {
                    holder.percentage.setTextColor(Color.parseColor("#47bde4"));
                } else if (c.getInt(DBAttendanceAdapter.COL_PERCENTAGE) > 30) {
                    holder.percentage.setTextColor(Color.parseColor("#e4b247"));
                } else {
                    holder.percentage.setTextColor(Color.parseColor("#e44747"));
                }


            }
        } else if (holderItem instanceof AttendanceHeaderViewHolder) {
            AttendanceHeaderViewHolder holder = (AttendanceHeaderViewHolder) holderItem;


            holder.date.setText(Utilities.getOnlyDateInString() + "");
            holder.day.setText(Utilities.getDayInString() + "");
            holder.month.setText(Utilities.getOnlyMonthInString() + ", ");


        }
    }

    public static String[] getDateCurrentTimeZone(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMM");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date currenTimeZone = calendar.getTime();
            return new String[]{sdf.format(currenTimeZone), sdf2.format(currenTimeZone)};

        } catch (Exception e) {
        }
        return new String[]{};
    }


    @Override
    public int getItemCount() {
        return c.getCount() + 1;

    }

    @Override
    public int getItemViewType(int position) {
        if (position > 1)
            return TYPE_ITEM;
        else if (position == 1)
            return TYPE_TODAY;
        else
            return TYPE_HEADER;
    }

    private boolean isPositionHeader(int position) {
        if (position > 1)
            return false;
        else
            return true;
    }

    class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView date, day, totalClasses, month, attended, percentage;
        LinearLayout dateLayout;
        RelativeLayout itemBack;


        public AttendanceViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.tv_item_attendance_date);
            day = (TextView) itemView.findViewById(R.id.tv_item_attendance_day);
            month = (TextView) itemView.findViewById(R.id.tv_item_attendance_month);
            attended = (TextView) itemView.findViewById(R.id.tv_item_attendance_attended);
            totalClasses = (TextView) itemView.findViewById(R.id.tv_item_attendance_total);
            percentage = (TextView) itemView.findViewById(R.id.tv_item_attendance_percentage);

            dateLayout = (LinearLayout) itemView.findViewById(R.id.ll_item_attendance_dateLayout);
            itemBack = (RelativeLayout) itemView.findViewById(R.id.layout_back);

            itemBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }
    }

    class AttendanceHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView date, day, month;

        public AttendanceHeaderViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.tv_today_date);
            day = (TextView) itemView.findViewById(R.id.tv_today_day);
            month = (TextView) itemView.findViewById(R.id.tv_today_month);

        }
    }

    class AttendanceTodayViewHolder extends RecyclerView.ViewHolder {
        TextView date, day, totalClasses, month, attended, percentage, slot1, slot2, slot3;
        LinearLayout dateLayout;
        ImageView iv_1, iv_2, iv_3;
        RelativeLayout itemBack;

        public AttendanceTodayViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.tv_item_attendance_date);
            day = (TextView) itemView.findViewById(R.id.tv_item_attendance_day);
            month = (TextView) itemView.findViewById(R.id.tv_item_attendance_month);
            attended = (TextView) itemView.findViewById(R.id.tv_item_attendance_attended);
            totalClasses = (TextView) itemView.findViewById(R.id.tv_item_attendance_total);
            percentage = (TextView) itemView.findViewById(R.id.tv_item_attendance_percentage);

            iv_1 = (ImageView) itemView.findViewById(R.id.iv_1);
            iv_2 = (ImageView) itemView.findViewById(R.id.iv_2);
            iv_3 = (ImageView) itemView.findViewById(R.id.iv_3);

            dateLayout = (LinearLayout) itemView.findViewById(R.id.ll_item_attendance_dateLayout);
            itemBack = (RelativeLayout) itemView.findViewById(R.id.layout_back);
            slot1 = (TextView) itemView.findViewById(R.id.tv_slot1);
            slot2 = (TextView) itemView.findViewById(R.id.tv_slot2);
            slot3 = (TextView) itemView.findViewById(R.id.tv_slot3);
            itemBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


}
