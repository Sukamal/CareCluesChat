package careclues.careclueschat.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.AnswerSelectionListner;

public class ChatImageView extends RelativeLayout implements View.OnClickListener{


    private View view;
    private Context context;
    private String imageUrl;

    @BindView(R.id.ivDocImageage)
    ImageView ivDocImageage;
    @BindView(R.id.pbProgress)
    ProgressBar pbProgress;

    public ChatImageView(Context context) {
        super(context);
        initView(context);
    }

    public ChatImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChatImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){

        this.context = context;
        view = inflate(context, R.layout.view_chat_image,this);
        ButterKnife.bind(this, view);

    }

    public void setImage(String imageUrl){
        this.imageUrl = imageUrl;
        showImage();
    }

    private void showImage(){
        Picasso.with(context)
                .load(imageUrl)
                .into(ivDocImageage);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivDocImageage:
                break;
        }
    }


}
