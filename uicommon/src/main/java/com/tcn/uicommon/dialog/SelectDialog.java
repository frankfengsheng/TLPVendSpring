package com.tcn.uicommon.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.tcn.uicommon.R;
import com.tcn.uicommon.button.ButtonEdit;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */
public class SelectDialog extends AlertDialog.Builder {

    public static final int BUTTON_ADD          = 1;
    public static final int BUTTON_DECREASE    = 2;
    public static final int BUTTON_POSITIVE    = 3;
    public static final int BUTTON_NEGATIVE    = 4;

    public static final int DIALOG_TYPE_DEFAULT    = 1;
    public static final int DIALOG_TYPE_SELECT    = 2;

    private int m_iCheckedItem = 0;
    private String[] m_strData = null;
    private ButtonEdit m_ButtonEdit = null;

    private TextView m_TextView = null;
    private EditText m_EditText = null;
    private LinearLayout m_LinearLayout = null;
    private Button select_dialog_add_button = null;
    private Button select_dialog_dec_button = null;

    public SelectDialog(Context context) {
        super(context);
        init(context,DIALOG_TYPE_DEFAULT);
    }

    public SelectDialog(Context context,int type) {
        super(context);
        init(context,type);
    }

    private void init(Context context,int type) {
        if (type == DIALOG_TYPE_DEFAULT) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.select_dialog,null);
            setView(layout);
            m_TextView = (TextView)layout.findViewById(R.id.select_dialog_text);
            m_EditText = (EditText)layout.findViewById(R.id.select_dialog_editText);
            m_LinearLayout = (LinearLayout)layout.findViewById(R.id.select_dialog_btn_layout);
            select_dialog_add_button = (Button)layout.findViewById(R.id.select_dialog_add_button);
            select_dialog_add_button.setOnClickListener(m_ClickListener);
            select_dialog_dec_button = (Button)layout.findViewById(R.id.select_dialog_dec_button);
            select_dialog_dec_button.setOnClickListener(m_ClickListener);
        }
    }

    public void deInit() {
        if (m_ButtonEdit != null) {
            m_ButtonEdit.removeButtonListener();
            m_ButtonEdit = null;
        }
        m_TextView = null;
        m_EditText = null;
        m_LinearLayout = null;
        if (select_dialog_add_button != null) {
            select_dialog_add_button.setOnClickListener(null);
            select_dialog_add_button = null;
        }
        if (select_dialog_dec_button != null) {
            select_dialog_dec_button.setOnClickListener(null);
            select_dialog_dec_button = null;
        }
        m_ClickListener = null;
        m_ButtonListener = null;
        m_PositiveListener = null;
        m_NegativeListener = null;
        m_ChoiceItemsListener = null;
    }

    public void setSingleChoiceItems(String[] data,int checkedItem) {
        m_strData = data;
        m_iCheckedItem = checkedItem;
        setSingleChoiceItems(data,checkedItem,m_ChoiceItemsListener);
    }

    public void setPositiveButton(String text) {
        setPositiveButton(text,m_PositiveListener);
    }

    public void setPositiveButton(int textId) {
        setPositiveButton(textId,m_PositiveListener);
    }

    public void setNegativeButton(String text) {
        setNegativeButton(text,m_NegativeListener);
    }

    public void setNegativeButton(int textId) {
        setNegativeButton(textId,m_NegativeListener);
    }

    public void show(ButtonEdit view, String[] data, int checkedItem) {
        if ((null == view) || (null == data)) {
            return;
        }
        m_ButtonEdit = view;
        notifydatachanged(data,checkedItem);
        show();
    }

    public void show(ButtonEdit view, List<String> data, int checkedItem) {
        if ((null == view) || (null == data)) {
            return;
        }
        m_ButtonEdit = view;
        notifydatachanged(data,checkedItem);
        show();
    }

    public void show(ButtonEdit view, String[] data, String checkedItemData) {
        if ((null == view) || (null == data) || (null == checkedItemData)) {
            return;
        }
        int checkedItem = -1;
        for (int i = 0; i < data.length; i++) {
            if (checkedItemData.equals(data[i])) {
                checkedItem = i;
            }
        }
        show(view, data, checkedItem);
    }

    public void show(String[] data, int checkedItem) {
        if (null == data) {
            return;
        }
        notifydatachanged(data,checkedItem);
        show();
    }

    public void show(List<String> data, int checkedItem) {
        if (null == data) {
            return;
        }
        notifydatachanged(data,checkedItem);
        show();
    }

    public void notifydatachanged(String[] data,int checkedItem) {
        if (null == data) {
            return;
        }
        setSingleChoiceItems(data,checkedItem);
    }

    public void notifydatachanged(List<String> data,int checkedItem) {
        if (null == data) {
            return;
        }
        String[] targetArr = new String[data.size()];
        data.toArray(targetArr);
        setSingleChoiceItems(targetArr,checkedItem);
    }

    private ChoiceItemsOnClickListener m_ChoiceItemsListener = new ChoiceItemsOnClickListener();
    private class ChoiceItemsOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            m_iCheckedItem = which;
        }
    }

    private PositiveOnClickListener m_PositiveListener = new PositiveOnClickListener();
    private class PositiveOnClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if ((null == m_strData) || (m_strData.length <= m_iCheckedItem) || (null == m_ButtonEdit) || (m_iCheckedItem < 0)) {
                return;
            }
            m_ButtonEdit.setButtonText(m_strData[m_iCheckedItem]);
            if (m_ButtonListener != null) {
                m_ButtonListener.onClick(m_ButtonEdit,BUTTON_POSITIVE,m_strData[m_iCheckedItem]);
            }
        }
    }

    private NegativeOnClickListener m_NegativeListener = new NegativeOnClickListener();
    private class NegativeOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if ((null == m_strData) || (m_strData.length <= m_iCheckedItem) || (m_iCheckedItem < 0)) {
                return;
            }
            if (m_ButtonListener != null) {
                m_ButtonListener.onClick(m_ButtonEdit,BUTTON_NEGATIVE,m_strData[m_iCheckedItem]);
            }
        }
    }

    private ClickListener m_ClickListener = new ClickListener();
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (null == v) {
                return;
            }
            if (null == m_EditText) {
                return;
            }
            String mData = m_EditText.getText().toString();
            if ((null == mData) || (mData.isEmpty())) {
                return;
            }
            int id = v.getId();
            if (R.id.select_dialog_add_button == id) {
                if (m_ButtonListener != null) {
                    m_ButtonListener.onClick(m_EditText,BUTTON_ADD,mData);
                }
            } else if (R.id.select_dialog_dec_button == id) {
                if (m_ButtonListener != null) {
                    m_ButtonListener.onClick(m_EditText,BUTTON_DECREASE,mData);
                }
            } else {

            }
        }
    }

    public void setButtonListener(ButtonListener listener) {
        m_ButtonListener = listener;
    }

    private ButtonListener m_ButtonListener = null;
    public interface ButtonListener {
        public void onClick(View view, int buttonId,String itemData);
    }

}
