package com.eaccid.bookreader.fragment_0;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
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
        args.putSerializable("wordFromText", wordFromText);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TextTranslation wordTranslation;
        WordFromText wordFromText = (WordFromText) getArguments().getSerializable("wordFromText");
        getDialog().setTitle(wordFromText.getText());

        wordTranslation = ReaderTranslator.translate(wordFromText);

        TranslatedWord translatedWord = new TranslatedWord();
        translatedWord.setWordBaseForm(wordTranslation.getWord());
        translatedWord.setWordFromContext(wordFromText.getText());
        translatedWord.setContext(wordFromText.getSentence());

        //get view

        View v = inflater.inflate(R.layout.translation_dialog_frame, container);
        ImageView imageWordPicture = (ImageView) v.findViewById(R.id.image_word_picture);
        ImageButton imageButtonTranscriptionSpeaker = (ImageButton) v.findViewById(R.id.transcription_speaker);
        TextView base_word_from = (TextView) v.findViewById(R.id.base_word_from);
        TextView text_transcription = (TextView) v.findViewById(R.id.text_transcription);
        ListView listViewTranslations = (ListView) v.findViewById(R.id.list_translations);

        //set data to view
        base_word_from.setText(wordTranslation.getWord());
        //imageWordPicture
        text_transcription.setText("[ " + wordTranslation.getTranscription() + " ]");
        //imageButtonTranscriptionSpeaker

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(), R.layout.translation_dialog_item, wordTranslation.getTranslates());
        listViewTranslations.setAdapter(adapter);

        imageButtonTranscriptionSpeaker.setOnClickListener(new OnImageClickListener());
        listViewTranslations.setOnItemClickListener(new OnItemTranslationClickListener(translatedWord));

        return v;

    }

    private class OnImageClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //TODO speak
            Toast.makeText(v.getContext(), "...is speaking ", Toast.LENGTH_SHORT).show();
        }
    }

    private class OnItemTranslationClickListener implements AdapterView.OnItemClickListener {

        TranslatedWord translatedWord;
        OnItemTranslationClickListener(TranslatedWord translatedWord) {
            this.translatedWord = translatedWord;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView tv = (TextView) view;
            String chosenTranslation = (String) tv.getText();

            translatedWord.setTranslation(chosenTranslation);

            ((WordTranslationClickListener) getContext()).onTranslatedWord(translatedWord);

            dismiss();
        }
    }

}