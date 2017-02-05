package kz.sagrad.ufix;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Timur_hnimdvi on 2/4/2017.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    private static String TAG = "ChatListAdapter";
    private List<WChat> chatList;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.w(TAG, "onCreateViewHolder: run");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.w(TAG, "onBindViewHolder: " + position);
        WChat wChat = chatList.get(position);
        holder.subject.setText(wChat.getSubject());
        holder.text.setText(wChat.getId());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView subject, text;
        public MyViewHolder(View view) {
            super(view);
            subject = (TextView) view.findViewById(R.id.textView1);
            text = (TextView) view.findViewById(R.id.textView2);
        }
    }

    public ChatListAdapter(List<WChat> chatList) {
        this.chatList = chatList;
        Log.w(TAG, "ChatListAdapter: new");
    }
}
