package de.ebamberg.streamline.nlp.streamlineText;

import java.io.IOException;
import java.util.Arrays;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class SentenceTest {

	private static String exampleText = "Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29. Mr. Vinken is\r\n"
			+ "chairman of Elsevier N.V., the Dutch publishing group. Rudolph Agnew, 55 years\r\n"
			+ "old and former chairman of Consolidated Gold Fields PLC, was named a director of this\r\n"
			+ "British industrial conglomerate.\r\n" + "		";

	public void run() throws IOException {
		try (var sentenceModel = SentenceTest.class.getResourceAsStream("/opennlp-en-ud-ewt-sentence-1.0-1.9.3.bin")) {
			var model = new SentenceModel(sentenceModel);
			try (var tokens = SentenceTest.class.getResourceAsStream("/opennlp-en-ud-ewt-tokens-1.0-1.9.3.bin")) {
				var tokenModel = new TokenizerModel(tokens);

				try (var partOfSpeech = SentenceTest.class.getResourceAsStream("/opennlp-en-ud-ewt-pos-1.0-1.9.3.bin")) {
					var posModel = new POSModel(partOfSpeech);

					var sentenceDetector = new SentenceDetectorME(model);
					var tokenizer = new TokenizerME(tokenModel);
					var tagger = new POSTaggerME(posModel);

					var sentences = sentenceDetector.sentDetect(exampleText);

					Arrays.stream(sentences).forEach(s -> {
						var tokenizedSequence = tokenizer.tokenize(s);
						var tags=tagger.tag(tokenizedSequence);
							
					});
				}
			}

		}
	}

	public void tokeninze(String input) {

	}

	public static void main(String... args) throws IOException {
		new SentenceTest().run();

	}

}
