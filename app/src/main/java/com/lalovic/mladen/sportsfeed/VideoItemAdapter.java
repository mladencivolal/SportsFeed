package com.lalovic.mladen.sportsfeed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsalf.smilerating.SmileRating;
import com.robert.autoplayvideo.CustomViewHolder;
import com.robert.autoplayvideo.VideosAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoItemAdapter extends VideosAdapter {
    private Context mContext;
    private List<VideoItem> mVideoItemsList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    VideoItemAdapter(Context context, List<VideoItem> videoItemsList) {
        mContext = context;
        mVideoItemsList = videoItemsList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item, parent, false);

        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {
        VideoItem currentItem = mVideoItemsList.get(position);

        String imagePoster = currentItem.getPoster();
        String creatorName = currentItem.getAuthor();
        String viewCount = currentItem.getViews();
        String imageFlag = currentItem.getFlag();
        String imageSport = currentItem.getSportUrl();
        String description = currentItem.getDescription();
        String videoUrl = currentItem.getUrl();

        String viewsText = "Shared by " + creatorName;
        SpannableStringBuilder ssb = new SpannableStringBuilder(viewsText);
        ForegroundColorSpan fcsWhite = new ForegroundColorSpan(Color.parseColor("#FAFAFA"));
        ssb.setSpan(fcsWhite, 9, viewsText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ((ExampleViewHolder) holder).mTextViewCreator.setText(ssb);
        ((ExampleViewHolder) holder).mTextViewLikes.setText(viewCount + " viewed this");
        ((ExampleViewHolder) holder).mTextViewDescription.setText(description);
        holder.setImageUrl(imagePoster);
        holder.setVideoUrl(videoUrl);
        holder.setLooping(true);

        ((ExampleViewHolder) holder).animateItemView();

        Picasso.get().load(imageFlag).fit().centerInside().into(((ExampleViewHolder) holder).mImageViewFlag);
        Picasso.get().load(imageSport).fit().centerInside().into(((ExampleViewHolder) holder).mImageViewSport);
        Picasso.get().load(holder.getImageUrl()).config(Bitmap.Config.RGB_565).into(holder.getImageView());

    }

    @Override
    public int getItemCount() {
        return mVideoItemsList.size();
    }

    public class ExampleViewHolder extends CustomViewHolder {
        private TextView mTextViewCreator;
        private TextView mTextViewLikes;
        private ImageView mImageViewFlag;
        private ImageView mImageViewSport;
        private TextView mTextViewDescription;
        private ImageView mShareBtn;

        SmileRating mSmileRating;

        private ImageView mImageViewUser;

        private RelativeLayout descriptionLayout;


        ExampleViewHolder(@NonNull final View itemView) {
            super(itemView);
            mTextViewCreator = itemView.findViewById(R.id.text_view_creator);
            mTextViewLikes = itemView.findViewById(R.id.text_view_views);
            mImageViewFlag = itemView.findViewById(R.id.image_view_flag);
            mImageViewSport = itemView.findViewById(R.id.image_view_sport);
            mTextViewDescription = itemView.findViewById(R.id.text_view_description);
            mShareBtn = itemView.findViewById(R.id.share);
            mImageViewUser = itemView.findViewById(R.id.image_view_user);
            descriptionLayout = itemView.findViewById(R.id.description_layout);
            mSmileRating = itemView.findViewById(R.id.rating_bar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

            mShareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postion = 0;

                    for (int i = 0; i<mVideoItemsList.size(); i++) {
                        if (mVideoItemsList.get(i).getUrl().equals(getVideoUrl())) {
                            postion = i;
                        }
                    }

                    String shareContent = "Check this new video:\n\n" + ":: " + mVideoItemsList.get(postion).getDescription()
                            + ":: \n\n" + getVideoUrl() + "\n\n*Sent from SportsFeed App!";

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
                    mContext.startActivity(Intent.createChooser(shareIntent, "Share video using"));
                }
            });
        }

        @Override
        public void videoStarted() {
            super.videoStarted();
        }

        void animateItemView() {
            final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(1000);

            mImageViewFlag.startAnimation(fadeIn);
            mImageViewSport.startAnimation(fadeIn);
            mTextViewLikes.startAnimation(fadeIn);
            mTextViewCreator.startAnimation(fadeIn);
            mImageViewUser.startAnimation(fadeIn);
            mSmileRating.startAnimation(fadeIn);
            mShareBtn.startAnimation(fadeIn);
            descriptionLayout.startAnimation(fadeIn);
        }
    }
}
