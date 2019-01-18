package com.attendancesolution.bams.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.attendancesolution.bams.R;
import com.attendancesolution.bams.singletonlClasses.Colors;

/**
 * Created by Akshay on 24-Apr-16.
 */
public class RecentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Cursor c;
    LayoutInflater inflater;
    Context context;
    Typeface boldTypeface, normalTypeface;
    DBRecentAdapter dbRecentAdapter;

    public RecentAdapter(Context context, Cursor c) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.c = c;
        boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        normalTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);
        dbRecentAdapter = new DBRecentAdapter(context);
        dbRecentAdapter.open();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_recent, parent, false);
            RecentViewHolder holder = new RecentViewHolder(view);
            return holder;
        } else {
            View view = inflater.inflate(R.layout.item_recent_messages, parent, false);
            RecentViewHolder2 holder = new RecentViewHolder2(view);
            return holder;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderItem, int position) {

        if (holderItem instanceof RecentViewHolder) {
            RecentViewHolder holder = (RecentViewHolder) holderItem;
            if (!c.moveToPosition(position)) {
                throw new IllegalStateException("couldn't move cursor to position " + position);
            } else {
                c.moveToFirst();
                c.moveToPosition(position);


                String character = c.getString(DBRecentAdapter.COL_LINK).toUpperCase().charAt(11) + "";

                holder.initial.setBackgroundResource(R.drawable.bg_icon_recent);
                GradientDrawable drawable = (GradientDrawable) holder.initial.getBackground();
                drawable.setColor(Color.parseColor(new Colors().getColorFromChar(character)));

                holder.initial.setText(character);
                holder.link.setText(c.getString(DBRecentAdapter.COL_LINK));
                holder.day.setText(c.getString(DBRecentAdapter.COL_DAY));
                holder.date.setText(c.getString(DBRecentAdapter.COL_DATE));

                if (c.getInt(DBRecentAdapter.COL_READ) == 0) {
                    holder.read.setVisibility(View.VISIBLE);
                    holder.link.setTypeface(boldTypeface);
                } else {
                    holder.read.setVisibility(View.GONE);
                    holder.link.setTypeface(normalTypeface);
                }
            }

        } else {
            RecentViewHolder2 holder = (RecentViewHolder2) holderItem;
            if (!c.moveToPosition(position)) {
                throw new IllegalStateException("couldn't move cursor to position " + position);
            } else {
                c.moveToFirst();
                c.moveToPosition(position);

                String character = c.getString(DBRecentAdapter.COL_LINK).toUpperCase().charAt(0) + "";
                holder.initialBack.setBackgroundColor(Color.parseColor(new Colors().getColorFromChar(character)));

                holder.link.setText(c.getString(DBRecentAdapter.COL_LINK));
                holder.day.setText(c.getString(DBRecentAdapter.COL_DAY));
                holder.date.setText(c.getString(DBRecentAdapter.COL_DATE));

                if (c.getInt(DBRecentAdapter.COL_READ) == 0) {
                    holder.read.setVisibility(View.VISIBLE);
                    holder.link.setTypeface(boldTypeface);
                } else {
                    holder.read.setVisibility(View.GONE);
                    holder.link.setTypeface(normalTypeface);
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return c.getCount();

    }

    @Override
    public int getItemViewType(int position) {
        c.moveToPosition(position);
        int type = c.getInt(DBRecentAdapter.COL_TYPE);
        return type;
    }

    private boolean isPositionHeader(int position) {
        if (position > 1)
            return false;
        else
            return true;
    }


    class RecentViewHolder2 extends RecyclerView.ViewHolder {

        TextView link, date, day;
        RelativeLayout layoutBack, read;
        LinearLayout initialBack;

        public RecentViewHolder2(View itemView) {
            super(itemView);

            link = (TextView) itemView.findViewById(R.id.tv_item_recent_link);
            date = (TextView) itemView.findViewById(R.id.tv_item_recent_date);
            day = (TextView) itemView.findViewById(R.id.tv_item_recent_day);

            layoutBack = (RelativeLayout) itemView.findViewById(R.id.layout_back);
            read = (RelativeLayout) itemView.findViewById(R.id.rl_recent_read);
            initialBack = (LinearLayout) itemView.findViewById(R.id.ll_item_recent_link);

            layoutBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    c.moveToFirst();
                    c.moveToPosition(getAdapterPosition());
                    dbRecentAdapter.updateRow(c.getInt(DBRecentAdapter.COL_ROWID),
                            c.getString(DBRecentAdapter.COL_LINK),
                            c.getString(DBRecentAdapter.COL_DAY),
                            c.getString(DBRecentAdapter.COL_DATE), 1, 1);

                    c = dbRecentAdapter.getAllMessages();
                    notifyItemChanged(getAdapterPosition());

                }
            });

        }
    }

    class RecentViewHolder extends RecyclerView.ViewHolder {

        TextView initial, link, date, day;
        RelativeLayout layoutBack, read;
        LinearLayout initialBack;

        public RecentViewHolder(View itemView) {

            super(itemView);

            initial = (TextView) itemView.findViewById(R.id.tv_item_recent_link_initial);
            link = (TextView) itemView.findViewById(R.id.tv_item_recent_link);
            date = (TextView) itemView.findViewById(R.id.tv_item_recent_date);
            day = (TextView) itemView.findViewById(R.id.tv_item_recent_day);

            layoutBack = (RelativeLayout) itemView.findViewById(R.id.layout_back);
            read = (RelativeLayout) itemView.findViewById(R.id.rl_recent_read);
            initialBack = (LinearLayout) itemView.findViewById(R.id.ll_item_recent_link);

            layoutBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    c.moveToFirst();
                    c.moveToPosition(getAdapterPosition());
                    dbRecentAdapter.updateRow(c.getInt(DBRecentAdapter.COL_ROWID),
                            c.getString(DBRecentAdapter.COL_LINK),
                            c.getString(DBRecentAdapter.COL_DAY),
                            c.getString(DBRecentAdapter.COL_DATE), 1, 0);

                    c = dbRecentAdapter.getAllLinks();

                    notifyItemChanged(getAdapterPosition());

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(c.getString(DBRecentAdapter.COL_LINK)));
                    context.startActivity(i);


                }
            });

        }
    }

}