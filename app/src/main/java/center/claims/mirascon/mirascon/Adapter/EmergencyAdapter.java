package center.claims.mirascon.mirascon.Adapter;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import center.claims.mirascon.mirascon.Activity.Emergency;
import center.claims.mirascon.mirascon.Activity.MainActivity;
import center.claims.mirascon.mirascon.Models.Support;
import center.claims.mirascon.mirascon.R;


public class EmergencyAdapter extends RecyclerView.Adapter<center.claims.mirascon.mirascon.Adapter.EmergencyAdapter.MyViewHolder> {

    private Context mContext;
    private List<Support> mData;


    public EmergencyAdapter(Context mContext, List<Support> mData) {
        this.mContext = mContext; //get Activity of bottomfrag2
        this.mData = mData;
    }

    @Override
    public center.claims.mirascon.mirascon.Adapter.EmergencyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //get the initialize class ids

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);  //get the frag3 layout
        view = mInflater.inflate(R.layout.recyclerview_button, parent, false); //set the CardView Layout in frag3 layout
        return new center.claims.mirascon.mirascon.Adapter.EmergencyAdapter.MyViewHolder(view); //return the cardView
    }

    @Override
    public void onBindViewHolder(center.claims.mirascon.mirascon.Adapter.EmergencyAdapter.MyViewHolder holder, final int position) { //set the Data in all positions of the CardView

        holder.img_title.setText(mData.get(position).getTitle());
        holder.img_thumbnail.setImageResource(mData.get(position).getThumbnail());
        holder.cardView.setOnClickListener(new View.OnClickListener() {  //Click on one CardView to do something
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:110")); //Police
                    /*if (ActivityCompat.checkSelfPermission(mContext,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }*/
                    mContext.startActivity(callIntent);
                } else if (position == 1) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:112")); //Fire Department
                    mContext.startActivity(callIntent);
                } else if (position == 2) {
                    //DateFormat df = new SimpleDateFormat("HH");
                    //String hour = df.format(Calendar.getInstance().getTime());
                    //int currentHour = Integer.valueOf(hour);

                    //if (currentHour>9&&currentHour<17) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:0800"));//0800 Mirascon
                    mContext.startActivity(callIntent);
                    /*}else{
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setTitle("MIRASCON Claims Department Cologne Head Office:");
                        alertDialog.setMessage("Service hours Mon. – Fri. 9:00 – 17:00 h (closed on German holidays)");
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "VERSTANDEN",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }*/

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

            img_title = itemView.findViewById(R.id.call_title);
            img_thumbnail = itemView.findViewById(R.id.call_img);
            cardView = itemView.findViewById(R.id.cardview_id);


        }
    }

}

