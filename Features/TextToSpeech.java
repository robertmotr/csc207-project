package Features;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;


/**
 * Contains the methods relating to providing text to speech
 *
 * Reference from:
 * https://stackoverflow.com/questions/34724104/text-to-speech-in-java-using-freetts
 * https://softwarepulse.co.uk/tutorials/javafx-text-to-speech/
 * @author Mico
 */
public class TextToSpeech {

    /**
     * Reads out a given string in a Male voice
     * Converts "&" symbols to "and" before being spoken
     *
     * @param words words to be spoken
     */

    public static void speak(String words) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        voice.allocate();
        voice.setRate(140);
        voice.setPitch(140);
        voice.setVolume(5);
        voice.speak(words.replaceAll("&", "and"));
    }
}
