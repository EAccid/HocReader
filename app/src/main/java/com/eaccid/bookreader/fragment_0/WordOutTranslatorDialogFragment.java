package com.eaccid.bookreader.fragment_0;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.translator.ReaderTranslator;
import com.eaccid.bookreader.translator.TranslatedWord;
import com.eaccid.bookreader.wordgetter.WordFromText;
import com.eaccid.libtranslator.translator.TextTranslation;

public class WordOutTranslatorDialogFragment extends DialogFragment {

    public interface WordTranslationClickListener {
        void onTranslatedWord(TranslatedWord translatedWord);
    }

    public static WordOutTranslatorDialogFragment newInstance(WordFromText wordFromText) {

        WordOutTranslatorDialogFragment f = new WordOutTranslatorDialogFragment();

        // Supply word translation input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("WordFromText", wordFromText);
        f.setArguments(args);
        return f;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        WordFromText wordFromText = (WordFromText) getArguments().getSerializable("WordFromText");
        TextTranslation wordTranslation = ReaderTranslator.translate(wordFromText);

        View v = inflater.inflate(R.layout.translation_dialog_frame, container);
        ImageView imageWordPicture = (ImageView) v.findViewById(R.id.image_word_picture);
        ImageButton imageButtonTranscriptionSpeaker = (ImageButton) v.findViewById(R.id.transcription_speaker);
        TextView word_from_text = (TextView) v.findViewById(R.id.word_from_text);
        TextView text_transcription = (TextView) v.findViewById(R.id.text_transcription);
        ListView listViewTranslations = (ListView) v.findViewById(R.id.list_translations);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
                inflater.getContext(), R.layout.translation_dialog_item, wordTranslation.getTranslates());

        getDialog().setTitle(wordFromText.getText());
        //imageWordPicture
        text_transcription.setText("[ " + wordTranslation.getTranscription() + " ]");
        word_from_text.setText(wordTranslation.getWord());
        //imageButtonTranscriptionSpeaker
        listViewTranslations.setAdapter(listAdapter);

        imageButtonTranscriptionSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO speak
                Toast.makeText(v.getContext(), "...is speaking ", Toast.LENGTH_SHORT).show();
            }
        });

        listViewTranslations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TextView tv = (TextView) view;
                String chosenTranscription = (String) tv.getText();

                TranslatedWord translatedWord = new TranslatedWord();
                translatedWord.setWord(wordTranslation.getWord());
                translatedWord.setTranslation(chosenTranscription);
                translatedWord.setContext(wordFromText.getSentence());

                ((WordTranslationClickListener) getContext()).onTranslatedWord(translatedWord);
            }


        });

        return v;
    }

}