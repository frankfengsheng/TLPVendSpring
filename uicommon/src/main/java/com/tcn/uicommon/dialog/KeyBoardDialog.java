package com.tcn.uicommon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tcn.uicommon.R;
import com.tcn.uicommon.resources.ResourceUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public class KeyBoardDialog extends Dialog {
    private final static String PASS_WORD_DEFAULT = "73194653";
    private List<Button> buttons;
    private KeyBoardDialog.InputHanppens input;
    private List<Integer> ids;
    private TextView output_tv;
    private TextView time_tv;
    private String m_strPassWord = PASS_WORD_DEFAULT;

    private Button m_btn_back = null;
    private Button var7,var8;

    public int getLayout() {
        return 0;
    }

    public KeyBoardDialog(Context context) {
        super(context, R.style.Dialog_bocop);
        init(context);
    }

    private void init(Context context) {
        View contentView = View.inflate(context, getLayout(), null);
        setContentView(contentView);

        m_btn_back = (Button) findViewById(ResourceUtil.getId(context, "key_board_back"));
       // m_btn_back = (Button) findViewById(R.id.key_board_back);
        m_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KeyBoardDialog.this.input != null) {
                    dismiss();
                    KeyBoardDialog.this.input.onBack();
                }
            }
        });
        this.buttons = new ArrayList();
        this.ids = new ArrayList();
        for(int clear = 0; clear < 10; ++clear) {
            int enter = ResourceUtil.getId(context, "number" + clear);
            Button button = (Button)contentView.findViewById(enter);
            if(null != button) {
                this.buttons.add(button);
            }
        }

        Iterator var6 = this.buttons.iterator();

        Button var88;
        while(var6.hasNext()) {
            var88 = (Button)var6.next();
            var88.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Button b = (Button)view;
                    KeyBoardDialog.this.output(b.getText().toString(), view.getId());
                    if(KeyBoardDialog.this.input != null) {
                        KeyBoardDialog.this.input.onInputNumber(b.getText().toString(), view.getId(), System.currentTimeMillis());
                    }
                }
            });
        }

        var7 = (Button)findViewById(ResourceUtil.getId(context, "clear"));
        var8 = (Button)findViewById(ResourceUtil.getId(context, "enter"));
//        Button var7 = (Button)findViewById(R.id.clear);
//        var8 = (Button)findViewById(R.id.enter);

        this.output_tv = (TextView)findViewById(ResourceUtil.getId(context, "out_put"));
        this.time_tv = (TextView)findViewById(ResourceUtil.getId(context, "time_textview"));

//        this.output_tv = (TextView)findViewById(R.id.out_put);
//        this.time_tv = (TextView)findViewById(R.id.time_textview);

        var7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                KeyBoardDialog.this.clear(view.getId(), System.currentTimeMillis());
                if(KeyBoardDialog.this.input != null) {
                    String data = KeyBoardDialog.this.output_tv.getText().toString();
                    KeyBoardDialog.this.input.onClear(data);
                }

            }
        });
        var8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(KeyBoardDialog.this.output_tv != null) {
                    String data = KeyBoardDialog.this.output_tv.getText().toString();
                    if(KeyBoardDialog.this.input != null) {
                        KeyBoardDialog.this.input.onEnter(data);
                    }
                    KeyBoardDialog.this.enter();
                }

            }
        });
        var7.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                KeyBoardDialog.this.clearAll();
                if (KeyBoardDialog.this.input != null) {
                    KeyBoardDialog.this.input.onClearAll();
                }

                return true;
            }
        });

        time_tv.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                if(KeyBoardDialog.this.output_tv != null) {
                    String data = KeyBoardDialog.this.output_tv.getText().toString();
                    if (m_strPassWord.equals(data)) {
                        KeyBoardDialog.this.enter();
                        KeyBoardDialog.this.input.onPassWordVerified();
                    }
                }
                return true;
            }
        });
    }

    public void destroy() {
        if (buttons != null) {
            Iterator mvar6 = this.buttons.iterator();
            Button mvar8;
            while(mvar6.hasNext()) {
                mvar8 = (Button)mvar6.next();
                mvar8.setOnClickListener(null);
                mvar8 = null;
            }
        }
        buttons = null;
        if (var7 != null) {
            var7.setOnLongClickListener(null);
            var7.setOnClickListener(null);
            var7 = null;
        }
        if (var8 != null) {
            var8.setOnLongClickListener(null);
            var8.setOnClickListener(null);
            var8 = null;
        }
        input = null;
        ids = null;
        output_tv = null;
        if (time_tv != null) {
            time_tv.setOnLongClickListener(null);
            time_tv = null;
        }
    }

    public void setHintText(int resId) {
        if (output_tv != null) {
            output_tv.setHint(resId);
        }
    }

    public void setHintText(String test) {
        if (output_tv != null) {
            output_tv.setHint(test);
        }
    }

    public void setInputType(int type) {
        if (output_tv != null) {
            output_tv.setInputType(type);
            output_tv.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

    //method  例如：PasswordTransformationMethod.getInstance()
    public void setTransformationMethod(TransformationMethod method) {
        if (output_tv != null) {
            output_tv.setTransformationMethod(method);
        }
    }

    public void setCancelText(String data) {
        if (var7 != null) {
            var7.setText(data);
        }
    }

    public void setTime(String str) {
        if(this.time_tv != null) {
            this.time_tv.setText(str);
        }

    }

    public void setTime(float textSize,String data) {
        if(this.time_tv != null) {
            this.time_tv.setTextColor(Color.WHITE);
            this.time_tv.setTextSize(textSize);
            this.time_tv.setText(data);
        }

    }

    public void setTime(int color, float textSize,String data) {
        if(this.time_tv != null) {
            this.time_tv.setTextColor(color);
            this.time_tv.setTextSize(textSize);
            this.time_tv.setText(data);
        }

    }


    public void output(String str, int id) {
        if(null != this.output_tv && null != this.ids) {
            if(this.output_tv.getText().toString().equals("")) {
                this.ids.clear();
            }

            if(this.ids.size() < 9) {
                this.ids.add(Integer.valueOf(id));
            }

            String string = this.output_tv.getText().toString();
            this.output_tv.setText(string + str);
        }
    }

    public void clear(int id, long time) {
        if(null != this.output_tv) {
            String string = this.output_tv.getText().toString();
            if(null != string && 0 != string.length()) {
                if(this.ids.size() < 10) {
                    this.ids.add(Integer.valueOf(id));
                }

                if(1 == string.length()) {
                    this.ids.clear();
                }

                this.output_tv.setText(string.substring(0, string.length() - 1));
            }
        }
    }

    public void clearAll() {
        if(this.ids != null) {
            this.ids.clear();
        }

        if(this.output_tv != null) {
            this.output_tv.setText("");
        }

    }

    public void enter() {
        if(!this.output_tv.getText().toString().equals("")) {
            this.clearAll();
        }
    }

    public void setPassWord(String passWord) {
        this.m_strPassWord = passWord;
    }

    public void setInputListener(KeyBoardDialog.InputHanppens listener) {
        this.input = listener;
    }

    public interface InputHanppens {

        void onSelectGoods(String data);

        void onTakeGoods(String data);

        void onInputNumber(String var1, int var2, long var3);

        void onClear(String data);

        void onEnter(String data);

        void onClearAll();

        void onPassWordVerified();

        void onBack();
    }
}
