package com.eaccid.bookreader.wordview;

import android.app.DialogFragment;
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
import android.widget.Toast;
import com.eaccid.bookreader.R;
import com.eaccid.translator.lingualeo.translator.WordTranslation;

public class TranslationDialogFragment extends DialogFragment {


    static TranslationDialogFragment newInstance(WordTranslation wordTranslation) {
        TranslationDialogFragment f = new TranslationDialogFragment();

        // Supply word translation input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("wordTranslation", wordTranslation);
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

        System.out.println("onCreateView TranslationDialogFragment");

        WordTranslation wordTranslation = (WordTranslation) getArguments().getSerializable("wordTranslation");

        getDialog().setTitle("Translations title");
        View v = inflater.inflate(R.layout.fragment_word_translation, container);

        ImageView imageWordPicture = (ImageView) v.findViewById(R.id.image_word_picture);
        ImageButton imageButtonTranscriptionSpeaker = (ImageButton) v.findViewById(R.id.transcription_speaker);
        ListView listViewTranslations = (ListView) v.findViewById(R.id.list_translations);

//        imageWordPicture
//        imageButtonTranscriptionSpeaker

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
                getContext(), R.layout.translation_item, wordTranslation.getTranslates()  );
        listViewTranslations.setAdapter(listAdapter);

        imageButtonTranscriptionSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "...is speaking ", Toast.LENGTH_SHORT).show();
            }
        });

        listViewTranslations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "chose  translation: ", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }


}
