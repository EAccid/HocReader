package com.eaccid.bookreader.fragment_0;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.Size;
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

import java.io.InputStream;
import java.net.URL;

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
        new ImageWordPictureLoader(imageWordPicture).execute(wordTranslation.getPicUrl());
        text_transcription.setText("[ " + wordTranslation.getTranscription() + " ]");

        //imageButtonTranscriptionSpeaker
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new PlayerWordSoundLoader(mediaPlayer).execute(wordTranslation.getSoundUrl());
//        imageButtonTranscriptionSpeaker.setImageResource(R.drawable.ic_hearing_blue_24px);
        imageButtonTranscriptionSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(), R.layout.translation_dialog_item, wordTranslation.getTranslates());
        listViewTranslations.setAdapter(adapter);

        listViewTranslations.setOnItemClickListener(new OnItemTranslationClickListener(translatedWord));

        return v;

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


    private class ImageWordPictureLoader extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        ImageWordPictureLoader(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(@Size(min = 1) String... urls) {
            String url = urls[0];
            Bitmap wordImage = null;
            try{
                InputStream is = new URL(url).openStream();
                wordImage = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return wordImage;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }

    }

    private class PlayerWordSoundLoader extends AsyncTask<String, Void, Boolean> {

        MediaPlayer mediaPlayer;

        PlayerWordSoundLoader(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
        }

        @Override
        protected Boolean doInBackground(@Size(min = 1) String... urls) {
            String url = urls[0];
            Boolean prepared;
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                prepared = true;
            } catch (Exception e) {
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }
    }
}