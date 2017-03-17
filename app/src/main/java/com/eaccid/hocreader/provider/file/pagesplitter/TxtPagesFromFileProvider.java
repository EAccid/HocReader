package com.eaccid.hocreader.provider.file.pagesplitter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class TxtPagesFromFileProvider implements FileToPagesReader<String> {

    private final CharactersDefinerForFullScreenTextView parameters;

    public TxtPagesFromFileProvider(CharactersDefinerForFullScreenTextView parameters) {
        this.parameters = parameters;
    }

    @Override
    public Observable<List<Page<String>>> getPagesObservable(BaseFile baseFile) {
        return Observable.create(
                new Observable.OnSubscribe<List<Page<String>>>() {
                    @Override
                    public void call(Subscriber<? super List<Page<String>>> sub) {
                        List<Page<String>> pages = new FileReader().read(baseFile);
                        sub.onNext(pages);
                        sub.onCompleted();
                    }
                }
        );
    }

    private class FileReader {
        private StringBuilder currentPage = new StringBuilder();
        private StringBuilder currentStringIsAboutToWriteOnPage = new StringBuilder();
        private int pageNumber = 0;
        private boolean isEndOfCurrentPage;
        private int emptyLinesNumberOnCurrentPage = 0;
        List<Page<String>> read(BaseFile baseFile) {
            List<Page<String>> pages = new ArrayList<>();
            BufferedReaderHandler br = new BufferedReaderHandler(baseFile);
            br.openBufferedReader();
            while (!br.isEof()) {
                readCurrentLineFromBufferedReader(br);
                writeCurrentLineToCurrentPage();
                if (isEndOfCurrentPage || br.isEof()) {
                    pages.add(getSubTxtPage());
                    updateFieldsForNewPage();
                }
            }
            br.closeBufferedReader();
            return pages;
        }

        private TxtPage getSubTxtPage() {
            TxtPage txtPage = new TxtPage();
            txtPage.setPageData(currentPage.toString());
            txtPage.setPageNumber(++pageNumber);
            return txtPage;
        }

        private void readCurrentLineFromBufferedReader(BufferedReaderHandler br) {
            String currentLineFromBufferedReader = br.readLineBufferedReader();
            boolean isEmptyLineFromBuffer = (currentLineFromBufferedReader = currentLineFromBufferedReader.trim()).isEmpty();
            currentStringIsAboutToWriteOnPage.append(currentLineFromBufferedReader);
            if (isEmptyLineFromBuffer && !br.isEof()) {
                readCurrentLineFromBufferedReader(br);
            } else {
                currentStringIsAboutToWriteOnPage.append("\n\n");
                emptyLinesNumberOnCurrentPage++;
            }
        }

        private void writeCurrentLineToCurrentPage() {
            int charNumberToAppend = getCharsNumberToAppend();
            appendCurrentLineToCurrentPage(charNumberToAppend);
            if (isEndOfCurrentPage) {
                trimPageAfterLastSpaceChar();
            } else {
                prepareCurrentStringToWriteOnPage(charNumberToAppend);
            }
        }

        private int getCharsNumberToAppend() {
            int currentCharsNumberOnCurrentPage = currentStringIsAboutToWriteOnPage.length();
            int remainedCharsToWriteOnCurrentPage = Math.max(parameters.getCharactersOnPage() - currentPage.length() -
                    (parameters.getCharactersOnLine() * emptyLinesNumberOnCurrentPage), 0
            );
            isEndOfCurrentPage = (remainedCharsToWriteOnCurrentPage == 0);
            return Math.min(currentCharsNumberOnCurrentPage, remainedCharsToWriteOnCurrentPage);
        }

        private void appendCurrentLineToCurrentPage(int endCharOfCurrentString) {
            String toAppend = currentStringIsAboutToWriteOnPage.substring(0, endCharOfCurrentString);
            currentPage.append(toAppend);
        }

        private void trimPageAfterLastSpaceChar() {
            String charToNextPage = currentPage.substring(currentPage.lastIndexOf(" "), currentPage.length());
            currentPage.delete(currentPage.length() - charToNextPage.length(), currentPage.length());
            currentStringIsAboutToWriteOnPage.insert(0, charToNextPage);
        }

        private void updateFieldsForNewPage() {
            currentPage = new StringBuilder();
            emptyLinesNumberOnCurrentPage = 0;
        }

        private void prepareCurrentStringToWriteOnPage(int charsNumberToAppend) {
            String textBringToNextPage = currentStringIsAboutToWriteOnPage.substring(charsNumberToAppend,
                    currentStringIsAboutToWriteOnPage.length());
            currentStringIsAboutToWriteOnPage = new StringBuilder(textBringToNextPage);
        }

    }

}