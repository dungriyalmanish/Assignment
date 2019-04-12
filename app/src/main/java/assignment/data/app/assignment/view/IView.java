package assignment.data.app.assignment.view;

import assignment.data.app.assignment.model.CardData;

public interface IView {
    interface IMainView {

      /*  void updateChoiceCard(CardData cardData);

        void updateImageCard(CardData cardData);

        void updateCommentCard(CardData cardData);*/

        void updateCard(CardData cardData);
        void captureImage(int pos);

        void showSavedLogs();

        void showImage(String loc);
    }
}
