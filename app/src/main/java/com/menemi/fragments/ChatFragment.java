package com.menemi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.customviews.ObservableScrollView;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.stream.messages.DialogResponceMessage;
import com.menemi.dbfactory.stream.messages.DialogSendMessage;
import com.menemi.dbfactory.stream.messages.RecievedMessage;
import com.menemi.dbfactory.stream.messages.ResponceReadMessage;
import com.menemi.dbfactory.stream.messages.StreamMessage;
import com.menemi.dbfactory.stream.messages.TypingMessage;
import com.menemi.personobject.DialogInfo;
import com.menemi.personobject.DialogMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.rockerhieu.emojicon.EmojiconEditText;

/**
 * Created by Ui-Developer on 12.08.2016.
 */
public class ChatFragment extends Fragment {
    private View rootView = null;
    private DialogInfo dialogInfo = null;
    int messagesShown = 10;
    int lastSenderID = -100;
    private  LinkedList<MessageFragment> messageFragments = new LinkedList<>();

    public void setDialogInfo(DialogInfo dialogInfo) {
        this.dialogInfo = dialogInfo;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.dialog_layout, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }

        }
        configureToolbar(dialogInfo);
        prepareMessages(dialogInfo, 1, 10, true);


        TypingMessage.addOnRecieveListener(StreamMessage.ConnectorCommands.ZCMD_NOTYFICATION_TYPING, new OnContactTypingListener());
        ObservableScrollView scrollView = (ObservableScrollView) rootView.findViewById(R.id.content);
        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                View view = scrollView.getChildAt(0);
                if (view.getTop() == y) {
                    Log.d("Scroll", "top reached");
                    prepareMessages(dialogInfo, messagesShown+1, 5, false);
                    messagesShown += 5;

                }

            }
        });
        LinearLayout typingContainer = (LinearLayout) rootView.findViewById(R.id.typingContainer);
        typingContainer.setVisibility(View.GONE);


        ImageView attachPhotoButton = (ImageView) rootView.findViewById(R.id.attachPhotoButton);
        attachPhotoButton.setOnClickListener(new OnAttachPhotoClickListener());
        ImageView send = (ImageView) rootView.findViewById(R.id.send);
        send.setOnClickListener(new OnSendListener());
        EmojiconEditText messageText = (EmojiconEditText) rootView.findViewById(R.id.messageText);
        messageText.addTextChangedListener(new OnTypeListener());
        RecievedMessage.addOnRecieveListener(StreamMessage.ConnectorCommands.ZCMD_RECEIVED_MESSAGE, new OnMessageRecieve());
        DialogResponceMessage.addOnRecieveListener(StreamMessage.ConnectorCommands.ZCMD_SEND_MESSAGE_RESPONSE, new OnMessageResponceListener());
        ResponceReadMessage.addOnRecieveListener(StreamMessage.ConnectorCommands.ZCMD_READ_MESSAGE_RESPONSE, new OnSendedMessageReadedListener());
        return rootView;
    }
    class OnMessageResponceListener implements StreamMessage.OnRecieveListener{

        @Override
        public void onRecieve(final StreamMessage message) {
            Log.d("COMMAND", "dialogResponceMessage ");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogResponceMessage dialogResponceMessage = (DialogResponceMessage) message;
                    Log.d("COMMAND", "dialogResponceMessage " + dialogResponceMessage.getMessageID());
                    for (int i = 0; i < messageFragments.size(); i++) {
                        Log.v("RND", " msg " + messageFragments.get(i).getRND());
                        Log.v("RND2", " rec " + dialogResponceMessage.getRnd());
                        if (messageFragments.get(i).compareRND(dialogResponceMessage.getRnd())) {
                            messageFragments.get(i).setStatus(MessageFragment.STATUS.SEND);
                        }
                    }
                }
            });
        }
    }
    ArrayList<DialogMessage> dialogMessages = new ArrayList<>();
    boolean hasMoreMessages = true;
    private void prepareMessages(DialogInfo dialogInfo, int offset, final int count, final boolean toEnd) {

        if (hasMoreMessages) {
            DBHandler.getInstance().getMessagesList(count, offset, dialogInfo.getDialogID(), new DBHandler.ResultListener() {
                @Override
                public void onFinish(Object object) {
                    ArrayList<DialogMessage> loadedDialogMessages = (ArrayList<DialogMessage>) object;
                    if (loadedDialogMessages.size() < count) {
                        hasMoreMessages = false;
                    }
                    dialogMessages.addAll(loadedDialogMessages);
                    ViewGroup root = (ViewGroup) getActivity().findViewById(R.id.fragment1);
                    root.removeAllViews();

                    messagesShown = dialogMessages.size();
                    for (int i = dialogMessages.size() - 1; i >= 0; i--) {
                        addMessage(dialogMessages.get(i), toEnd);
                    }
                }
            });
        }

    }

    private void addMessage(DialogMessage message, boolean toEnd) {
        boolean isFirst = lastSenderID != message.getProfileId();
        Log.d("FIRST", "lastSenderID " + lastSenderID + " message.getProfileId() " + message.getProfileId() + " lastSenderID != message.getProfileId() " + (lastSenderID != message.getProfileId()));
        lastSenderID = message.getProfileId();

        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setFirst(isFirst);
        messageFragment.setMessage(message);
        if (!message.isOwner()) {
            messageFragment.setCropedAvatar(dialogInfo.getConatactAvatar());
        }


            messageFragments.add(messageFragment);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment1, messageFragment);
            fragmentTransaction.commitAllowingStateLoss();
        if(toEnd) {
            scrollDown();
        }


    }

    public void scrollDown() {
        final ScrollView content = (ScrollView) rootView.findViewById(R.id.content);
        content.postDelayed(new Runnable() {

            @Override
            public void run() {
                content.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);
    }

    private void configureToolbar(DialogInfo dialogInfo) {
        Toolbar toolbar = PersonPage.getToolbar();

        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();


        toolbarContainer.addView(View.inflate(getActivity(), R.layout.ab_chat, null));

        TextView screenTitle = (TextView) toolbarContainer.findViewById(R.id.screenTitle);
        screenTitle.setText("" + dialogInfo.getContactName());


        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(PersonPage.getMenuListener());


    }

    class OnAttachPhotoClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AttachPhotoDialogFragment dialogFragment = new AttachPhotoDialogFragment();
            dialogFragment.setParent(ChatFragment.this);
            dialogFragment.show(getFragmentManager(), "Dialog Fragment");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.v("AttachPhotoDialog", "requestCode " + requestCode);
        if (requestCode != Activity.RESULT_CANCELED) {
            Bitmap bitmap = null;
            if (requestCode == AttachPhotoDialogFragment.SELECTED_PICTURES_FROM_GIZMO && resultCode == Activity.RESULT_OK && null != data) {
                Uri selectedImage = null;
                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                bitmap = BitmapFactory.decodeFile(picturePath);

                cursor.close();
                DBHandler.getInstance().sendPictureMessage(dialogInfo.getProfileId(), bitmap);
                Log.d("onActivityResult", "camera" + data);

            } else if (requestCode == AttachPhotoDialogFragment.SELECTED_PICTURES_FROM_CAMERA && resultCode == Activity.RESULT_OK) {

                Uri selectedImage = null;
                Log.d("onActivityResult", "GOVNOCODE " + AttachPhotoDialogFragment.PICTURE_PATH);
                File image = new File(AttachPhotoDialogFragment.PICTURE_PATH);
                Log.d("onActivityResult", "" + image.exists());
                selectedImage = Uri.fromFile(image);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    DBHandler.getInstance().sendPictureMessage(dialogInfo.getProfileId(), bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("onActivityResult", "camera" + data);

            }

        }
        Log.d("onActivityResult", "" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        //uiHelper.onActivityResult(requestCode, resultCode, data);

    }

    class OnSendListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
                send();


        }
        private void send(){
            EmojiconEditText messageText = (EmojiconEditText) rootView.findViewById(R.id.messageText);
            if(messageText.getText().toString().equals("")){
                return;
            }

            DialogSendMessage sendedMessage = DBHandler.getInstance().sendTextMessage(dialogInfo.getDialogID(), dialogInfo.getProfileId(), messageText.getText().toString());
            DialogMessage message = new DialogMessage(sendedMessage);
            messageText.setText("");
            addMessage(message, true);
        }
    }

    class OnTypeListener implements TextWatcher {
        Thread timer = null;
        AtomicBoolean isTyping = new AtomicBoolean(false);

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!isTyping.get()) {
                isTyping.set(true);
                if (timer != null) {
                    timer.interrupt();
                    return;
                }
                Log.d("Typing", "typing");
                DBHandler.getInstance().sendTypingMessage(dialogInfo.getProfileId(), true);

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            Log.d("Typing", "stop");
            if(timer!=null) {
                timer.interrupt();
            }
            timer = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000l);
                        isTyping.set(false);
                        DBHandler.getInstance().sendTypingMessage(dialogInfo.getProfileId(), false);
                        timer = null;
                        Log.d("Typing", "stop 2s");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            timer.start();
        }
    }

    class OnMessageRecieve implements StreamMessage.OnRecieveListener {

        @Override
        public void onRecieve(final StreamMessage message) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    RecievedMessage recievedMessage = (RecievedMessage) message;
                    if(recievedMessage.getDialogID() == dialogInfo.getDialogID()) {
                        Log.d("ZCMD_RECEIVED_MESSAGE", "" + recievedMessage.getMessageBody());
                        ArrayList<Integer> msgIds = new ArrayList<Integer>();
                        msgIds.add(recievedMessage.getMessageId());
                        addMessage(new DialogMessage(recievedMessage), true);
                        DBHandler.getInstance().sendReadMessage(dialogInfo.getDialogID(), msgIds);
                    }
                }
            });
        }
    }

    class OnContactTypingListener implements StreamMessage.OnRecieveListener {

        @Override
        public void onRecieve(final StreamMessage message) {
            if(getActivity() == null){
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayout typingContainer = (LinearLayout) rootView.findViewById(R.id.typingContainer);
                    TypingMessage typingMessage = (TypingMessage) message;
                    Log.d("TYPING", "listener");
                    if (typingMessage.getFromProfileID() == dialogInfo.getProfileId() && typingMessage.getIsTyping()) {
                        typingContainer.setVisibility(View.VISIBLE);
                        Log.d("TYPING", "visible");
                    } else if (typingMessage.getFromProfileID() == dialogInfo.getProfileId() && !typingMessage.getIsTyping()) {
                        typingContainer.setVisibility(View.GONE);
                        Log.d("TYPING", "not visible");
                    }
                }
            });

        }


    }

    private class OnSendedMessageReadedListener implements StreamMessage.OnRecieveListener {
        @Override
        public void onRecieve(StreamMessage message) {
            if(getActivity() == null){
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //LinearLayout typingContainer = (LinearLayout) rootView.findViewById(R.id.typingContainer);
                    ResponceReadMessage responceReadMessage = (ResponceReadMessage) message;
                    Log.d("responce", "responce on message Readed");
                    //Log.d("TYPING", "listener");
                    /*if (responceReadMessage.getgetFromProfileID() == dialogInfo.getProfileId() && typingMessage.getIsTyping()) {
                        typingContainer.setVisibility(View.VISIBLE);
                        Log.d("TYPING", "visible");
                    } else if (typingMessage.getFromProfileID() == dialogInfo.getProfileId() && !typingMessage.getIsTyping()) {
                        typingContainer.setVisibility(View.GONE);
                        Log.d("TYPING", "not visible");
                    }*/
                }
            });
        }
    }
}
