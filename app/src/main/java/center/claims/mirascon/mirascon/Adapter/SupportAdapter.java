package center.claims.mirascon.mirascon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;
import center.claims.mirascon.mirascon.Activity.ClaimsCenter;
import center.claims.mirascon.mirascon.Activity.Emergency;
import center.claims.mirascon.mirascon.Activity.MainActivity;
import center.claims.mirascon.mirascon.Activity.RoadsideAssistance;
import center.claims.mirascon.mirascon.Models.Support;
import center.claims.mirascon.mirascon.R;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.MyViewHolder> {

    private Context mContext;
    private List<Support> mData;
    private Button activeButton = null;


    public SupportAdapter(Context mContext, List<Support> mData) {
        this.mContext = mContext; //get Activity of bottomfrag2
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //get the initialize class ids

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);  //get the frag3 layout
        view = mInflater.inflate(R.layout.cardview_main, parent, false); //set the CardView Layout in frag3 layout
        return new MyViewHolder(view); //return the cardView
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) { //set the Data in all positions of the CardView

        holder.img_title.setText(mData.get(position).getTitle());
        holder.img_thumbnail.setImageResource(mData.get(position).getThumbnail());
        holder.cardView.setOnClickListener(new View.OnClickListener() {  //Click on one CardView to do something
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).transitionActivity(Emergency.class);
                    }
                } else if (position == 1) {
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).transitionActivity(RoadsideAssistance.class);
                    }
                } else if (position == 2) {
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).transitionActivity(ClaimsCenter.class);
                    }
                } else if (position == 3) {
                    /*webView = findViewById(R.id.webView1);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadUrl("http://www.google.com");*/

                    String url = "https://www.mirascon.com/";

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));

                    mContext.startActivity(intent);
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

            img_title = itemView.findViewById(R.id.img_title);
            img_thumbnail = itemView.findViewById(R.id.img_main);
            cardView = itemView.findViewById(R.id.cardview_id);


        }
    }

}
