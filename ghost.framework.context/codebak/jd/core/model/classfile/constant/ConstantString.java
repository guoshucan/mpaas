/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package ghost.framework.context.asm.jd.core.model.classfile.constant;

public class ConstantString extends Constant {
    protected int stringIndex;

    public ConstantString(int stringIndex) {
        super(CONSTANT_String);
        this.stringIndex = stringIndex;
    }

    public int getStringIndex() {
        return stringIndex;
    }
}
