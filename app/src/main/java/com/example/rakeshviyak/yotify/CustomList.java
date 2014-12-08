package com.example.rakeshviyak.yotify;

/**
 * Created by Balajee on 6/12/2014.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomList extends ArrayAdapter<String>{
    private final Activity context;
    private final List<String> title;
    private final List<String> message;
    private final List<String> time;
    private final List<Bitmap> imageId;
    private final List<String> tagNo;
    public CustomList(Activity context,
                      List<String> title, List<Bitmap> imageId,List<String> message,List<String> time, List<String> tagNo) {
        super(context, R.layout.list_item, title);
        this.context = context;
        this.title = title;
        this.imageId = imageId;
        this.message=message;
        this.time=time;
        this.tagNo=tagNo;
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item, null, true);
        //rowView.setTag(0,tagNo.get(position));
        final TextView txtTitle = (TextView) rowView.findViewById(R.id.txtTitle);
        final TextView txtMessage = (TextView) rowView.findViewById(R.id.txtMessage);
        final TextView txtTimeStamp = (TextView) rowView.findViewById(R.id.txtTimeStamp);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        //ImageButton rmvButton=(ImageButton)rowView.findViewById(R.id.removeButton);
        txtTitle.setText(title.get(position));
        txtMessage.setText(message.get(position));
        txtTimeStamp.setText(time.get(position));
        imageView.setImageBitmap(imageId.get(position));

//        rmvButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                txtTitle.setText("");
//                txtMessage.setText("");
//                txtTimeStamp.setText("");
//                imageView.setImageBitmap(null);
//
//            }
//        });

        return rowView;
    }
}
