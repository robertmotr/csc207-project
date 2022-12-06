package Features;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;


/**
 * Reference from:
 * https://stackoverflow.com/questions/34724104/text-to-speech-in-java-using-freetts
 * https://softwarepulse.co.uk/tutorials/javafx-text-to-speech/
 */
public class TextToSpeech {
    VoiceManager freeVM;

    public static void speak(String words) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        voice.allocate();
        voice.setRate(110);
        voice.setPitch(100);
        voice.setVolume(5);
        voice.speak(words);
    }
}
