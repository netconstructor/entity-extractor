package gov.ornl.csed.csiir.sava.stucco;

import gov.ornl.csed.csiir.sava.stucco.models.Sentence;
import gov.ornl.csed.csiir.sava.stucco.models.Sentences;
import gov.ornl.csed.csiir.sava.stucco.models.Word;

import java.util.Arrays;

import org.junit.Test;

public class EntityExtractorTest {
	private static String testSentence = "Unspecified vulnerability in the update check in Vanilla Forums before 2.0.18.8 has unspecified impact and remote attack vectors, related to 'object injection'.";
	private static String[] expectedWords = {"Unspecified", "vulnerability", "in", "the", "update", "check", "in", "Vanilla", "Forums", "before", "2", ".", "0", ".", "18", ".", "8", "has", "unspecified", "impact", "and", "remote", "attack", "vectors", ",", "related", "to", "'", "object", "injection", "'", "."};
	private static String[] expectedPOS = {"JJ", "NN", "IN", "DT", "NN", "NN", "IN", "NNP", "``", "IN", "CD", ".", "CD", ".", "CD", ".", "CD", "VBZ", "JJ", "NN", "CC", "JJ", "NN", "NNS", ",", "VBN", "TO", "''", "NN", "NN", "''", "."};
	private static String[] expectedIOB = {"O", "O", "O", "O", "O", "O", "O", "B", "I", "B", "I", "O", "O", "B", "I", "B", "O", "O", "O", "O", "O", "B", "B", "O", "O", "O", "O", "O", "O", "O", "O", "O"};
	private static String[] expectedLabels = {"O", "O", "O", "O", "O", "O", "O", "sw.product", "sw.product", "sw.version", "sw.version", "O", "sw.product", "vuln.relevant_term", "sw.version", "O", "O", "O", "O", "O", "O", "vuln.relevant_term", "vuln.relevant_term", "O", "O", "O", "O", "O", "O", "O", "O", "O"};
	//TODO: need a way of rounding or checking digits of precision
	private static double[] expectedScores = {0.30546253374051335, 0.30787655281990906, 0.30798526884094246, 0.308093586900048, 0.3049197878745381, 0.30155984363891264, 0.30629129971391356, 0.29752649136139936, 0.2957501736680968, 0.2821412536772508, 0.24381068473918738, 0.30560809455709564, 0.24958042919073953, 0.2787354049171418, 0.2928137316747827, 0.24744695476526354, 0.2614525378318422, 0.30560324989198695, 0.30879074734502326, 0.3074321989276122, 0.306095238622678, 0.30545779231819714, 0.29941290399523435, 0.3042641855645965, 0.3079819915443962, 0.30653420775161616, 0.30825220319632923, 0.30634989779602145, 0.30570318643411803, 0.2966469666701804, 0.30506202814870637, 0.3002562131257219};
	
	@Test
	public void testGetSentences() throws Exception {
		String testSentences = testSentence.concat(" Multiple SQL injection vulnerabilities in todooforum.php in Todoo Forum 2.0 allow remote attackers to execute arbitrary SQL commands via the (1) id_post or (2) pg parameter.");
		EntityExtractor extractor = new EntityExtractor();
		String[] sentences = extractor.getSentences(testSentences);
		String[] expectedSentences = {testSentence, "Multiple SQL injection vulnerabilities in todooforum.php in Todoo Forum 2.0 allow remote attackers to execute arbitrary SQL commands via the (1) id_post or (2) pg parameter."};
		
		assert(Arrays.equals(expectedSentences, sentences));
	}

	@Test
	public void testTokenize() throws Exception {
		EntityExtractor extractor = new EntityExtractor();
		Sentence sentence = extractor.tokenize(testSentence);
		
		Sentence expectedSentence = new Sentence();
		for (int i=0; i<expectedWords.length; i++) {
			Word word = new Word(expectedWords[i]);
			expectedSentence.addWord(word);
		}
		
		assert(expectedSentence.equals(sentence));
	}

	@Test
	public void testGetPOS() throws Exception {
		EntityExtractor extractor = new EntityExtractor();
		Sentence sentence = extractor.tokenize(testSentence);
		sentence = extractor.getPOS(sentence);
		
		Sentence expectedSentence = new Sentence();
		for (int i=0; i<expectedWords.length; i++) {
			Word word = new Word(expectedWords[i]);
			word.setPos(expectedPOS[i]);
			expectedSentence.addWord(word);
		}
		
		assert(expectedSentence.equals(sentence));
	}

	@Test
	public void testGetIOB() throws Exception {
		EntityExtractor extractor = new EntityExtractor();
		Sentence sentence = extractor.tokenize(testSentence);
		sentence = extractor.getPOS(sentence);
		sentence = extractor.getIOB(sentence);
		
		Sentence expectedSentence = new Sentence();
		for (int i=0; i<expectedWords.length; i++) {
			Word word = new Word(expectedWords[i]);
			word.setPos(expectedPOS[i]);
			word.setIob(expectedIOB[i]);
			expectedSentence.addWord(word);
		}
		
		assert(expectedSentence.equals(sentence));
	}

	@Test
	public void testGetLabels() throws Exception {
		EntityExtractor extractor = new EntityExtractor();
		Sentence sentence = extractor.tokenize(testSentence);
		sentence = extractor.getPOS(sentence);
		sentence = extractor.getIOB(sentence);
		sentence = extractor.getLabels(sentence);
		
		Sentence expectedSentence = new Sentence();
		for (int i=0; i<expectedWords.length; i++) {
			Word word = new Word(expectedWords[i]);
			word.setPos(expectedPOS[i]);
			word.setIob(expectedIOB[i]);
			word.setDomainLabel(expectedLabels[i]);
			word.setDomainScore(expectedScores[i]);
			expectedSentence.addWord(word);
		}
		
		assert(expectedSentence.equals(sentence));
	}
	
	@Test
	public void testGetAnnotatedText() throws Exception {
		EntityExtractor extractor = new EntityExtractor();
		Sentences sentences = extractor.getAnnotatedText(testSentence);
		
		Sentence expectedSentence = new Sentence();
		for (int i=0; i<expectedWords.length; i++) {
			Word word = new Word(expectedWords[i]);
			word.setPos(expectedPOS[i]);
			word.setIob(expectedIOB[i]);
			word.setDomainLabel(expectedLabels[i]);
			word.setDomainScore(expectedScores[i]);
			expectedSentence.addWord(word);
		}
		Sentences expectedSentences = new Sentences();
		expectedSentences.addSentence(expectedSentence);
		
		assert(expectedSentences.equals(sentences));
	}

	@Test
	public void testGetAnnotatedTextAsJson() throws Exception {
		EntityExtractor extractor = new EntityExtractor();
		String labelString = extractor.getAnnotatedTextAsJson(testSentence);
		
		Sentence expectedSentence = new Sentence();
		for (int i=0; i<expectedWords.length; i++) {
			Word word = new Word(expectedWords[i]);
			word.setPos(expectedPOS[i]);
			word.setIob(expectedIOB[i]);
			word.setDomainLabel(expectedLabels[i]);
			word.setDomainScore(expectedScores[i]);
			expectedSentence.addWord(word);
		}
		Sentences expectedSentences = new Sentences();
		expectedSentences.addSentence(expectedSentence);
		String expectedLabelString = expectedSentences.toJSON();
		
		assert(expectedLabelString.equals(labelString));
	}

}
