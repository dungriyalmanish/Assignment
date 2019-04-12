package assignment.data.app.assignment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import assignment.data.app.assignment.model.AppConstants;
import assignment.data.app.assignment.model.CardData;
import assignment.data.app.assignment.view.IView;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "Adapter";
    Context mContext;
    List<CardData> cardDataList;
    IView.IMainView iMV;
    boolean bind = true;

    ItemsAdapter(Context context) {
        mContext = context;
        cardDataList = new ArrayList<>();
        iMV = (IView.IMainView) mContext;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        RecyclerView.ViewHolder vh = null;
        switch (i) {
            case 0:
                v = LayoutInflater.from(mContext).inflate(R.layout.image_card, viewGroup, false);
                vh = new ImageViewHolder(v);
                break;
            case 1:
                v = LayoutInflater.from(mContext).inflate(R.layout.comment_card, viewGroup, false);
                vh = new CommentViewHolder(v);
                break;
            case 2:
                v = LayoutInflater.from(mContext).inflate(R.layout.choice_card, viewGroup, false);
                vh = new ChoiceViewHolder(v);
                break;
            default:
                Log.d(TAG, "Not handled case");
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        CardData cd = cardDataList.get(i);
        switch (viewHolder.getItemViewType()) {
            case 0: //image card
                ImageViewHolder i_vh = (ImageViewHolder) viewHolder;
                if (!cd.getData().get(0).isEmpty()) {
                    //Picasso.with(mContext).load(new File(cd.getData().get(0))).into(i_vh.photo);
                    i_vh.photo.setImageDrawable(Drawable.createFromPath(cd.getData().get(0)));
                    i_vh.imageButton.setVisibility(View.VISIBLE);
                } else {
                    i_vh.photo.setImageResource(R.color.blur);
                    i_vh.imageButton.setVisibility(View.GONE);
                }
                i_vh.titleTxt.setText(cd.getTitle());
                break;
            case 1: //comment card
                CommentViewHolder co_vh = (CommentViewHolder) viewHolder;
                co_vh.co_titleTxt.setText(cd.getTitle());
                bind = true;
                co_vh.editText.setText(cd.getData().get(0));
                bind = false;
                break;
            case 2: // choice card
                ChoiceViewHolder ch_vh = (ChoiceViewHolder) viewHolder;
                ch_vh.ch_text.setText(cd.getTitle());
                if (!cd.getData().isEmpty()) {
                    int id = 0;
                    if (ch_vh.radioGroup.getChildCount() == 0) {
                        for (String s : cd.getData()) {
                            RadioButton rb = new RadioButton(mContext);
                            rb.setText(s);
                            rb.setId(id++);
                            ch_vh.radioGroup.addView(rb);
                        }
                    }
                }
                break;
            default:
                Log.d(TAG, "Not handled case");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return cardDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (cardDataList.get(position).getType()) {
            case AppConstants.IMAGE_DATA:
                return 0;    //view type as 0 for image card
            case AppConstants.COMMENT:
                return 1;
            case AppConstants.CHECK_DATA:
                return 2;
            default:
                Log.d(TAG, "Not handled case");
                break;
        }
        return -1;
    }

    public void addCard(CardData cardData) {
        cardDataList.add(cardData);
        notifyDataSetChanged();
    }

    public void clearAll() {
        cardDataList.clear();
        notifyDataSetChanged();
    }

    public void updateImageCard(String imagePath, int pos) {
        CardData cd = cardDataList.get(pos);
        ArrayList<String> arr = new ArrayList<>();
        arr.add(imagePath);
        cd.setData(arr);
        cardDataList.set(pos, cd);
        Log.v(TAG, "image location=" + arr.toString());
        notifyDataSetChanged();
    }

    private void updateComment(boolean check, String text, int pos) {
        CardData cd = cardDataList.get(pos);
        cd.setChecked(check);
        ArrayList<String> arr = new ArrayList<>();
        arr.add(text);
        cd.setData(arr);
        cardDataList.set(pos, cd);
        notifyDataSetChanged();
    }

    private void updateCheckCard(int checkedId, int pos) {
        CardData cd = cardDataList.get(pos);
        cd.setRadioId(checkedId);
        cardDataList.set(pos, cd);
        notifyDataSetChanged();
    }

    public List<CardData> getAllData() {
        return cardDataList;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView photo;
        ImageButton imageButton;
        TextView titleTxt;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id._photo);
            imageButton = itemView.findViewById(R.id._image_del_btn);
            titleTxt = itemView.findViewById(R.id._title_text_image);
            photo.setOnClickListener(this);
            imageButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id._photo:
                    if (imageButton.getVisibility() != View.GONE) {
                        iMV.showImage(cardDataList.get(getAdapterPosition()).getData().get(0));
                    } else {
                        iMV.captureImage(getAdapterPosition());
                    }
                    break;
                case R.id._image_del_btn:
                    updateImageCard("", getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, TextView.OnEditorActionListener {
        TextView co_titleTxt;
        TextInputEditText editText;
        Switch _switch;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            co_titleTxt = itemView.findViewById(R.id._title_text_comment);
            editText = itemView.findViewById(R.id._edit_text);
            _switch = itemView.findViewById(R.id._toggle);
            _switch.setOnCheckedChangeListener(this);
            editText.setOnEditorActionListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            updateComment(isChecked, editText.getText().toString(), getAdapterPosition());
            if (isChecked) {
                editText.setVisibility(View.VISIBLE);
            } else {
                editText.setVisibility(View.GONE);
            }

        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (!bind)
                updateComment(_switch.isChecked(), editText.getText().toString(), getAdapterPosition());
            return true;
        }
    }

    public class ChoiceViewHolder extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener {
        TextView ch_text;
        RadioGroup radioGroup;

        public ChoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            ch_text = itemView.findViewById(R.id._title_text_choice);
            radioGroup = itemView.findViewById(R.id._radio_group);
            radioGroup.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton rb = group.findViewById(checkedId);
            updateCheckCard(checkedId, getAdapterPosition());
            Toast.makeText(mContext, rb.getText(), Toast.LENGTH_SHORT).show();
        }
    }


}
