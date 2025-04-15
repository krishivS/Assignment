package com.example.questions_5;
import android.content.Context;
import android.view.*;
import android.widget.*;
import android.graphics.*;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<String> imagePaths;

    public ImageAdapter(Context context, List<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return imagePaths.size();
    }

    @Override
    public Object getItem(int i) {
        return imagePaths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(i)));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
        return imageView;
    }
}
