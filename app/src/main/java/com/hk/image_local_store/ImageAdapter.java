package com.hk.image_local_store;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.hk.image_local_store.util.Common;

import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<Photo> photoList;
    private Context context;
    private PhotoviewModel photoviewModel;


    public ImageAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
        photoviewModel = new ViewModelProvider((FragmentActivity) context).get(PhotoviewModel.class);
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        Photo photo = photoList.get(position);
        File imgFile = new File(photo.getImagePath());
        if (imgFile.exists()) {

            holder.imageView.setImageBitmap(
                    Common.decodeSampledBitmap(context, photo.getImagePath()));

        } else
            holder.imageView.setImageResource(R.drawable.insert_drive_file);

        holder.textView.setText(photo.getTitle());


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDelete(photo);
                return false;
            }
        });


    }

    public void showDelete(Photo photo) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Delete entry");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            // continue with delete
            photoviewModel.deletePhoto(photo);
            if (new File(photo.getImagePath()).exists())
                new File(photo.getImagePath()).delete();
            dialog.dismiss();
            Toast.makeText(context, "deleted!", Toast.LENGTH_SHORT).show();

        });
        alert.setNegativeButton(android.R.string.no, (dialog, which) -> {
            // close dialog
            dialog.cancel();
        });
        alert.show();
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photoIV);
            textView = itemView.findViewById(R.id.photoTV);
        }
    }
}
