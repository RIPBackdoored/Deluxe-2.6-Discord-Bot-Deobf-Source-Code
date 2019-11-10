/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class DoubleMetaphone
implements StringEncoder {
    private static final String VOWELS = "AEIOUY";
    private static final String[] SILENT_START = new String[]{"GN", "KN", "PN", "WR", "PS"};
    private static final String[] L_R_N_M_B_H_F_V_W_SPACE = new String[]{"L", "R", "N", "M", "B", "H", "F", "V", "W", " "};
    private static final String[] ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER = new String[]{"ES", "EP", "EB", "EL", "EY", "IB", "IL", "IN", "IE", "EI", "ER"};
    private static final String[] L_T_K_S_N_M_B_Z = new String[]{"L", "T", "K", "S", "N", "M", "B", "Z"};
    private int maxCodeLen = 4;

    public String doubleMetaphone(String value) {
        return this.doubleMetaphone(value, false);
    }

    public String doubleMetaphone(String value, boolean alternate) {
        String string;
        if ((value = this.cleanInput(value)) == null) {
            return null;
        }
        boolean slavoGermanic = this.isSlavoGermanic(value);
        int index = this.isSilentStart(value) ? 1 : 0;
        DoubleMetaphoneResult result = new DoubleMetaphoneResult(this.getMaxCodeLen());
        block25 : while (!result.isComplete() && index <= value.length() - 1) {
            switch (value.charAt(index)) {
                case 'A': 
                case 'E': 
                case 'I': 
                case 'O': 
                case 'U': 
                case 'Y': {
                    index = this.handleAEIOUY(result, index);
                    continue block25;
                }
                case 'B': {
                    result.append('P');
                    index = this.charAt(value, index + 1) == 'B' ? index + 2 : index + 1;
                    continue block25;
                }
                case '\u00c7': {
                    result.append('S');
                    ++index;
                    continue block25;
                }
                case 'C': {
                    index = this.handleC(value, result, index);
                    continue block25;
                }
                case 'D': {
                    index = this.handleD(value, result, index);
                    continue block25;
                }
                case 'F': {
                    result.append('F');
                    index = this.charAt(value, index + 1) == 'F' ? index + 2 : index + 1;
                    continue block25;
                }
                case 'G': {
                    index = this.handleG(value, result, index, slavoGermanic);
                    continue block25;
                }
                case 'H': {
                    index = this.handleH(value, result, index);
                    continue block25;
                }
                case 'J': {
                    index = this.handleJ(value, result, index, slavoGermanic);
                    continue block25;
                }
                case 'K': {
                    result.append('K');
                    index = this.charAt(value, index + 1) == 'K' ? index + 2 : index + 1;
                    continue block25;
                }
                case 'L': {
                    index = this.handleL(value, result, index);
                    continue block25;
                }
                case 'M': {
                    result.append('M');
                    index = this.conditionM0(value, index) ? index + 2 : index + 1;
                    continue block25;
                }
                case 'N': {
                    result.append('N');
                    index = this.charAt(value, index + 1) == 'N' ? index + 2 : index + 1;
                    continue block25;
                }
                case '\u00d1': {
                    result.append('N');
                    ++index;
                    continue block25;
                }
                case 'P': {
                    index = this.handleP(value, result, index);
                    continue block25;
                }
                case 'Q': {
                    result.append('K');
                    index = this.charAt(value, index + 1) == 'Q' ? index + 2 : index + 1;
                    continue block25;
                }
                case 'R': {
                    index = this.handleR(value, result, index, slavoGermanic);
                    continue block25;
                }
                case 'S': {
                    index = this.handleS(value, result, index, slavoGermanic);
                    continue block25;
                }
                case 'T': {
                    index = this.handleT(value, result, index);
                    continue block25;
                }
                case 'V': {
                    result.append('F');
                    index = this.charAt(value, index + 1) == 'V' ? index + 2 : index + 1;
                    continue block25;
                }
                case 'W': {
                    index = this.handleW(value, result, index);
                    continue block25;
                }
                case 'X': {
                    index = this.handleX(value, result, index);
                    continue block25;
                }
                case 'Z': {
                    index = this.handleZ(value, result, index, slavoGermanic);
                    continue block25;
                }
            }
            ++index;
        }
        if (alternate) {
            string = result.getAlternate();
            return string;
        }
        string = result.getPrimary();
        return string;
    }

    @Override
    public Object encode(Object obj) throws EncoderException {
        if (obj instanceof String) return this.doubleMetaphone((String)obj);
        throw new EncoderException("DoubleMetaphone encode parameter is not of type String");
    }

    @Override
    public String encode(String value) {
        return this.doubleMetaphone(value);
    }

    public boolean isDoubleMetaphoneEqual(String value1, String value2) {
        return this.isDoubleMetaphoneEqual(value1, value2, false);
    }

    public boolean isDoubleMetaphoneEqual(String value1, String value2, boolean alternate) {
        return this.doubleMetaphone(value1, alternate).equals(this.doubleMetaphone(value2, alternate));
    }

    public int getMaxCodeLen() {
        return this.maxCodeLen;
    }

    public void setMaxCodeLen(int maxCodeLen) {
        this.maxCodeLen = maxCodeLen;
    }

    private int handleAEIOUY(DoubleMetaphoneResult result, int index) {
        if (index != 0) return index + 1;
        result.append('A');
        return index + 1;
    }

    private int handleC(String value, DoubleMetaphoneResult result, int index) {
        if (this.conditionC0(value, index)) {
            result.append('K');
            return index += 2;
        }
        if (index == 0) {
            if (DoubleMetaphone.contains(value, index, 6, "CAESAR")) {
                result.append('S');
                return index += 2;
            }
        }
        if (DoubleMetaphone.contains(value, index, 2, "CH")) {
            return this.handleCH(value, result, index);
        }
        if (DoubleMetaphone.contains(value, index, 2, "CZ")) {
            if (!DoubleMetaphone.contains(value, index - 2, 4, "WICZ")) {
                result.append('S', 'X');
                return index += 2;
            }
        }
        if (DoubleMetaphone.contains(value, index + 1, 3, "CIA")) {
            result.append('X');
            return index += 3;
        }
        if (DoubleMetaphone.contains(value, index, 2, "CC")) {
            if (index != 1) return this.handleCC(value, result, index);
            if (this.charAt(value, 0) != 'M') {
                return this.handleCC(value, result, index);
            }
        }
        if (DoubleMetaphone.contains(value, index, 2, "CK", "CG", "CQ")) {
            result.append('K');
            return index += 2;
        }
        if (DoubleMetaphone.contains(value, index, 2, "CI", "CE", "CY")) {
            if (DoubleMetaphone.contains(value, index, 3, "CIO", "CIE", "CIA")) {
                result.append('S', 'X');
                return index += 2;
            } else {
                result.append('S');
            }
            return index += 2;
        }
        result.append('K');
        if (DoubleMetaphone.contains(value, index + 1, 2, " C", " Q", " G")) {
            return index += 3;
        }
        if (DoubleMetaphone.contains(value, index + 1, 1, "C", "K", "Q")) {
            if (!DoubleMetaphone.contains(value, index + 1, 2, "CE", "CI")) {
                return index += 2;
            }
        }
        ++index;
        return index;
    }

    /*
     * Unable to fully structure code
     */
    private int handleCC(String value, DoubleMetaphoneResult result, int index) {
        block2 : {
            if (!DoubleMetaphone.contains(value, index + 2, 1, new String[]{"I", "E", "H"})) break block2;
            if (DoubleMetaphone.contains(value, index + 2, 2, new String[]{"HU"})) break block2;
            if (index == 1 && this.charAt(value, index - 1) == 'A') ** GOTO lbl-1000
            if (DoubleMetaphone.contains(value, index - 1, 5, new String[]{"UCCEE", "UCCES"})) lbl-1000: // 2 sources:
            {
                result.append("KS");
                return index += 3;
            } else {
                result.append('X');
            }
            return index += 3;
        }
        result.append('K');
        index += 2;
        return index;
    }

    private int handleCH(String value, DoubleMetaphoneResult result, int index) {
        if (index > 0) {
            if (DoubleMetaphone.contains(value, index, 4, "CHAE")) {
                result.append('K', 'X');
                return index + 2;
            }
        }
        if (this.conditionCH0(value, index)) {
            result.append('K');
            return index + 2;
        }
        if (this.conditionCH1(value, index)) {
            result.append('K');
            return index + 2;
        }
        if (index <= 0) {
            result.append('X');
            return index + 2;
        }
        if (DoubleMetaphone.contains(value, 0, 2, "MC")) {
            result.append('K');
            return index + 2;
        }
        result.append('X', 'K');
        return index + 2;
    }

    private int handleD(String value, DoubleMetaphoneResult result, int index) {
        if (DoubleMetaphone.contains(value, index, 2, "DG")) {
            if (DoubleMetaphone.contains(value, index + 2, 1, "I", "E", "Y")) {
                result.append('J');
                return index += 3;
            }
            result.append("TK");
            return index += 2;
        }
        if (DoubleMetaphone.contains(value, index, 2, "DT", "DD")) {
            result.append('T');
            return index += 2;
        }
        result.append('T');
        ++index;
        return index;
    }

    /*
     * Unable to fully structure code
     */
    private int handleG(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
        block18 : {
            block17 : {
                if (this.charAt(value, index + 1) == 'H') {
                    return this.handleGH(value, result, index);
                }
                if (this.charAt(value, index + 1) == 'N') {
                    if (index == 1 && this.isVowel(this.charAt(value, 0)) && !slavoGermanic) {
                        result.append("KN", "N");
                        return index += 2;
                    } else if (!DoubleMetaphone.contains(value, index + 2, 2, new String[]{"EY"}) && this.charAt(value, index + 1) != 'Y' && !slavoGermanic) {
                        result.append("N", "KN");
                        return index += 2;
                    } else {
                        result.append("KN");
                    }
                    return index += 2;
                }
                if (DoubleMetaphone.contains(value, index + 1, 2, new String[]{"LI"}) && !slavoGermanic) {
                    result.append("KL", "L");
                    return index += 2;
                }
                if (index == 0 && (this.charAt(value, index + 1) == 'Y' || DoubleMetaphone.contains(value, index + 1, 2, DoubleMetaphone.ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER))) {
                    result.append('K', 'J');
                    return index += 2;
                }
                if (DoubleMetaphone.contains(value, index + 1, 2, new String[]{"ER"}) || this.charAt(value, index + 1) == 'Y') {
                    if (!DoubleMetaphone.contains(value, 0, 6, new String[]{"DANGER", "RANGER", "MANGER"})) {
                        if (!DoubleMetaphone.contains(value, index - 1, 1, new String[]{"E", "I"})) {
                            if (!DoubleMetaphone.contains(value, index - 1, 3, new String[]{"RGY", "OGY"})) {
                                result.append('K', 'J');
                                return index += 2;
                            }
                        }
                    }
                }
                if (DoubleMetaphone.contains(value, index + 1, 1, new String[]{"E", "I", "Y"})) break block17;
                if (!DoubleMetaphone.contains(value, index - 1, 4, new String[]{"AGGI", "OGGI"})) break block18;
            }
            if (DoubleMetaphone.contains(value, 0, 4, new String[]{"VAN ", "VON "})) ** GOTO lbl-1000
            if (DoubleMetaphone.contains(value, 0, 3, new String[]{"SCH"})) ** GOTO lbl-1000
            if (DoubleMetaphone.contains(value, index + 1, 2, new String[]{"ET"})) lbl-1000: // 3 sources:
            {
                result.append('K');
                return index += 2;
            } else if (DoubleMetaphone.contains(value, index + 1, 3, new String[]{"IER"})) {
                result.append('J');
                return index += 2;
            } else {
                result.append('J', 'K');
            }
            return index += 2;
        }
        if (this.charAt(value, index + 1) == 'G') {
            result.append('K');
            return index += 2;
        }
        ++index;
        result.append('K');
        return index;
    }

    /*
     * Unable to fully structure code
     */
    private int handleGH(String value, DoubleMetaphoneResult result, int index) {
        if (index > 0 && !this.isVowel(this.charAt(value, index - 1))) {
            result.append('K');
            return index += 2;
        }
        if (index == 0) {
            if (this.charAt(value, index + 2) == 'I') {
                result.append('J');
                return index += 2;
            } else {
                result.append('K');
            }
            return index += 2;
        }
        if (index > 1) {
            if (DoubleMetaphone.contains(value, index - 2, 1, new String[]{"B", "H", "D"})) return index += 2;
        }
        if (index > 2) {
            if (DoubleMetaphone.contains(value, index - 3, 1, new String[]{"B", "H", "D"})) return index += 2;
        }
        if (index > 3) {
            if (DoubleMetaphone.contains(value, index - 4, 1, new String[]{"B", "H"})) {
                return index += 2;
            }
        }
        if (index <= 2 || this.charAt(value, index - 1) != 'U') ** GOTO lbl-1000
        if (DoubleMetaphone.contains(value, index - 3, 1, new String[]{"C", "G", "L", "R", "T"})) {
            result.append('F');
        } else if (index > 0 && this.charAt(value, index - 1) != 'I') {
            result.append('K');
        }
        index += 2;
        return index;
    }

    private int handleH(String value, DoubleMetaphoneResult result, int index) {
        if ((index == 0 || this.isVowel(this.charAt(value, index - 1))) && this.isVowel(this.charAt(value, index + 1))) {
            result.append('H');
            return index += 2;
        }
        ++index;
        return index;
    }

    /*
     * Unable to fully structure code
     */
    private int handleJ(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
        block12 : {
            block11 : {
                if (DoubleMetaphone.contains(value, index, 4, new String[]{"JOSE"})) break block11;
                if (!DoubleMetaphone.contains(value, 0, 4, new String[]{"SAN "})) break block12;
            }
            if (index == 0 && this.charAt(value, index + 4) == ' ' || value.length() == 4) ** GOTO lbl-1000
            if (DoubleMetaphone.contains(value, 0, 4, new String[]{"SAN "})) lbl-1000: // 2 sources:
            {
                result.append('H');
                return ++index;
            } else {
                result.append('J', 'H');
            }
            return ++index;
        }
        if (index != 0) ** GOTO lbl-1000
        if (!DoubleMetaphone.contains(value, index, 4, new String[]{"JOSE"})) {
            result.append('J', 'A');
        } else if (this.isVowel(this.charAt(value, index - 1)) && !slavoGermanic && (this.charAt(value, index + 1) == 'A' || this.charAt(value, index + 1) == 'O')) {
            result.append('J', 'H');
        } else if (index == value.length() - 1) {
            result.append('J', ' ');
        } else if (!DoubleMetaphone.contains(value, index + 1, 1, DoubleMetaphone.L_T_K_S_N_M_B_Z)) {
            if (!DoubleMetaphone.contains(value, index - 1, 1, new String[]{"S", "K", "L"})) {
                result.append('J');
            }
        }
        if (this.charAt(value, index + 1) == 'J') {
            return index += 2;
        }
        ++index;
        return index;
    }

    private int handleL(String value, DoubleMetaphoneResult result, int index) {
        if (this.charAt(value, index + 1) != 'L') {
            ++index;
            result.append('L');
            return index;
        }
        if (this.conditionL0(value, index)) {
            result.appendPrimary('L');
            return index += 2;
        } else {
            result.append('L');
        }
        return index += 2;
    }

    private int handleP(String value, DoubleMetaphoneResult result, int index) {
        if (this.charAt(value, index + 1) == 'H') {
            result.append('F');
            return index += 2;
        }
        result.append('P');
        return DoubleMetaphone.contains(value, index + 1, 1, "P", "B") ? index + 2 : index + 1;
    }

    /*
     * Unable to fully structure code
     */
    private int handleR(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
        if (index != value.length() - 1 || slavoGermanic) ** GOTO lbl-1000
        if (!DoubleMetaphone.contains(value, index - 2, 2, new String[]{"IE"})) ** GOTO lbl-1000
        if (!DoubleMetaphone.contains(value, index - 4, 2, new String[]{"ME", "MA"})) {
            result.appendAlternate('R');
        } else lbl-1000: // 3 sources:
        {
            result.append('R');
        }
        if (this.charAt(value, index + 1) == 'R') {
            v0 = index + 2;
            return v0;
        }
        v0 = index + 1;
        return v0;
    }

    /*
     * Unable to fully structure code
     */
    private int handleS(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
        block21 : {
            block20 : {
                block19 : {
                    block18 : {
                        block17 : {
                            if (DoubleMetaphone.contains(value, index - 1, 3, new String[]{"ISL", "YSL"})) {
                                return ++index;
                            }
                            if (index == 0) {
                                if (DoubleMetaphone.contains(value, index, 5, new String[]{"SUGAR"})) {
                                    result.append('X', 'S');
                                    return ++index;
                                }
                            }
                            if (DoubleMetaphone.contains(value, index, 2, new String[]{"SH"})) {
                                if (DoubleMetaphone.contains(value, index + 1, 4, new String[]{"HEIM", "HOEK", "HOLM", "HOLZ"})) {
                                    result.append('S');
                                    return index += 2;
                                } else {
                                    result.append('X');
                                }
                                return index += 2;
                            }
                            if (DoubleMetaphone.contains(value, index, 3, new String[]{"SIO", "SIA"})) break block17;
                            if (!DoubleMetaphone.contains(value, index, 4, new String[]{"SIAN"})) break block18;
                        }
                        if (slavoGermanic) {
                            result.append('S');
                            return index += 3;
                        } else {
                            result.append('S', 'X');
                        }
                        return index += 3;
                    }
                    if (index != 0) break block19;
                    if (DoubleMetaphone.contains(value, index + 1, 1, new String[]{"M", "N", "L", "W"})) break block20;
                }
                if (!DoubleMetaphone.contains(value, index + 1, 1, new String[]{"Z"})) break block21;
            }
            result.append('S', 'X');
            return DoubleMetaphone.contains(value, index + 1, 1, new String[]{"Z"}) != false ? index + 2 : index + 1;
        }
        if (DoubleMetaphone.contains(value, index, 2, new String[]{"SC"})) {
            return this.handleSC(value, result, index);
        }
        if (index != value.length() - 1) ** GOTO lbl-1000
        if (DoubleMetaphone.contains(value, index - 2, 2, new String[]{"AI", "OI"})) {
            result.appendAlternate('S');
            return DoubleMetaphone.contains(value, index + 1, 1, new String[]{"S", "Z"}) != false ? index + 2 : index + 1;
        } else lbl-1000: // 2 sources:
        {
            result.append('S');
        }
        return DoubleMetaphone.contains(value, index + 1, 1, new String[]{"S", "Z"}) != false ? index + 2 : index + 1;
    }

    private int handleSC(String value, DoubleMetaphoneResult result, int index) {
        if (this.charAt(value, index + 2) == 'H') {
            if (DoubleMetaphone.contains(value, index + 3, 2, "OO", "ER", "EN", "UY", "ED", "EM")) {
                if (DoubleMetaphone.contains(value, index + 3, 2, "ER", "EN")) {
                    result.append("X", "SK");
                    return index + 3;
                }
                result.append("SK");
                return index + 3;
            }
            if (index == 0 && !this.isVowel(this.charAt(value, 3)) && this.charAt(value, 3) != 'W') {
                result.append('X', 'S');
                return index + 3;
            }
            result.append('X');
            return index + 3;
        }
        if (DoubleMetaphone.contains(value, index + 2, 1, "I", "E", "Y")) {
            result.append('S');
            return index + 3;
        }
        result.append("SK");
        return index + 3;
    }

    /*
     * Unable to fully structure code
     */
    private int handleT(String value, DoubleMetaphoneResult result, int index) {
        block5 : {
            block4 : {
                if (DoubleMetaphone.contains(value, index, 4, new String[]{"TION"})) {
                    result.append('X');
                    return index += 3;
                }
                if (DoubleMetaphone.contains(value, index, 3, new String[]{"TIA", "TCH"})) {
                    result.append('X');
                    return index += 3;
                }
                if (DoubleMetaphone.contains(value, index, 2, new String[]{"TH"})) break block4;
                if (!DoubleMetaphone.contains(value, index, 3, new String[]{"TTH"})) break block5;
            }
            if (DoubleMetaphone.contains(value, index + 2, 2, new String[]{"OM", "AM"})) ** GOTO lbl-1000
            if (DoubleMetaphone.contains(value, 0, 4, new String[]{"VAN ", "VON "})) ** GOTO lbl-1000
            if (DoubleMetaphone.contains(value, 0, 3, new String[]{"SCH"})) lbl-1000: // 3 sources:
            {
                result.append('T');
                return index += 2;
            } else {
                result.append('0', 'T');
            }
            return index += 2;
        }
        result.append('T');
        return DoubleMetaphone.contains(value, index + 1, 1, new String[]{"T", "D"}) != false ? index + 2 : index + 1;
    }

    private int handleW(String value, DoubleMetaphoneResult result, int index) {
        block13 : {
            block12 : {
                block10 : {
                    block11 : {
                        if (DoubleMetaphone.contains(value, index, 2, "WR")) {
                            result.append('R');
                            return index += 2;
                        }
                        if (index != 0) break block10;
                        if (this.isVowel(this.charAt(value, index + 1))) break block11;
                        if (!DoubleMetaphone.contains(value, index, 2, "WH")) break block10;
                    }
                    if (this.isVowel(this.charAt(value, index + 1))) {
                        result.append('A', 'F');
                        return ++index;
                    } else {
                        result.append('A');
                    }
                    return ++index;
                }
                if (index == value.length() - 1 && this.isVowel(this.charAt(value, index - 1))) break block12;
                if (DoubleMetaphone.contains(value, index - 1, 5, "EWSKI", "EWSKY", "OWSKI", "OWSKY")) break block12;
                if (!DoubleMetaphone.contains(value, 0, 3, "SCH")) break block13;
            }
            result.appendAlternate('F');
            return ++index;
        }
        if (DoubleMetaphone.contains(value, index, 4, "WICZ", "WITZ")) {
            result.append("TS", "FX");
            return index += 4;
        }
        ++index;
        return index;
    }

    private int handleX(String value, DoubleMetaphoneResult result, int index) {
        if (index == 0) {
            result.append('S');
            return ++index;
        }
        if (index == value.length() - 1) {
            if (DoubleMetaphone.contains(value, index - 3, 3, "IAU", "EAU")) return DoubleMetaphone.contains(value, index + 1, 1, "C", "X") ? index + 2 : index + 1;
            if (DoubleMetaphone.contains(value, index - 2, 2, "AU", "OU")) return DoubleMetaphone.contains(value, index + 1, 1, "C", "X") ? index + 2 : index + 1;
        }
        result.append("KS");
        return DoubleMetaphone.contains(value, index + 1, 1, "C", "X") ? index + 2 : index + 1;
    }

    private int handleZ(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
        if (this.charAt(value, index + 1) == 'H') {
            result.append('J');
            return index += 2;
        }
        if (DoubleMetaphone.contains(value, index + 1, 2, "ZO", "ZI", "ZA") || slavoGermanic && index > 0 && this.charAt(value, index - 1) != 'T') {
            result.append("S", "TS");
            return this.charAt(value, index + 1) == 'Z' ? index + 2 : index + 1;
        } else {
            result.append('S');
        }
        return this.charAt(value, index + 1) == 'Z' ? index + 2 : index + 1;
    }

    private boolean conditionC0(String value, int index) {
        if (DoubleMetaphone.contains(value, index, 4, "CHIA")) {
            return true;
        }
        if (index <= 1) {
            return false;
        }
        if (this.isVowel(this.charAt(value, index - 2))) {
            return false;
        }
        if (!DoubleMetaphone.contains(value, index - 1, 3, "ACH")) {
            return false;
        }
        char c = this.charAt(value, index + 2);
        if (c != 'I') {
            if (c != 'E') return true;
        }
        if (DoubleMetaphone.contains(value, index - 2, 6, "BACHER", "MACHER")) return true;
        return false;
    }

    private boolean conditionCH0(String value, int index) {
        if (index != 0) {
            return false;
        }
        if (!DoubleMetaphone.contains(value, index + 1, 5, "HARAC", "HARIS")) {
            if (!DoubleMetaphone.contains(value, index + 1, 3, "HOR", "HYM", "HIA", "HEM")) {
                return false;
            }
        }
        if (!DoubleMetaphone.contains(value, 0, 5, "CHORE")) return true;
        return false;
    }

    private boolean conditionCH1(String value, int index) {
        if (DoubleMetaphone.contains(value, 0, 4, "VAN ", "VON ")) return true;
        if (DoubleMetaphone.contains(value, 0, 3, "SCH")) return true;
        if (DoubleMetaphone.contains(value, index - 2, 6, "ORCHES", "ARCHIT", "ORCHID")) return true;
        if (DoubleMetaphone.contains(value, index + 2, 1, "T", "S")) return true;
        if (!DoubleMetaphone.contains(value, index - 1, 1, "A", "O", "U", "E")) {
            if (index != 0) return false;
        }
        if (DoubleMetaphone.contains(value, index + 2, 1, L_R_N_M_B_H_F_V_W_SPACE)) return true;
        if (index + 1 != value.length() - 1) return false;
        return true;
    }

    private boolean conditionL0(String value, int index) {
        if (index == value.length() - 3) {
            if (DoubleMetaphone.contains(value, index - 1, 4, "ILLO", "ILLA", "ALLE")) {
                return true;
            }
        }
        if (!DoubleMetaphone.contains(value, value.length() - 2, 2, "AS", "OS")) {
            if (!DoubleMetaphone.contains(value, value.length() - 1, 1, "A", "O")) return false;
        }
        if (!DoubleMetaphone.contains(value, index - 1, 4, "ALLE")) return false;
        return true;
    }

    private boolean conditionM0(String value, int index) {
        if (this.charAt(value, index + 1) == 'M') {
            return true;
        }
        if (!DoubleMetaphone.contains(value, index - 1, 3, "UMB")) return false;
        if (index + 1 == value.length() - 1) return true;
        if (!DoubleMetaphone.contains(value, index + 2, 2, "ER")) return false;
        return true;
    }

    private boolean isSlavoGermanic(String value) {
        if (value.indexOf(87) > -1) return true;
        if (value.indexOf(75) > -1) return true;
        if (value.indexOf("CZ") > -1) return true;
        if (value.indexOf("WITZ") > -1) return true;
        return false;
    }

    private boolean isVowel(char ch) {
        if (VOWELS.indexOf(ch) == -1) return false;
        return true;
    }

    private boolean isSilentStart(String value) {
        boolean result = false;
        String[] arr$ = SILENT_START;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            String element = arr$[i$];
            if (value.startsWith(element)) {
                return true;
            }
            ++i$;
        }
        return result;
    }

    private String cleanInput(String input) {
        if (input == null) {
            return null;
        }
        if ((input = input.trim()).length() != 0) return input.toUpperCase(Locale.ENGLISH);
        return null;
    }

    protected char charAt(String value, int index) {
        if (index < 0) return '\u0000';
        if (index < value.length()) return value.charAt(index);
        return '\u0000';
    }

    protected static boolean contains(String value, int start, int length, String ... criteria) {
        boolean result = false;
        if (start < 0) return result;
        if (start + length > value.length()) return result;
        String target = value.substring(start, start + length);
        String[] arr$ = criteria;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            String element = arr$[i$];
            if (target.equals(element)) {
                return true;
            }
            ++i$;
        }
        return result;
    }

    public class DoubleMetaphoneResult {
        private final StringBuilder primary;
        private final StringBuilder alternate;
        private final int maxLength;

        public DoubleMetaphoneResult(int maxLength) {
            this.primary = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
            this.alternate = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
            this.maxLength = maxLength;
        }

        public void append(char value) {
            this.appendPrimary(value);
            this.appendAlternate(value);
        }

        public void append(char primary, char alternate) {
            this.appendPrimary(primary);
            this.appendAlternate(alternate);
        }

        public void appendPrimary(char value) {
            if (this.primary.length() >= this.maxLength) return;
            this.primary.append(value);
        }

        public void appendAlternate(char value) {
            if (this.alternate.length() >= this.maxLength) return;
            this.alternate.append(value);
        }

        public void append(String value) {
            this.appendPrimary(value);
            this.appendAlternate(value);
        }

        public void append(String primary, String alternate) {
            this.appendPrimary(primary);
            this.appendAlternate(alternate);
        }

        public void appendPrimary(String value) {
            int addChars = this.maxLength - this.primary.length();
            if (value.length() <= addChars) {
                this.primary.append(value);
                return;
            }
            this.primary.append(value.substring(0, addChars));
        }

        public void appendAlternate(String value) {
            int addChars = this.maxLength - this.alternate.length();
            if (value.length() <= addChars) {
                this.alternate.append(value);
                return;
            }
            this.alternate.append(value.substring(0, addChars));
        }

        public String getPrimary() {
            return this.primary.toString();
        }

        public String getAlternate() {
            return this.alternate.toString();
        }

        public boolean isComplete() {
            if (this.primary.length() < this.maxLength) return false;
            if (this.alternate.length() < this.maxLength) return false;
            return true;
        }
    }

}

