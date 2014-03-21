package lecture;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import reseauNeurone.ReseauTest;
import reseauNeurone.TraitementTest;

import analyseChiffres.CodeNumberTest;
import analyseChiffres.CodeurTest;
import analyseChiffres.DecodeurTest;

@RunWith(Suite.class)
@SuiteClasses(value={
CodeNumberTest.class,
UtileTest.class,
CommandesTest.class,
CodeurTest.class,
DecodeurTest.class,
TraitementTest.class,
ReseauTest.class,

})

public class AllTests {
	

	
	
}