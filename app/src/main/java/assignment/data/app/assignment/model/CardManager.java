package assignment.data.app.assignment.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import assignment.data.app.assignment.presenter.IPresenter;

public class CardManager extends Handler {
    private static final String TAG = "CardManager";
    Context mContext;
    IPresenter.IMainPresenter iMP;
    JsonParser jsonParser;

    public <T> CardManager(T presenter, Context context) {
        mContext = context;
        if (presenter instanceof IPresenter.IMainPresenter) {
            iMP = (IPresenter.IMainPresenter) presenter;
        }
        jsonParser = new JsonParser(mContext, this);

    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case AppConstants.READ_DATA:
                jsonParser.readJsonData();
                break;
            case AppConstants.WRITE_DATA:
                break;
            case AppConstants.SEND_DATA:
                iMP.updateData((CardData) msg.obj);
                break;
            case AppConstants.LOG_ALL_DATA:
                saveUserData((List<CardData>) msg.obj);
                break;
        }
    }

    //Just to save the current status of the data to device
    private void saveUserData(List<CardData> cardDataList) {
        StringBuilder data = new StringBuilder("");
        for (CardData cd : cardDataList) {
            data.append("\n\nLOG for ID: " + cd.getId());
            data.append("\ntype=" + cd.getType());
            switch (cd.getType()) {
                case AppConstants.IMAGE_DATA:
                    data.append("\nImage Title = " + cd.getTitle());
                    data.append("\nImage Location = " + cd.getData());
                    break;
                case AppConstants.COMMENT:
                    data.append("\nComment Title = " + cd.getTitle());
                    data.append("\nComment Enabled = " + cd.isChecked());
                    data.append("\nComment = " + cd.getData());
                    break;
                case AppConstants.CHECK_DATA:
                    data.append("\nChoice Title = " + cd.getTitle());
                    data.append("\nChoice Option = " + cd.getData());
                    if (cd.getRadioId() != -1) {
                        data.append("\nChoice Selected = " + cd.getData().get(cd.getRadioId()));
                    }else{
                        data.append("\nChoice Selected = Not Selected");
                    }
                    break;
                default:
                    break;
            }
        }
        Log.v(TAG, "Data = " + data.toString());
        try {
            FileOutputStream fos = mContext.openFileOutput(String.valueOf(Calendar.getInstance().getTimeInMillis()), Context.MODE_PRIVATE);
            fos.write(data.toString().getBytes());
            Log.v(TAG, fos.toString());
            fos.close();
            iMP.moveToNextAct();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
