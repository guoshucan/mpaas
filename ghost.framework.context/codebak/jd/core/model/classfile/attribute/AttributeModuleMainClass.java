/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package ghost.framework.context.asm.jd.core.model.classfile.attribute;


import ghost.framework.context.asm.jd.core.model.classfile.constant.ConstantClass;

public class AttributeModuleMainClass implements Attribute {
    protected ConstantClass mainClass;

    public AttributeModuleMainClass(ConstantClass mainClass) {
        this.mainClass = mainClass;
    }

    public ConstantClass getMainClass() {
        return mainClass;
    }
}
