package com.tacttiles.touche.chat;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tacttiles.touche.R;

import java.util.ArrayList;
import java.util.List;

public abstract class ChatActivity extends AppCompatActivity {

    public static final String TAG = "ChatActivity";

    private static final String[] ANIM = new String[4];

    static {
        StringBuilder sb = new StringBuilder();
        for (int a = 0; a <= 3; a++) {
            sb.setLength(0);
            for (int b = 0; b <= 3; b++) {
                sb.append(a == b ? "· " : ". ");
            }
            ANIM[a] = sb.toString();
        }
    }

    public class ChatMessage {
        public static final int STATE_DEFAULT = 0;
        public static final int STATE_INFO = 1;
        public static final int STATE_DONE = 2;

        public int state = STATE_DEFAULT;
        public boolean in;
        public String message;
        public int progress = 0;
        public int pos;

        public ChatMessage(boolean in, String message) {
            super();
            this.in = in;
            this.message = message;
        }

        public void setText(TextView tv) {
            if (in) {
                if (state == STATE_DEFAULT) {
                    tv.setText(ANIM[aID]);
                } else {
                    tv.setText(message);
                }
            } else {
                if (state == STATE_INFO) {
                    Spannable span = new SpannableString(message);
                    if (progress > 0) {
                        span.setSpan(new ForegroundColorSpan(Color.BLACK), 0, progress - 1, 0);
                    }
                    span.setSpan(new ForegroundColorSpan(Color.BLUE), progress, progress, 0);
                    if (progress + 1 < message.length()) {
                        span.setSpan(new ForegroundColorSpan(Color.GRAY), progress + 1, message.length(), 0);
                        tv.setText(span);
                        return;
                    } else {
                        state = STATE_DONE;
                        currentOutMessage = null;
                    }
                }
                tv.setText(message);
            }
        }
    }

    private class DynamicArrayAdapter extends ArrayAdapter<ChatMessage> {

        private List<ChatMessage> data;

        public DynamicArrayAdapter(Context context, int textViewResourceId, List<ChatMessage> data) {
            super(context, textViewResourceId, data);
            this.data = data;
        }

        public void addMessage(ChatMessage msg) {
            data.add(msg);
            notifyUpdate();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.activity_chat_singlemessage, parent, false);
            }

            LinearLayout singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
            final ChatMessage chatMessageObj = getItem(position);
            TextView chatText = (TextView) row.findViewById(R.id.singleMessage);
            chatMessageObj.pos = position;
            chatText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick");
                    if (chatMessageObj.state != ChatMessage.STATE_DONE) {
                        if (chatMessageObj == currentInMessage) {
                            if (chatMessageObj.state == ChatMessage.STATE_DEFAULT) {
                                chatMessageObj.state = ChatMessage.STATE_INFO;
                            } else {
                                chatMessageObj.state = ChatMessage.STATE_DEFAULT;
                            }
                        } else if (chatMessageObj == currentOutMessage) {
                            if (chatMessageObj.state == ChatMessage.STATE_DEFAULT) {
                                chatMessageObj.state = ChatMessage.STATE_INFO;
                            } else {
                                chatMessageObj.state = ChatMessage.STATE_DEFAULT;
                            }
                        }
                    }
                    notifyUpdate();
                }
            });

            chatMessageObj.setText(chatText);
            singleMessageContainer.setGravity(chatMessageObj.in ? Gravity.LEFT : Gravity.RIGHT);

            int paddingDP = (int) (15 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);
            int l = chatMessageObj.in ? paddingDP : 0;
            int r = chatMessageObj.in ? 0 : paddingDP;
            chatText.setPadding(l, 0, r, 0);

            LinearLayout bubble = (LinearLayout) row.findViewById(R.id.bubble);

            //Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), chatMessageObj.in ? R.drawable.in : R.drawable.out);
            //ColorMatrix matrix = new ColorMatrix();
            //matrix.setSaturation(0);
            //ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            //drawable.setColorFilter(filter);
            //bubble.setBackgroundDrawable(drawable);

            bubble.setBackgroundResource(chatMessageObj.in ? R.drawable.in : R.drawable.out);

            return row;
        }

        public void notifyUpdate() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }

    private DynamicArrayAdapter arrayAdapter;
    private ListView listView;
    private Button buttonSend;
    private int aID = 0;
    private boolean sendButtonEnabled = false;

    private ChatMessage currentInMessage;
    private ChatMessage currentOutMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.activity_chat);

        buttonSend = (Button) findViewById(R.id.buttonSend);

        listView = (ListView) findViewById(R.id.listView1);

        arrayAdapter = new DynamicArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage, new ArrayList<ChatMessage>());

        listView.setAdapter(arrayAdapter);
        listView.setDividerHeight(0);
        listView.setDivider(null);

        final EditText chatText = (EditText) findViewById(R.id.chatText);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    newOutMessage(chatText.getText().toString());
                    chatText.setText("");
                    return true;
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                newOutMessage(chatText.getText().toString());
                chatText.setText("");
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(arrayAdapter); //TODO again?

        //to scroll the list view to bottom on data change
        arrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setSelection(arrayAdapter.getCount() - 1);
                    }
                });
            }
        });

        new Thread("String Animation Thread") {
            public void run() {
                while (true) {
                    aID = aID == 3 ? 0 : aID + 1;
                    if (currentInMessage != null && currentInMessage.state == ChatMessage.STATE_DEFAULT) {
                        arrayAdapter.notifyUpdate();
                    }
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {

                    }
                }
            }
        }.start();

        //addMessage(false, "ola");
        //addMessage(false, "oi, tudo bem?");
        //addMessage(true, "Tudo bem sim e voce?");
        //addMessage(false, "Eu vou bem. Ontem eu fui na casa de um amigo que fazia muito tempo que eu nao o via!");
    }

    public void enableSendButton(boolean value) {
        sendButtonEnabled = value;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttonSend.setEnabled(sendButtonEnabled);
                if (sendButtonEnabled) {
                    Toast.makeText(getApplicationContext(), "Glove Connected!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Glove Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendButtonPressed(EditText editText) {
        if (sendButtonEnabled) {
            newOutMessage(editText.getText().toString());
            editText.setText("");
        } else {
            enableSendButton(false);
        }
    }

    private void newOutMessage(String msg) {
        ChatMessage chatMessage = new ChatMessage(false, msg);
        currentOutMessage = chatMessage;
        arrayAdapter.addMessage(chatMessage);
        onSendMessage(msg);
    }

    public void addMessage(boolean in, String msg) {
        ChatMessage chatMessage = new ChatMessage(in, msg);
        chatMessage.state = ChatMessage.STATE_DONE;
        arrayAdapter.addMessage(chatMessage);
    }

    public void setCurrentOutMessageProgress(int index) {
        if (currentOutMessage != null) {
            currentOutMessage.progress = index;
            arrayAdapter.notifyUpdate();
        }
    }

    public void appendToCurrentInMessage(final String str) {
        if (currentInMessage == null) {
            ChatMessage chatMessage = new ChatMessage(true, str);
            currentInMessage = chatMessage;
            arrayAdapter.addMessage(chatMessage);
        } else {
            currentInMessage.message += str;
            arrayAdapter.notifyUpdate();
        }
    }

    public void flushCurrentInMessage() {
        if (currentInMessage != null) {
            currentInMessage.state = ChatMessage.STATE_DONE;
            currentInMessage = null;
            arrayAdapter.notifyUpdate();
        }
    }

    public abstract void onSendMessage(String msg);


}