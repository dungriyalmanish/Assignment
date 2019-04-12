package assignment.data.app.assignment.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.List;

import assignment.data.app.assignment.model.AppConstants;
import assignment.data.app.assignment.model.CardData;
import assignment.data.app.assignment.model.CardManager;
import assignment.data.app.assignment.view.IView;

public class MainConnector implements IPresenter.IMainPresenter {
    private static final String TAG = "MainConnector";
    IView.IMainView iMV;
    Context mContext;
    Handler mHandler;

    public MainConnector(Context context) {
        mContext = context;
        iMV = (IView.IMainView) mContext;
        mHandler = new CardManager(this, mContext);
    }

    @Override
    public void readData() {
        mHandler.sendEmptyMessage(AppConstants.READ_DATA);
    }

    @Override
    public void updateData(CardData cardData) {
        iMV.updateCard(cardData);
    }

    @Override
    public void saveData() {

    }

    @Override
    public void logCurrentData(List<CardData> allData) {
        Message.obtain(mHandler, AppConstants.LOG_ALL_DATA, allData).sendToTarget();
    }

    @Override
    public void moveToNextAct() {
        iMV.showSavedLogs();
    }
}
