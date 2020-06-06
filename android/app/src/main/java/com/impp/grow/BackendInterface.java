package com.impp.grow;

public final class BackendInterface {

    //JNI functions
    public static native String getTitle(final String sheetContent);
    public static native int getRandomQuestion(final String category);
    public static native String[] getQuestionDetails(final int questionNumber, boolean jeopardyMode);
    public static native String[] getMCDistractors(int questionNumber, int answerAmount, boolean jeopardyMode);
    public static native String[] getCategories();
    public static native int importfromGoogleSheet(final String sheet);
    public static native boolean getDatabaseStatus();
    public static native boolean checkGoogleSheetURL(final String url);

    public static void loadLibrary(){
        System.loadLibrary("rust");
    }

}
