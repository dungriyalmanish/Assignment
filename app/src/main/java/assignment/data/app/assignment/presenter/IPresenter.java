package assignment.data.app.assignment.presenter;

import java.util.List;

import assignment.data.app.assignment.model.CardData;

public interface IPresenter {
    interface IMainPresenter {
        void readData();

        void updateData(CardData cardData);

        void saveData();

        void logCurrentData(List<CardData> allData);

        void moveToNextAct();
    }
}
