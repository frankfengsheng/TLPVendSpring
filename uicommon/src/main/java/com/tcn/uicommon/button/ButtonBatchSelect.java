package com.tcn.uicommon.button;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcn.uicommon.R;

/**
 * Created by Administrator on 2017/2/22.
 */
public class ButtonBatchSelect extends RelativeLayout {
    public static final int BUTTON_TYPE_HAVE_ALL                        = 1;
    public static final int BUTTON_TYPE_EDIT_TITLE_NO                   = 2;
    public static final int BUTTON_TYPE_TITLE_NO                        = 3;
    public static final int BUTTON_TYPE_TITLE_NO_AND_NO_RIGHT_BTN     = 4;
    public static final int BUTTON_TYPE_TITLE_NO_AND_NO_BTN           = 5;
    public static final int BUTTON_TYPE_TITLE_AND_NO_RIGHT_BTN           = 6;

    public static final int BUTTON_ID_LEFT = 1;
    public static final int BUTTON_ID_RIGHT = 2;

    private TextView btn_batch_select_name = null;
    private EditText btn_batch_select_edit = null;
    private EditText btn_batch_input_start = null;
    private EditText btn_batch_input_end = null;
    private Button btn_batch_btn_left = null;
    private Button btn_batch_btn_right = null;


    public ButtonBatchSelect(Context context) {
        super( context );
        initView(context);
    }

    public ButtonBatchSelect(Context context, AttributeSet attrs) {
        super( context, attrs );
        initView(context);
    }

    public ButtonBatchSelect(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        initView(context);
    }


    private void initView(Context context) {
        View.inflate(context, R.layout.button_batch_select, this);
        btn_batch_select_name = (TextView)findViewById(R.id.btn_batch_select_name);
        btn_batch_select_edit = (EditText)findViewById(R.id.btn_batch_select_edit);
        btn_batch_input_start = (EditText)findViewById(R.id.btn_batch_input_start);
        btn_batch_input_end = (EditText)findViewById(R.id.btn_batch_input_end);
        btn_batch_btn_left = (Button)findViewById(R.id.btn_batch_btn_left);
        btn_batch_btn_left.setOnClickListener(m_ClickListener);
        btn_batch_btn_right = (Button)findViewById(R.id.btn_batch_btn_right);
        btn_batch_btn_right.setOnClickListener(m_ClickListener);
    }

    public void setButtonType(int type) {
        if (BUTTON_TYPE_EDIT_TITLE_NO == type) {
            btn_batch_select_edit.setVisibility(GONE);
        } else if (BUTTON_TYPE_TITLE_NO == type) {
            btn_batch_select_name.setVisibility(GONE);
            btn_batch_select_edit.setVisibility(GONE);
        } else if (BUTTON_TYPE_TITLE_NO_AND_NO_RIGHT_BTN == type) {
            btn_batch_select_name.setVisibility(GONE);
            btn_batch_select_edit.setVisibility(GONE);
            btn_batch_btn_right.setVisibility(GONE);
        } else if (BUTTON_TYPE_TITLE_NO_AND_NO_BTN == type) {
            btn_batch_select_name.setVisibility(GONE);
            btn_batch_select_edit.setVisibility(GONE);
            btn_batch_btn_left.setVisibility(GONE);
            btn_batch_btn_right.setVisibility(GONE);
        } else if (BUTTON_TYPE_TITLE_AND_NO_RIGHT_BTN == type) {
            btn_batch_btn_right.setVisibility(GONE);
        }
        else {

        }
    }

    public void setButtonName(int resid) {
        if (btn_batch_select_name != null) {
            btn_batch_select_name.setText(resid);
        }
    }

    public void setButtonName(String text) {
        if (btn_batch_select_name != null) {
            btn_batch_select_name.setText(text);
        }
    }

    public void setButtonLeft(int resid) {
        if (btn_batch_btn_left != null) {
            btn_batch_btn_left.setText(resid);
        }
    }

    public void setButtonLeft(String text) {
        if (btn_batch_btn_left != null) {
            btn_batch_btn_left.setText(text);
        }
    }

    public void setButtonRight(int resid) {
        if (btn_batch_btn_right != null) {
            btn_batch_btn_right.setText(resid);
        }
    }

    public void setButtonRight(String text) {
        if (btn_batch_btn_right != null) {
            btn_batch_btn_right.setText(text);
        }
    }
    

    public void setEditTitleInputType(int type) {
        if (btn_batch_select_edit != null) {
            btn_batch_select_edit.setInputType(type);
        }
    }

    public String getTitleEditText() {
        String text = null;
        if (btn_batch_select_edit != null) {
            text = btn_batch_select_edit.getText().toString();
        }
        return text;
    }

    public String getStartEditText() {
        String text = null;
        if (btn_batch_input_start != null) {
            text = btn_batch_input_start.getText().toString();
        }
        return text;
    }

    public String getEndEditText() {
        String text = null;
        if (btn_batch_input_end != null) {
            text = btn_batch_input_end.getText().toString();
        }
        return text;
    }

    public void setButtonListener(ButtonListener listener) {
        m_ButtonListener = listener;
    }

    public void removeButtonListener() {
        if (btn_batch_btn_left != null) {
            btn_batch_btn_left.setOnClickListener(null);
        }
        if (btn_batch_btn_right != null) {
            btn_batch_btn_left.setOnClickListener(null);
        }
    }

    private ButtonListener m_ButtonListener = null;
    public interface ButtonListener {
        public void onClick(View v, int buttonId);
    }

    private ClickListener m_ClickListener = new ClickListener();
    private class ClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (null == v) {
                return;
            }
            int id = v.getId();
            if (R.id.btn_batch_btn_left == id) {
                if (m_ButtonListener != null) {
                    m_ButtonListener.onClick(ButtonBatchSelect.this,BUTTON_ID_LEFT);
                }
            } else if (R.id.btn_batch_btn_right == id) {
                if (m_ButtonListener != null) {
                    m_ButtonListener.onClick(ButtonBatchSelect.this,BUTTON_ID_RIGHT);
                }
            } else {

            }
        }
    }
}
