package com.tcn.uicommon.view;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tcn.uicommon.resources.ResourceUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KeyBoardView extends Fragment {
    private final static String PASS_WORD_DEFAULT = "73194653";
    private List<Button> buttons;
    private KeyBoardView.InputHanppens input;
    private List<Integer> ids;
    private TextView output_tv;
    private TextView time_tv;
    private String m_strPassWord = PASS_WORD_DEFAULT;
    private Button back;
    private Button var7,var8;

    public int getLayout() {
        return 0;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = View.inflate(this.getActivity(), getLayout(), null);
        this.initView(this.getActivity(), contentView);
//        back = (Button) contentView.findViewById(R.id.key_board_back);
        return contentView;
    }

    public  void setBack(){
         this.clearAll();
    }
    public void hideBack(){
        back.setVisibility(View.GONE);
    }
    protected void initView(Context context, View contentView) {
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
            var88.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Button b = (Button)view;
                    KeyBoardView.this.output(b.getText().toString(), view.getId());
                    if(KeyBoardView.this.input != null) {
                        KeyBoardView.this.input.onInputNumber(b.getText().toString(), view.getId(), System.currentTimeMillis());
                    }

                }
            });
        }

        var7 = (Button)contentView.findViewById(ResourceUtil.getId(context, "clear"));
        var8 = (Button)contentView.findViewById(ResourceUtil.getId(context, "enter"));

        this.output_tv = (TextView)contentView.findViewById(ResourceUtil.getId(context, "out_put"));
        this.time_tv = (TextView)contentView.findViewById(ResourceUtil.getId(context, "time_textview"));

        var7.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                KeyBoardView.this.clear(view.getId(), System.currentTimeMillis());
                if(KeyBoardView.this.input != null) {
                    String data = KeyBoardView.this.output_tv.getText().toString();
                    KeyBoardView.this.input.onClear(data);
                }

            }
        });
        var8.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if(KeyBoardView.this.output_tv != null) {
                    String data = KeyBoardView.this.output_tv.getText().toString();
                    if(KeyBoardView.this.input != null) {
                        KeyBoardView.this.input.onEnter(data);
                    }
                    KeyBoardView.this.enter();
                }

            }
        });
        var7.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                KeyBoardView.this.clearAll();
                if (KeyBoardView.this.input != null) {
                    KeyBoardView.this.input.onClearAll();
                }

                return true;
            }
        });

        time_tv.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                longClickInput();
                return true;
            }
        });

//        back = (Button) contentView.findViewById(ResourceUtil.getId(context, "key_board_back"));
//        back.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setBack();
//            }
//        });
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

    protected void longClickInput() {
        if(KeyBoardView.this.output_tv != null) {
            String data = KeyBoardView.this.output_tv.getText().toString();
            if (m_strPassWord.equals(data)) {
                KeyBoardView.this.enter();
                KeyBoardView.this.input.onPassWordVerified();
            }
        }
    }

    public void setHintText(int resId) {
        if (output_tv != null) {
            output_tv.setHint(resId);
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

    public void setInputListener(KeyBoardView.InputHanppens listener) {
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
    }
    private int time = 0;
    private String strTime = "";

    @Override
    public void onDestroy() {
        destroy();
        time = 0;
        time_tv = null;
        output_tv = null;
        ids = null;
        buttons = null;
        super.onDestroy();
    }
}