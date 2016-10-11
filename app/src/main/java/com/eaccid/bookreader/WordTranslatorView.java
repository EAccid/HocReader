package com.eaccid.bookreader;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
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

import com.eaccid.bookreader.wordfinder.WordFromText;
import com.eaccid.translator.lingualeo.translator.WordTranslation;
import com.eaccid.translator.translator.TextTranslation;

public class WordTranslatorView {

    private Context context;

    public WordTranslatorView(Context context) {
        this.context = context;
    }

    public void showTranslationView(WordFromText wordFromText) {
        TranslationDialogFragment translationDialogFragment = TranslationDialogFragment.newInstance(wordFromText);
        translationDialogFragment.show(((Activity) context).getFragmentManager(), "translationDialogFragmentDel");
    }

    public static class TranslationDialogFragment extends DialogFragment {

        public static TranslationDialogFragment newInstance(WordFromText wordFromText) {

            TranslationDialogFragment f = new TranslationDialogFragment();

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


            View v = inflater.inflate(R.layout.fragment_word_translation, container);
            ImageView imageWordPicture = (ImageView) v.findViewById(R.id.image_word_picture);
            ImageButton imageButtonTranscriptionSpeaker = (ImageButton) v.findViewById(R.id.transcription_speaker);
            TextView word_from_text = (TextView) v.findViewById(R.id.word_from_text);
            TextView text_transcription = (TextView) v.findViewById(R.id.text_transcription);
            ListView listViewTranslations = (ListView) v.findViewById(R.id.list_translations);
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
                    inflater.getContext(), R.layout.translation_item, wordTranslation.getTranslates());


            getDialog().setTitle(wordTranslation.getWord());
            //imageWordPicture
            text_transcription.setText("[ " + wordTranslation.getTranscription() + " ]");
            word_from_text.setText(wordFromText.getText());
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

                    TranslatedDictionaryWord translatedDictionaryWord = new TranslatedDictionaryWord();
                    translatedDictionaryWord.setWord(wordTranslation.getWord());
                    translatedDictionaryWord.setTranslation(chosenTranscription);
                    translatedDictionaryWord.setContext(wordFromText.getSentence());

                    boolean succeed = ReaderDictionary.addTranslatedWord(translatedDictionaryWord);

                    //TODO msg
                    if (!succeed) {
                        Toast.makeText(v.getContext(), " ... FAIL ... ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "word has added", Toast.LENGTH_SHORT).show();
                    }

                    //TODO add to inner dictionary

                }
            });

            return v;
        }

    }

}
