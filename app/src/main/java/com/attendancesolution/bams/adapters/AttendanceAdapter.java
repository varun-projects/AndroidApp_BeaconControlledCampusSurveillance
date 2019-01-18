package com.attendancesolution.bams.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.attendancesolution.bams.R;
import com.attendancesolution.bams.singletonlClasses.Colors2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Akshay on 24-Apr-16.
 */
public class AttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    LayoutInflater inflater;
    Context context;

    Cursor c;

    public AttendanceAdapter(Context context, Cursor c) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.c = c;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 1) {
            View view = inflater.inflate(R.layout.item_attendance, parent, false);
            AttendanceViewHolder holder = new AttendanceViewHolder(view);

            return holder;
        } else {

            View view = inflater.inflate(R.layout.item_attendance_today, parent, false);
            AttendanceHeaderViewHolder holder = new AttendanceHeaderViewHolder(view);

            return holder;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderItem, int position) {

        if (holderItem instanceof AttendanceViewHolder) {
            AttendanceViewHolder holder = (AttendanceViewHolder) holderItem;
            if (!c.moveToPosition(position)) {
                throw new IllegalStateException("couldn't move cursor to position " + position);
            } else {
                c.moveToFirst();
                c.moveToPosition(position);

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


        } else {
            AttendanceHeaderViewHolder holder = (AttendanceHeaderViewHolder) holderItem;
            if (!c.moveToPosition(position)) {
                throw new IllegalStateException("couldn't move cursor to position " + position);
            } else {
                c.moveToFirst();
                c.moveToPosition(position);


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
        return c.getCount();

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        }
        return 0;
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

        TextView date, day, totalClasses, month, attended, percentage, slot1, slot2, slot3;
        LinearLayout dateLayout;
        RelativeLayout itemBack;

        public AttendanceHeaderViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.tv_item_attendance_date);
            day = (TextView) itemView.findViewById(R.id.tv_item_attendance_day);
            month = (TextView) itemView.findViewById(R.id.tv_item_attendance_month);
            attended = (TextView) itemView.findViewById(R.id.tv_item_attendance_attended);
            totalClasses = (TextView) itemView.findViewById(R.id.tv_item_attendance_total);
            percentage = (TextView) itemView.findViewById(R.id.tv_item_attendance_percentage);

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
