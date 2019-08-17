package pl.wnb.communicator.adapter;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.wnb.communicator.R;
import pl.wnb.communicator.model.Message;

public class MessageAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messages = new ArrayList<>();

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        if (message.isMyMsg()) {
            convertView = messageInflater.inflate(R.layout.msg_right, null);

            holder.messageBody = convertView.findViewById(R.id.textViewMessage);
            holder.date = convertView.findViewById(R.id.timestamp);

            convertView.setTag(holder);

            holder.messageBody.setText(message.getContent());
            holder.date.setText(message.getDate());

        } else {
            convertView = messageInflater.inflate(R.layout.msg_left, null);

            holder.name = convertView.findViewById(R.id.senderName);
            holder.messageBody = convertView.findViewById(R.id.textViewMessage);
            holder.date = convertView.findViewById(R.id.timeStamp);

            convertView.setTag(holder);

            holder.name.setText(message.getName());
            holder.messageBody.setText(message.getContent());
            holder.date.setText(message.getDate());

        }
        return convertView;
    }

    class MessageViewHolder {
        public TextView name;
        TextView messageBody;
        TextView date;
    }
}

