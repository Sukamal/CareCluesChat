package careclues.careclueschat.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.AnswerSelectionListner;

public class EndConsultationView extends ConstraintLayout implements View.OnClickListener {

    private Context context;
//    private Button btnConsultAgain;
//    private Button btnWriteReview;

    AnswerSelectionListner answerSelectionListner;


    public EndConsultationView(Context context) {
        super(context);
        initView(context);
    }

    public EndConsultationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EndConsultationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        this.context = context;
        inflate(context, R.layout.view_end_consultation,this);
//        ivSubmit = (ImageButton) findViewById(R.id.ib_submit);
//        ibAttachement = (ImageButton) findViewById(R.id.ib_attachement);
//
//        etMessage = findViewById(R.id.et_message);
//        ivSubmit.setEnabled(false);
//        ivSubmit.setOnClickListener(this);
//        ibAttachement.setOnClickListener(this);
//        etMessage.addTextChangedListener(this);
    }

    public void setAnsSelectionListner(AnswerSelectionListner listner) {
        this.answerSelectionListner = listner;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_consult_again:
                answerSelectionListner.onWriteAReview();
                break;
            case R.id.btn_write_review:
                answerSelectionListner.onEndConsultation();
                break;
        }
    }
}


