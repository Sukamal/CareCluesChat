package careclues.careclueschat.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import careclues.careclueschat.R;

public class MessageInputView extends RelativeLayout implements View.OnClickListener,TextWatcher{

    private ImageView ivSubmit;
    private EditText etMessage;
    private CharSequence input;

    public MessageInputView(Context context) {
        super(context);
        initView(context);
    }

    public MessageInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MessageInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context){

        inflate(context, R.layout.message_input_view,this);
        ivSubmit = (ImageView) findViewById(R.id.ib_submit);
        etMessage = findViewById(R.id.et_message);
        ivSubmit.setEnabled(false);
        ivSubmit.setOnClickListener(this);
        etMessage.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        input = s;
        ivSubmit.setEnabled(input.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {

    }
}
