package center.claims.mirascon.mirascon.Adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import center.claims.mirascon.mirascon.Activity.Camera;
import center.claims.mirascon.mirascon.Activity.ClaimsCenter;
import center.claims.mirascon.mirascon.Activity.ClaimsForm;
import center.claims.mirascon.mirascon.Activity.Emergency;
import center.claims.mirascon.mirascon.Activity.GlassDamage;
import center.claims.mirascon.mirascon.Activity.GoogleMaps;
import center.claims.mirascon.mirascon.Models.Support;
import center.claims.mirascon.mirascon.R;

public class ClaimsCenterAdapter extends RecyclerView.Adapter<center.claims.mirascon.mirascon.Adapter.ClaimsCenterAdapter.MyViewHolder> {

    private Context mContext;
    private List<Support> mData;


    public ClaimsCenterAdapter(Context mContext, List<Support> mData) {
        this.mContext = mContext; //get Activity of bottomfrag2
        this.mData = mData;
    }

    @Override
    public center.claims.mirascon.mirascon.Adapter.ClaimsCenterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //get the initialize class ids

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);  //get the frag3 layout
        view = mInflater.inflate(R.layout.recyclerview_button, parent, false); //set the CardView Layout in frag3 layout
        return new center.claims.mirascon.mirascon.Adapter.ClaimsCenterAdapter.MyViewHolder(view); //return the cardView
    }

    @Override
    public void onBindViewHolder(center.claims.mirascon.mirascon.Adapter.ClaimsCenterAdapter.MyViewHolder holder, final int position) { //set the Data in all positions of the CardView

        holder.img_title.setText(mData.get(position).getTitle());
        holder.img_thumbnail.setImageResource(mData.get(position).getThumbnail());
        holder.cardView.setOnClickListener(new View.OnClickListener() {  //Click on one CardView to do something
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    if (mContext instanceof ClaimsCenter) {
                        ((ClaimsCenter) mContext).transitionActivity(ClaimsForm.class);
                    }
                } else if (position == 1) {
                    if (mContext instanceof ClaimsCenter) {
                        ((ClaimsCenter) mContext).transitionActivity(Camera.class);
                    }
                } else if (position == 2){
                    if (mContext instanceof ClaimsCenter) {
                        ((ClaimsCenter) mContext).transitionActivity(GoogleMaps.class);
                    }
                } else if (position == 3){
                    if (mContext instanceof ClaimsCenter) {
                        ((ClaimsCenter) mContext).transitionActivity(GlassDamage.class);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder { //This class is to initialize views and say which place to set the cardView

        TextView img_title;
        ImageView img_thumbnail;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            img_title = (TextView) itemView.findViewById(R.id.call_title);
            img_thumbnail = (ImageView) itemView.findViewById(R.id.call_img);
            cardView = itemView.findViewById(R.id.cardview_id);
        }
    }

}
