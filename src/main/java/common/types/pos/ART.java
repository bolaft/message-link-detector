

/* First created by JCasGen Fri Oct 25 12:10:34 CEST 2013 */
package common.types.pos;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import common.types.POS;


/** 
 * Updated by JCasGen Fri Oct 25 12:41:37 CEST 2013
 * XML source: /media/ext4/teaching/5a.ATAL.M2.Dev-log-et-projet/nlp-software-development/workspace/resourceManagement/src/main/resources/resourceManagement/types/CommonTS.xml
 * @generated */
public class ART extends POS {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(ART.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected ART() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public ART(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public ART(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public ART(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
}

    