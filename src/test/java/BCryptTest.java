import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by jooyong on 2015-11-04.
 */
public class BCryptTest {

    @Test
    public void test(){
        String password = "암호";
        String password2= "암호2";

        System.out.println("plan text : "+ password);

        String hashPass = BCrypt.hashpw(password, BCrypt.gensalt(12));
        System.out.println("password hash : " + hashPass);

        assertTrue(BCrypt.checkpw(password, hashPass));
        assertFalse(BCrypt.checkpw(password2, hashPass));
    }

    @Test
    public void salt() {
        for (int i = 0; i < 10; i++) {
            System.out.println(BCrypt.gensalt(15));
        }
    }
}
