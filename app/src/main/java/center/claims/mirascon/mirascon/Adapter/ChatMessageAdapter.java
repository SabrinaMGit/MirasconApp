package center.claims.mirascon.mirascon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import center.claims.mirascon.mirascon.Models.ChatMessage;
import center.claims.mirascon.mirascon.R;

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, MY_IMAGE = 2, OTHER_IMAGE = 3;

    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        super(context, R.layout.user_message, data);
    }

    @Override
    public int getViewTypeCount() {
        // my message, other message, my image, other image
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage item = getItem(position);

        if (item.isMine() && !item.isImage()) return MY_MESSAGE;
        else if (!item.isMine() && !item.isImage()) return OTHER_MESSAGE;
        else if (item.isMine() && item.isImage()) return MY_IMAGE;
        else return OTHER_IMAGE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == MY_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_message, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());

        } else if (viewType == OTHER_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.supporter_message, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        } else if (viewType == MY_IMAGE) {
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_image, parent, false);
        } else {
            // convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_image, parent, false);
        }

        convertView.findViewById(R.id.chatMessageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "onClick", Toast.LENGTH_LONG).show();
            }
        });


        return convertView;
    }
}
