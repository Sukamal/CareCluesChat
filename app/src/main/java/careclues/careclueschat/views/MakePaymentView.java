package careclues.careclueschat.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.AnswerSelectionListner;

public class MakePaymentView extends RelativeLayout implements View.OnClickListener{


    private View view;
    private Context context;
    private AnswerSelectionListner answerSelectionListner;


    @BindView(R.id.tvDrName)
    TextView tvDrName;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.btnPay)
    Button btnPay;



    public MakePaymentView(Context context) {
        super(context);
        initView(context);
    }

    public MakePaymentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MakePaymentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setAnsSelectionListner(AnswerSelectionListner listner){
        this.answerSelectionListner = listner;
    }


    private void initView(Context context){

        this.context = context;
        view = inflate(context, R.layout.view_make_payment,this);
        ButterKnife.bind(this, view);
        btnPay.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPay:
                if(answerSelectionListner != null){
                    answerSelectionListner.onPayButtonClick();
                }
                break;
        }
    }


}
