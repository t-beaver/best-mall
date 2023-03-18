package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.alibaba.fastjson.asm.ClassWriter;
import com.alibaba.fastjson.asm.FieldWriter;
import com.alibaba.fastjson.asm.Label;
import com.alibaba.fastjson.asm.MethodVisitor;
import com.alibaba.fastjson.asm.MethodWriter;
import com.alibaba.fastjson.asm.Opcodes;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.SymbolTable;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.util.JSUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ASMDeserializerFactory implements Opcodes {
    static final String DefaultJSONParser = ASMUtils.type(DefaultJSONParser.class);
    static final String JSONLexerBase = ASMUtils.type(JSONLexerBase.class);
    public final ASMClassLoader classLoader;
    protected final AtomicLong seed = new AtomicLong();

    public ASMDeserializerFactory(ClassLoader classLoader2) {
        this.classLoader = classLoader2 instanceof ASMClassLoader ? (ASMClassLoader) classLoader2 : new ASMClassLoader(classLoader2);
    }

    public ObjectDeserializer createJavaBeanDeserializer(ParserConfig parserConfig, JavaBeanInfo javaBeanInfo) throws Exception {
        String str;
        Class<?> cls = javaBeanInfo.clazz;
        if (!cls.isPrimitive()) {
            String str2 = "FastjsonASMDeserializer_" + this.seed.incrementAndGet() + "_" + cls.getSimpleName();
            Package packageR = ASMDeserializerFactory.class.getPackage();
            if (packageR != null) {
                String name = packageR.getName();
                String str3 = name.replace(Operators.DOT, '/') + "/" + str2;
                str = name + Operators.DOT_STR + str2;
                str2 = str3;
            } else {
                str = str2;
            }
            ClassWriter classWriter = new ClassWriter();
            classWriter.visit(49, 33, str2, ASMUtils.type(JavaBeanDeserializer.class), (String[]) null);
            _init(classWriter, new Context(str2, parserConfig, javaBeanInfo, 3));
            _createInstance(classWriter, new Context(str2, parserConfig, javaBeanInfo, 3));
            _deserialze(classWriter, new Context(str2, parserConfig, javaBeanInfo, 5));
            _deserialzeArrayMapping(classWriter, new Context(str2, parserConfig, javaBeanInfo, 4));
            byte[] byteArray = classWriter.toByteArray();
            return (ObjectDeserializer) this.classLoader.defineClassPublic(str, byteArray, 0, byteArray.length).getConstructor(new Class[]{ParserConfig.class, JavaBeanInfo.class}).newInstance(new Object[]{parserConfig, javaBeanInfo});
        }
        throw new IllegalArgumentException("not support type :" + cls.getName());
    }

    private void _setFlag(MethodVisitor methodVisitor, Context context, int i) {
        String str = "_asm_flag_" + (i / 32);
        methodVisitor.visitVarInsn(21, context.var(str));
        methodVisitor.visitLdcInsn(Integer.valueOf(1 << i));
        methodVisitor.visitInsn(128);
        methodVisitor.visitVarInsn(54, context.var(str));
    }

    private void _isFlag(MethodVisitor methodVisitor, Context context, int i, Label label) {
        methodVisitor.visitVarInsn(21, context.var("_asm_flag_" + (i / 32)));
        methodVisitor.visitLdcInsn(Integer.valueOf(1 << i));
        methodVisitor.visitInsn(126);
        methodVisitor.visitJumpInsn(Opcodes.IFEQ, label);
    }

    private void _deserialzeArrayMapping(ClassWriter classWriter, Context context) {
        FieldInfo[] fieldInfoArr;
        int i;
        int i2;
        int i3;
        Context context2 = context;
        StringBuilder sb = new StringBuilder();
        sb.append("(L");
        String str = DefaultJSONParser;
        sb.append(str);
        sb.append(";Ljava/lang/reflect/Type;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
        MethodWriter methodWriter = new MethodWriter(classWriter, 1, "deserialzeArrayMapping", sb.toString(), (String) null, (String[]) null);
        defineVarLexer(context2, methodWriter);
        methodWriter.visitVarInsn(25, context2.var("lexer"));
        methodWriter.visitVarInsn(25, 1);
        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "getSymbolTable", "()" + ASMUtils.desc((Class<?>) SymbolTable.class));
        String str2 = JSONLexerBase;
        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str2, "scanTypeName", Operators.BRACKET_START_STR + ASMUtils.desc((Class<?>) SymbolTable.class) + ")Ljava/lang/String;");
        methodWriter.visitVarInsn(58, context2.var("typeName"));
        Label label = new Label();
        methodWriter.visitVarInsn(25, context2.var("typeName"));
        methodWriter.visitJumpInsn(Opcodes.IFNULL, label);
        methodWriter.visitVarInsn(25, 1);
        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "getConfig", "()" + ASMUtils.desc((Class<?>) ParserConfig.class));
        methodWriter.visitVarInsn(25, 0);
        methodWriter.visitFieldInsn(180, ASMUtils.type(JavaBeanDeserializer.class), "beanInfo", ASMUtils.desc((Class<?>) JavaBeanInfo.class));
        methodWriter.visitVarInsn(25, context2.var("typeName"));
        String type = ASMUtils.type(JavaBeanDeserializer.class);
        methodWriter.visitMethodInsn(Opcodes.INVOKESTATIC, type, "getSeeAlso", Operators.BRACKET_START_STR + ASMUtils.desc((Class<?>) ParserConfig.class) + ASMUtils.desc((Class<?>) JavaBeanInfo.class) + "Ljava/lang/String;)" + ASMUtils.desc((Class<?>) JavaBeanDeserializer.class));
        methodWriter.visitVarInsn(58, context2.var("userTypeDeser"));
        methodWriter.visitVarInsn(25, context2.var("userTypeDeser"));
        methodWriter.visitTypeInsn(Opcodes.INSTANCEOF, ASMUtils.type(JavaBeanDeserializer.class));
        methodWriter.visitJumpInsn(Opcodes.IFEQ, label);
        methodWriter.visitVarInsn(25, context2.var("userTypeDeser"));
        methodWriter.visitVarInsn(25, 1);
        methodWriter.visitVarInsn(25, 2);
        methodWriter.visitVarInsn(25, 3);
        methodWriter.visitVarInsn(25, 4);
        String type2 = ASMUtils.type(JavaBeanDeserializer.class);
        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type2, "deserialzeArrayMapping", "(L" + str + ";Ljava/lang/reflect/Type;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
        methodWriter.visitInsn(176);
        methodWriter.visitLabel(label);
        _createInstance(context2, (MethodVisitor) methodWriter);
        FieldInfo[] fieldInfoArr2 = context.beanInfo.sortedFields;
        int length = fieldInfoArr2.length;
        int i4 = 0;
        while (i4 < length) {
            boolean z = i4 == length + -1;
            int i5 = z ? 93 : 44;
            FieldInfo fieldInfo = fieldInfoArr2[i4];
            Class<?> cls = fieldInfo.fieldClass;
            Type type3 = fieldInfo.fieldType;
            int i6 = length;
            if (cls == Byte.TYPE || cls == Short.TYPE || cls == Integer.TYPE) {
                i2 = i4;
                fieldInfoArr = fieldInfoArr2;
                i = i6;
                methodWriter.visitVarInsn(25, context2.var("lexer"));
                methodWriter.visitVarInsn(16, i5);
                methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanInt", "(C)I");
                methodWriter.visitVarInsn(54, context2.var_asm(fieldInfo));
            } else {
                FieldInfo[] fieldInfoArr3 = fieldInfoArr2;
                boolean z2 = z;
                String str3 = "(I)V";
                if (cls == Byte.class) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    String str4 = JSONLexerBase;
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str4, "scanInt", "(C)I");
                    methodWriter.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                    Label label2 = new Label();
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitFieldInsn(180, str4, "matchStat", "I");
                    methodWriter.visitLdcInsn(5);
                    methodWriter.visitJumpInsn(Opcodes.IF_ICMPNE, label2);
                    methodWriter.visitInsn(1);
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                    methodWriter.visitLabel(label2);
                } else if (cls == Short.class) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    String str5 = JSONLexerBase;
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str5, "scanInt", "(C)I");
                    methodWriter.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                    Label label3 = new Label();
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitFieldInsn(180, str5, "matchStat", "I");
                    methodWriter.visitLdcInsn(5);
                    methodWriter.visitJumpInsn(Opcodes.IF_ICMPNE, label3);
                    methodWriter.visitInsn(1);
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                    methodWriter.visitLabel(label3);
                } else if (cls == Integer.class) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    String str6 = JSONLexerBase;
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str6, "scanInt", "(C)I");
                    methodWriter.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                    Label label4 = new Label();
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitFieldInsn(180, str6, "matchStat", "I");
                    methodWriter.visitLdcInsn(5);
                    methodWriter.visitJumpInsn(Opcodes.IF_ICMPNE, label4);
                    methodWriter.visitInsn(1);
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                    methodWriter.visitLabel(label4);
                } else if (cls == Long.TYPE) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanLong", "(C)J");
                    methodWriter.visitVarInsn(55, context2.var_asm(fieldInfo, 2));
                } else if (cls == Long.class) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    String str7 = JSONLexerBase;
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str7, "scanLong", "(C)J");
                    methodWriter.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                    Label label5 = new Label();
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitFieldInsn(180, str7, "matchStat", "I");
                    methodWriter.visitLdcInsn(5);
                    methodWriter.visitJumpInsn(Opcodes.IF_ICMPNE, label5);
                    methodWriter.visitInsn(1);
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                    methodWriter.visitLabel(label5);
                } else if (cls == Boolean.TYPE) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanBoolean", "(C)Z");
                    methodWriter.visitVarInsn(54, context2.var_asm(fieldInfo));
                } else if (cls == Float.TYPE) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanFloat", "(C)F");
                    methodWriter.visitVarInsn(56, context2.var_asm(fieldInfo));
                } else if (cls == Float.class) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    String str8 = JSONLexerBase;
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str8, "scanFloat", "(C)F");
                    methodWriter.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                    Label label6 = new Label();
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitFieldInsn(180, str8, "matchStat", "I");
                    methodWriter.visitLdcInsn(5);
                    methodWriter.visitJumpInsn(Opcodes.IF_ICMPNE, label6);
                    methodWriter.visitInsn(1);
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                    methodWriter.visitLabel(label6);
                } else if (cls == Double.TYPE) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanDouble", "(C)D");
                    methodWriter.visitVarInsn(57, context2.var_asm(fieldInfo, 2));
                } else if (cls == Double.class) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    String str9 = JSONLexerBase;
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str9, "scanDouble", "(C)D");
                    methodWriter.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                    Label label7 = new Label();
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitFieldInsn(180, str9, "matchStat", "I");
                    methodWriter.visitLdcInsn(5);
                    methodWriter.visitJumpInsn(Opcodes.IF_ICMPNE, label7);
                    methodWriter.visitInsn(1);
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                    methodWriter.visitLabel(label7);
                } else if (cls == Character.TYPE) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanString", "(C)Ljava/lang/String;");
                    methodWriter.visitInsn(3);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "charAt", "(I)C");
                    methodWriter.visitVarInsn(54, context2.var_asm(fieldInfo));
                } else if (cls == String.class) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanString", "(C)Ljava/lang/String;");
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                } else if (cls == BigDecimal.class) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanDecimal", "(C)Ljava/math/BigDecimal;");
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                } else if (cls == Date.class) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanDate", "(C)Ljava/util/Date;");
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                } else if (cls == UUID.class) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanUUID", "(C)Ljava/util/UUID;");
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                } else if (cls.isEnum()) {
                    Label label8 = new Label();
                    Label label9 = new Label();
                    Label label10 = new Label();
                    Label label11 = new Label();
                    int i7 = i4;
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    String str10 = JSONLexerBase;
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str10, "getCurrent", "()C");
                    methodWriter.visitInsn(89);
                    methodWriter.visitVarInsn(54, context2.var("ch"));
                    methodWriter.visitLdcInsn(110);
                    methodWriter.visitJumpInsn(159, label11);
                    methodWriter.visitVarInsn(21, context2.var("ch"));
                    methodWriter.visitLdcInsn(34);
                    methodWriter.visitJumpInsn(Opcodes.IF_ICMPNE, label8);
                    methodWriter.visitLabel(label11);
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(cls)));
                    methodWriter.visitVarInsn(25, 1);
                    String str11 = DefaultJSONParser;
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str11, "getSymbolTable", "()" + ASMUtils.desc((Class<?>) SymbolTable.class));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str10, "scanEnum", "(Ljava/lang/Class;" + ASMUtils.desc((Class<?>) SymbolTable.class) + "C)Ljava/lang/Enum;");
                    methodWriter.visitJumpInsn(Opcodes.GOTO, label10);
                    methodWriter.visitLabel(label8);
                    methodWriter.visitVarInsn(21, context2.var("ch"));
                    methodWriter.visitLdcInsn(48);
                    methodWriter.visitJumpInsn(161, label9);
                    methodWriter.visitVarInsn(21, context2.var("ch"));
                    methodWriter.visitLdcInsn(57);
                    methodWriter.visitJumpInsn(Opcodes.IF_ICMPGT, label9);
                    _getFieldDeser(context2, methodWriter, fieldInfo);
                    methodWriter.visitTypeInsn(192, ASMUtils.type(EnumDeserializer.class));
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str10, "scanInt", "(C)I");
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(EnumDeserializer.class), "valueOf", "(I)Ljava/lang/Enum;");
                    methodWriter.visitJumpInsn(Opcodes.GOTO, label10);
                    methodWriter.visitLabel(label9);
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    String type4 = ASMUtils.type(JavaBeanDeserializer.class);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type4, "scanEnum", "(L" + str10 + ";C)Ljava/lang/Enum;");
                    methodWriter.visitLabel(label10);
                    methodWriter.visitTypeInsn(192, ASMUtils.type(cls));
                    methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                    i = i6;
                    fieldInfoArr = fieldInfoArr3;
                    i2 = i7;
                } else {
                    int i8 = i4;
                    if (Collection.class.isAssignableFrom(cls)) {
                        Class<?> collectionItemClass = TypeUtils.getCollectionItemClass(type3);
                        if (collectionItemClass == String.class) {
                            if (cls == List.class || cls == Collections.class || cls == ArrayList.class) {
                                methodWriter.visitTypeInsn(Opcodes.NEW, ASMUtils.type(ArrayList.class));
                                methodWriter.visitInsn(89);
                                methodWriter.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(ArrayList.class), "<init>", "()V");
                            } else {
                                methodWriter.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(cls)));
                                methodWriter.visitMethodInsn(Opcodes.INVOKESTATIC, ASMUtils.type(TypeUtils.class), "createCollection", "(Ljava/lang/Class;)Ljava/util/Collection;");
                            }
                            methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                            methodWriter.visitVarInsn(25, context2.var("lexer"));
                            methodWriter.visitVarInsn(25, context2.var_asm(fieldInfo));
                            methodWriter.visitVarInsn(16, i5);
                            String str12 = JSONLexerBase;
                            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str12, "scanStringArray", "(Ljava/util/Collection;C)V");
                            Label label12 = new Label();
                            methodWriter.visitVarInsn(25, context2.var("lexer"));
                            methodWriter.visitFieldInsn(180, str12, "matchStat", "I");
                            methodWriter.visitLdcInsn(5);
                            methodWriter.visitJumpInsn(Opcodes.IF_ICMPNE, label12);
                            methodWriter.visitInsn(1);
                            methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                            methodWriter.visitLabel(label12);
                            i3 = i8;
                        } else {
                            Label label13 = new Label();
                            methodWriter.visitVarInsn(25, context2.var("lexer"));
                            String str13 = JSONLexerBase;
                            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str13, BindingXConstants.KEY_TOKEN, "()I");
                            methodWriter.visitVarInsn(54, context2.var(BindingXConstants.KEY_TOKEN));
                            methodWriter.visitVarInsn(21, context2.var(BindingXConstants.KEY_TOKEN));
                            int i9 = i8 == 0 ? 14 : 16;
                            methodWriter.visitLdcInsn(Integer.valueOf(i9));
                            methodWriter.visitJumpInsn(159, label13);
                            methodWriter.visitVarInsn(25, 1);
                            methodWriter.visitLdcInsn(Integer.valueOf(i9));
                            String str14 = DefaultJSONParser;
                            String str15 = str3;
                            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str14, "throwException", str15);
                            methodWriter.visitLabel(label13);
                            Label label14 = new Label();
                            Label label15 = new Label();
                            methodWriter.visitVarInsn(25, context2.var("lexer"));
                            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str13, "getCurrent", "()C");
                            methodWriter.visitVarInsn(16, 91);
                            methodWriter.visitJumpInsn(Opcodes.IF_ICMPNE, label14);
                            methodWriter.visitVarInsn(25, context2.var("lexer"));
                            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str13, "next", "()C");
                            methodWriter.visitInsn(87);
                            methodWriter.visitVarInsn(25, context2.var("lexer"));
                            methodWriter.visitLdcInsn(14);
                            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str13, "setToken", str15);
                            methodWriter.visitJumpInsn(Opcodes.GOTO, label15);
                            methodWriter.visitLabel(label14);
                            methodWriter.visitVarInsn(25, context2.var("lexer"));
                            methodWriter.visitLdcInsn(14);
                            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str13, "nextToken", str15);
                            methodWriter.visitLabel(label15);
                            i3 = i8;
                            _newCollection(methodWriter, cls, i3, false);
                            methodWriter.visitInsn(89);
                            methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                            _getCollectionFieldItemDeser(context2, methodWriter, fieldInfo, collectionItemClass);
                            methodWriter.visitVarInsn(25, 1);
                            methodWriter.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(collectionItemClass)));
                            methodWriter.visitVarInsn(25, 3);
                            String type5 = ASMUtils.type(JavaBeanDeserializer.class);
                            methodWriter.visitMethodInsn(Opcodes.INVOKESTATIC, type5, "parseArray", "(Ljava/util/Collection;" + ASMUtils.desc((Class<?>) ObjectDeserializer.class) + "L" + str14 + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)V");
                        }
                        i2 = i3;
                        i = i6;
                        fieldInfoArr = fieldInfoArr3;
                    } else {
                        String str16 = str3;
                        int i10 = i8;
                        if (cls.isArray()) {
                            methodWriter.visitVarInsn(25, context2.var("lexer"));
                            methodWriter.visitLdcInsn(14);
                            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "nextToken", str16);
                            methodWriter.visitVarInsn(25, 1);
                            methodWriter.visitVarInsn(25, 0);
                            methodWriter.visitLdcInsn(Integer.valueOf(i10));
                            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(JavaBeanDeserializer.class), "getFieldType", "(I)Ljava/lang/reflect/Type;");
                            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, DefaultJSONParser, "parseObject", "(Ljava/lang/reflect/Type;)Ljava/lang/Object;");
                            methodWriter.visitTypeInsn(192, ASMUtils.type(cls));
                            methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                            i2 = i10;
                            i = i6;
                            fieldInfoArr = fieldInfoArr3;
                        } else {
                            Label label16 = new Label();
                            Label label17 = new Label();
                            if (cls == Date.class) {
                                methodWriter.visitVarInsn(25, context2.var("lexer"));
                                String str17 = JSONLexerBase;
                                methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str17, "getCurrent", "()C");
                                methodWriter.visitLdcInsn(49);
                                methodWriter.visitJumpInsn(Opcodes.IF_ICMPNE, label16);
                                methodWriter.visitTypeInsn(Opcodes.NEW, ASMUtils.type(Date.class));
                                methodWriter.visitInsn(89);
                                methodWriter.visitVarInsn(25, context2.var("lexer"));
                                methodWriter.visitVarInsn(16, i5);
                                methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str17, "scanLong", "(C)J");
                                methodWriter.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(Date.class), "<init>", "(J)V");
                                methodWriter.visitVarInsn(58, context2.var_asm(fieldInfo));
                                methodWriter.visitJumpInsn(Opcodes.GOTO, label17);
                            }
                            methodWriter.visitLabel(label16);
                            _quickNextToken(context2, methodWriter, 14);
                            i2 = i10;
                            i = i6;
                            fieldInfoArr = fieldInfoArr3;
                            _deserObject(context, methodWriter, fieldInfo, cls, i2);
                            methodWriter.visitVarInsn(25, context2.var("lexer"));
                            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, BindingXConstants.KEY_TOKEN, "()I");
                            methodWriter.visitLdcInsn(15);
                            methodWriter.visitJumpInsn(159, label17);
                            methodWriter.visitVarInsn(25, 0);
                            methodWriter.visitVarInsn(25, context2.var("lexer"));
                            if (!z2) {
                                methodWriter.visitLdcInsn(16);
                            } else {
                                methodWriter.visitLdcInsn(15);
                            }
                            String type6 = ASMUtils.type(JavaBeanDeserializer.class);
                            methodWriter.visitMethodInsn(Opcodes.INVOKESPECIAL, type6, "check", Operators.BRACKET_START_STR + ASMUtils.desc((Class<?>) JSONLexer.class) + "I)V");
                            methodWriter.visitLabel(label17);
                        }
                    }
                }
                i2 = i4;
                i = i6;
                fieldInfoArr = fieldInfoArr3;
            }
            i4 = i2 + 1;
            length = i;
            fieldInfoArr2 = fieldInfoArr;
        }
        _batchSet(context2, methodWriter, false);
        Label label18 = new Label();
        Label label19 = new Label();
        Label label20 = new Label();
        Label label21 = new Label();
        methodWriter.visitVarInsn(25, context2.var("lexer"));
        String str18 = JSONLexerBase;
        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str18, "getCurrent", "()C");
        methodWriter.visitInsn(89);
        methodWriter.visitVarInsn(54, context2.var("ch"));
        methodWriter.visitVarInsn(16, 44);
        methodWriter.visitJumpInsn(Opcodes.IF_ICMPNE, label19);
        methodWriter.visitVarInsn(25, context2.var("lexer"));
        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str18, "next", "()C");
        methodWriter.visitInsn(87);
        methodWriter.visitVarInsn(25, context2.var("lexer"));
        methodWriter.visitLdcInsn(16);
        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str18, "setToken", "(I)V");
        methodWriter.visitJumpInsn(Opcodes.GOTO, label21);
        methodWriter.visitLabel(label19);
        methodWriter.visitVarInsn(21, context2.var("ch"));
        methodWriter.visitVarInsn(16, 93);
        methodWriter.visitJumpInsn(Opcodes.IF_ICMPNE, label20);
        methodWriter.visitVarInsn(25, context2.var("lexer"));
        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str18, "next", "()C");
        methodWriter.visitInsn(87);
        methodWriter.visitVarInsn(25, context2.var("lexer"));
        methodWriter.visitLdcInsn(15);
        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str18, "setToken", "(I)V");
        methodWriter.visitJumpInsn(Opcodes.GOTO, label21);
        methodWriter.visitLabel(label20);
        methodWriter.visitVarInsn(21, context2.var("ch"));
        methodWriter.visitVarInsn(16, 26);
        methodWriter.visitJumpInsn(Opcodes.IF_ICMPNE, label18);
        methodWriter.visitVarInsn(25, context2.var("lexer"));
        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str18, "next", "()C");
        methodWriter.visitInsn(87);
        methodWriter.visitVarInsn(25, context2.var("lexer"));
        methodWriter.visitLdcInsn(20);
        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str18, "setToken", "(I)V");
        methodWriter.visitJumpInsn(Opcodes.GOTO, label21);
        methodWriter.visitLabel(label18);
        methodWriter.visitVarInsn(25, context2.var("lexer"));
        methodWriter.visitLdcInsn(16);
        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str18, "nextToken", "(I)V");
        methodWriter.visitLabel(label21);
        methodWriter.visitVarInsn(25, context2.var("instance"));
        methodWriter.visitInsn(176);
        methodWriter.visitMaxs(5, context.variantIndex);
        methodWriter.visitEnd();
    }

    /* JADX WARNING: Removed duplicated region for block: B:129:0x0ad2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void _deserialze(com.alibaba.fastjson.asm.ClassWriter r33, com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory.Context r34) {
        /*
            r32 = this;
            r8 = r32
            r9 = r34
            com.alibaba.fastjson.util.FieldInfo[] r0 = r34.fieldInfoList
            int r0 = r0.length
            if (r0 != 0) goto L_0x000c
            return
        L_0x000c:
            com.alibaba.fastjson.util.FieldInfo[] r0 = r34.fieldInfoList
            int r1 = r0.length
            r10 = 0
            r2 = 0
        L_0x0013:
            if (r2 >= r1) goto L_0x003d
            r3 = r0[r2]
            java.lang.Class<?> r4 = r3.fieldClass
            java.lang.reflect.Type r3 = r3.fieldType
            java.lang.Class r5 = java.lang.Character.TYPE
            if (r4 != r5) goto L_0x0020
            return
        L_0x0020:
            java.lang.Class<java.util.Collection> r5 = java.util.Collection.class
            boolean r4 = r5.isAssignableFrom(r4)
            if (r4 == 0) goto L_0x003a
            boolean r4 = r3 instanceof java.lang.reflect.ParameterizedType
            if (r4 == 0) goto L_0x0039
            java.lang.reflect.ParameterizedType r3 = (java.lang.reflect.ParameterizedType) r3
            java.lang.reflect.Type[] r3 = r3.getActualTypeArguments()
            r3 = r3[r10]
            boolean r3 = r3 instanceof java.lang.Class
            if (r3 == 0) goto L_0x0039
            goto L_0x003a
        L_0x0039:
            return
        L_0x003a:
            int r2 = r2 + 1
            goto L_0x0013
        L_0x003d:
            com.alibaba.fastjson.util.JavaBeanInfo r0 = r34.beanInfo
            com.alibaba.fastjson.util.FieldInfo[] r1 = r0.sortedFields
            com.alibaba.fastjson.util.FieldInfo[] unused = r9.fieldInfoList = r1
            com.alibaba.fastjson.asm.MethodWriter r7 = new com.alibaba.fastjson.asm.MethodWriter
            r13 = 1
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r6 = "(L"
            r1.append(r6)
            java.lang.String r2 = DefaultJSONParser
            r1.append(r2)
            java.lang.String r3 = ";Ljava/lang/reflect/Type;Ljava/lang/Object;I)Ljava/lang/Object;"
            r1.append(r3)
            java.lang.String r15 = r1.toString()
            r16 = 0
            r17 = 0
            java.lang.String r14 = "deserialze"
            r11 = r7
            r12 = r33
            r11.<init>(r12, r13, r14, r15, r16, r17)
            com.alibaba.fastjson.asm.Label r11 = new com.alibaba.fastjson.asm.Label
            r11.<init>()
            com.alibaba.fastjson.asm.Label r12 = new com.alibaba.fastjson.asm.Label
            r12.<init>()
            com.alibaba.fastjson.asm.Label r13 = new com.alibaba.fastjson.asm.Label
            r13.<init>()
            com.alibaba.fastjson.asm.Label r14 = new com.alibaba.fastjson.asm.Label
            r14.<init>()
            r8.defineVarLexer(r9, r7)
            com.alibaba.fastjson.asm.Label r1 = new com.alibaba.fastjson.asm.Label
            r1.<init>()
            java.lang.String r15 = "lexer"
            int r3 = r9.var(r15)
            r5 = 25
            r7.visitVarInsn(r5, r3)
            java.lang.String r3 = JSONLexerBase
            r4 = 182(0xb6, float:2.55E-43)
            java.lang.String r10 = "token"
            java.lang.String r5 = "()I"
            r7.visitMethodInsn(r4, r3, r10, r5)
            r5 = 14
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r7.visitLdcInsn(r5)
            r10 = 160(0xa0, float:2.24E-43)
            r7.visitJumpInsn(r10, r1)
            int r0 = r0.parserFeatures
            com.alibaba.fastjson.parser.Feature r5 = com.alibaba.fastjson.parser.Feature.SupportArrayToBean
            int r5 = r5.mask
            r0 = r0 & r5
            r5 = 21
            r10 = 4
            if (r0 != 0) goto L_0x00de
            int r0 = r9.var(r15)
            r4 = 25
            r7.visitVarInsn(r4, r0)
            r7.visitVarInsn(r5, r10)
            com.alibaba.fastjson.parser.Feature r0 = com.alibaba.fastjson.parser.Feature.SupportArrayToBean
            int r0 = r0.mask
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r7.visitLdcInsn(r0)
            java.lang.String r0 = "isEnabled"
            java.lang.String r4 = "(II)Z"
            r5 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r5, r3, r0, r4)
            r0 = 153(0x99, float:2.14E-43)
            r7.visitJumpInsn(r0, r1)
        L_0x00de:
            r0 = 0
            r4 = 25
            r7.visitVarInsn(r4, r0)
            r5 = 1
            r7.visitVarInsn(r4, r5)
            r0 = 2
            r7.visitVarInsn(r4, r0)
            r0 = 3
            r7.visitVarInsn(r4, r0)
            r7.visitInsn(r5)
            r4 = 183(0xb7, float:2.56E-43)
            java.lang.String r10 = r34.className
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r6)
            r0.append(r2)
            java.lang.String r5 = ";Ljava/lang/reflect/Type;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            java.lang.String r5 = "deserialzeArrayMapping"
            r7.visitMethodInsn(r4, r10, r5, r0)
            r0 = 176(0xb0, float:2.47E-43)
            r7.visitInsn(r0)
            r7.visitLabel(r1)
            int r0 = r9.var(r15)
            r1 = 25
            r7.visitVarInsn(r1, r0)
            com.alibaba.fastjson.parser.Feature r0 = com.alibaba.fastjson.parser.Feature.SortFeidFastMatch
            int r0 = r0.mask
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r7.visitLdcInsn(r0)
            java.lang.String r0 = "isEnabled"
            java.lang.String r1 = "(I)Z"
            r4 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r4, r3, r0, r1)
            com.alibaba.fastjson.asm.Label r0 = new com.alibaba.fastjson.asm.Label
            r0.<init>()
            r1 = 154(0x9a, float:2.16E-43)
            r7.visitJumpInsn(r1, r0)
            r1 = 200(0xc8, float:2.8E-43)
            r7.visitJumpInsn(r1, r12)
            r7.visitLabel(r0)
            int r0 = r9.var(r15)
            r1 = 25
            r7.visitVarInsn(r1, r0)
            java.lang.Class r0 = r34.clazz
            java.lang.String r0 = r0.getName()
            r7.visitLdcInsn(r0)
            java.lang.String r0 = "scanType"
            java.lang.String r1 = "(Ljava/lang/String;)I"
            r4 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r4, r3, r0, r1)
            r0 = -1
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r7.visitLdcInsn(r0)
            com.alibaba.fastjson.asm.Label r0 = new com.alibaba.fastjson.asm.Label
            r0.<init>()
            r1 = 160(0xa0, float:2.24E-43)
            r7.visitJumpInsn(r1, r0)
            r1 = 200(0xc8, float:2.8E-43)
            r7.visitJumpInsn(r1, r12)
            r7.visitLabel(r0)
            r0 = 1
            r1 = 25
            r7.visitVarInsn(r1, r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "()"
            r0.append(r1)
            java.lang.Class<com.alibaba.fastjson.parser.ParseContext> r1 = com.alibaba.fastjson.parser.ParseContext.class
            java.lang.String r1 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r1)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "getContext"
            r4 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r4, r2, r1, r0)
            java.lang.String r0 = "mark_context"
            int r0 = r9.var(r0)
            r10 = 58
            r7.visitVarInsn(r10, r0)
            r0 = 3
            r7.visitInsn(r0)
            java.lang.String r0 = "matchedCount"
            int r0 = r9.var(r0)
            r5 = 54
            r7.visitVarInsn(r5, r0)
            r8._createInstance((com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory.Context) r9, (com.alibaba.fastjson.asm.MethodVisitor) r7)
            r0 = 1
            r1 = 25
            r7.visitVarInsn(r1, r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "()"
            r0.append(r1)
            java.lang.Class<com.alibaba.fastjson.parser.ParseContext> r1 = com.alibaba.fastjson.parser.ParseContext.class
            java.lang.String r1 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r1)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "getContext"
            r4 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r4, r2, r1, r0)
            java.lang.String r0 = "context"
            int r0 = r9.var(r0)
            r7.visitVarInsn(r10, r0)
            r0 = 1
            r1 = 25
            r7.visitVarInsn(r1, r0)
            java.lang.String r0 = "context"
            int r0 = r9.var(r0)
            r7.visitVarInsn(r1, r0)
            java.lang.String r0 = "instance"
            int r0 = r9.var(r0)
            r7.visitVarInsn(r1, r0)
            r0 = 3
            r7.visitVarInsn(r1, r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "("
            r0.append(r1)
            java.lang.Class<com.alibaba.fastjson.parser.ParseContext> r1 = com.alibaba.fastjson.parser.ParseContext.class
            java.lang.String r1 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r1)
            r0.append(r1)
            java.lang.String r1 = "Ljava/lang/Object;Ljava/lang/Object;)"
            r0.append(r1)
            java.lang.Class<com.alibaba.fastjson.parser.ParseContext> r1 = com.alibaba.fastjson.parser.ParseContext.class
            java.lang.String r1 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r1)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "setContext"
            r4 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r4, r2, r1, r0)
            java.lang.String r0 = "childContext"
            int r0 = r9.var(r0)
            r7.visitVarInsn(r10, r0)
            int r0 = r9.var(r15)
            r1 = 25
            r7.visitVarInsn(r1, r0)
            r4 = 180(0xb4, float:2.52E-43)
            java.lang.String r2 = "matchStat"
            java.lang.String r1 = "I"
            r7.visitFieldInsn(r4, r3, r2, r1)
            r0 = 4
            java.lang.Integer r3 = java.lang.Integer.valueOf(r0)
            r7.visitLdcInsn(r3)
            com.alibaba.fastjson.asm.Label r0 = new com.alibaba.fastjson.asm.Label
            r0.<init>()
            r3 = 160(0xa0, float:2.24E-43)
            r7.visitJumpInsn(r3, r0)
            r3 = 200(0xc8, float:2.8E-43)
            r7.visitJumpInsn(r3, r13)
            r7.visitLabel(r0)
            r0 = 3
            r7.visitInsn(r0)
            int r3 = r9.var(r2)
            r7.visitIntInsn(r5, r3)
            com.alibaba.fastjson.util.FieldInfo[] r3 = r34.fieldInfoList
            int r3 = r3.length
            r4 = 0
        L_0x027c:
            if (r4 >= r3) goto L_0x02a1
            r7.visitInsn(r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r10 = "_asm_flag_"
            r0.append(r10)
            int r10 = r4 / 32
            r0.append(r10)
            java.lang.String r0 = r0.toString()
            int r0 = r9.var(r0)
            r7.visitVarInsn(r5, r0)
            int r4 = r4 + 32
            r0 = 3
            r10 = 58
            goto L_0x027c
        L_0x02a1:
            int r0 = r9.var(r15)
            r4 = 25
            r7.visitVarInsn(r4, r0)
            com.alibaba.fastjson.parser.Feature r0 = com.alibaba.fastjson.parser.Feature.InitStringFieldAsEmpty
            int r0 = r0.mask
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r7.visitLdcInsn(r0)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r4 = "isEnabled"
            java.lang.String r10 = "(I)Z"
            r5 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r5, r0, r4, r10)
            java.lang.String r0 = "initStringFieldAsEmpty"
            int r0 = r9.var(r0)
            r4 = 54
            r7.visitIntInsn(r4, r0)
            r0 = 0
        L_0x02cc:
            if (r0 >= r3) goto L_0x03c5
            com.alibaba.fastjson.util.FieldInfo[] r4 = r34.fieldInfoList
            r4 = r4[r0]
            java.lang.Class<?> r5 = r4.fieldClass
            java.lang.Class r10 = java.lang.Boolean.TYPE
            if (r5 == r10) goto L_0x03a0
            java.lang.Class r10 = java.lang.Byte.TYPE
            if (r5 == r10) goto L_0x03a0
            java.lang.Class r10 = java.lang.Short.TYPE
            if (r5 == r10) goto L_0x03a0
            java.lang.Class r10 = java.lang.Integer.TYPE
            if (r5 != r10) goto L_0x02e8
            goto L_0x03a0
        L_0x02e8:
            java.lang.Class r10 = java.lang.Long.TYPE
            if (r5 != r10) goto L_0x0308
            r5 = 9
            r7.visitInsn(r5)
            r5 = 55
            r10 = 2
            int r4 = r9.var_asm(r4, r10)
            r7.visitVarInsn(r5, r4)
        L_0x02fb:
            r28 = r6
            r27 = r11
            r24 = r12
            r25 = r13
            r26 = r14
        L_0x0305:
            r5 = 3
            goto L_0x03b7
        L_0x0308:
            java.lang.Class r10 = java.lang.Float.TYPE
            if (r5 != r10) goto L_0x031b
            r5 = 11
            r7.visitInsn(r5)
            r5 = 56
            int r4 = r9.var_asm(r4)
            r7.visitVarInsn(r5, r4)
            goto L_0x02fb
        L_0x031b:
            java.lang.Class r10 = java.lang.Double.TYPE
            if (r5 != r10) goto L_0x032f
            r5 = 14
            r7.visitInsn(r5)
            r5 = 57
            r10 = 2
            int r4 = r9.var_asm(r4, r10)
            r7.visitVarInsn(r5, r4)
            goto L_0x02fb
        L_0x032f:
            java.lang.Class<java.lang.String> r10 = java.lang.String.class
            if (r5 != r10) goto L_0x037e
            com.alibaba.fastjson.asm.Label r10 = new com.alibaba.fastjson.asm.Label
            r10.<init>()
            r24 = r12
            com.alibaba.fastjson.asm.Label r12 = new com.alibaba.fastjson.asm.Label
            r12.<init>()
            r25 = r13
            java.lang.String r13 = "initStringFieldAsEmpty"
            int r13 = r9.var(r13)
            r26 = r14
            r14 = 21
            r7.visitVarInsn(r14, r13)
            r13 = 153(0x99, float:2.14E-43)
            r7.visitJumpInsn(r13, r12)
            r8._setFlag(r7, r9, r0)
            int r13 = r9.var(r15)
            r14 = 25
            r7.visitVarInsn(r14, r13)
            java.lang.String r13 = JSONLexerBase
            java.lang.String r14 = "stringDefaultValue"
            r27 = r11
            java.lang.String r11 = "()Ljava/lang/String;"
            r28 = r6
            r6 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r6, r13, r14, r11)
            r6 = 167(0xa7, float:2.34E-43)
            r7.visitJumpInsn(r6, r10)
            r7.visitLabel(r12)
            r6 = 1
            r7.visitInsn(r6)
            r7.visitLabel(r10)
            goto L_0x038c
        L_0x037e:
            r28 = r6
            r27 = r11
            r24 = r12
            r25 = r13
            r26 = r14
            r6 = 1
            r7.visitInsn(r6)
        L_0x038c:
            r6 = 192(0xc0, float:2.69E-43)
            java.lang.String r5 = com.alibaba.fastjson.util.ASMUtils.type(r5)
            r7.visitTypeInsn(r6, r5)
            int r4 = r9.var_asm(r4)
            r5 = 58
            r7.visitVarInsn(r5, r4)
            goto L_0x0305
        L_0x03a0:
            r28 = r6
            r27 = r11
            r24 = r12
            r25 = r13
            r26 = r14
            r5 = 3
            r7.visitInsn(r5)
            int r4 = r9.var_asm(r4)
            r6 = 54
            r7.visitVarInsn(r6, r4)
        L_0x03b7:
            int r0 = r0 + 1
            r12 = r24
            r13 = r25
            r14 = r26
            r11 = r27
            r6 = r28
            goto L_0x02cc
        L_0x03c5:
            r28 = r6
            r27 = r11
            r24 = r12
            r25 = r13
            r26 = r14
            r5 = 3
            r10 = 0
        L_0x03d1:
            if (r10 >= r3) goto L_0x0b66
            com.alibaba.fastjson.util.FieldInfo[] r0 = r34.fieldInfoList
            r4 = r0[r10]
            java.lang.Class<?> r6 = r4.fieldClass
            java.lang.reflect.Type r0 = r4.fieldType
            com.alibaba.fastjson.asm.Label r11 = new com.alibaba.fastjson.asm.Label
            r11.<init>()
            java.lang.Class r12 = java.lang.Boolean.TYPE
            java.lang.String r13 = "[C"
            if (r6 != r12) goto L_0x0423
            int r0 = r9.var(r15)
            r6 = 25
            r7.visitVarInsn(r6, r0)
            r0 = 0
            r7.visitVarInsn(r6, r0)
            java.lang.String r0 = r34.className
            java.lang.String r6 = r9.fieldName(r4)
            r12 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r12, r0, r6, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r6 = "scanFieldBoolean"
            java.lang.String r12 = "([C)Z"
            r13 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r13, r0, r6, r12)
            int r0 = r9.var_asm(r4)
            r4 = 54
            r7.visitVarInsn(r4, r0)
        L_0x0416:
            r30 = r3
            r31 = r11
        L_0x041a:
            r12 = r28
            r5 = 58
            r11 = 1
        L_0x041f:
            r14 = 182(0xb6, float:2.55E-43)
            goto L_0x0a34
        L_0x0423:
            java.lang.Class r12 = java.lang.Byte.TYPE
            java.lang.String r14 = "([C)I"
            java.lang.String r5 = "scanFieldInt"
            if (r6 != r12) goto L_0x0456
            int r0 = r9.var(r15)
            r6 = 25
            r7.visitVarInsn(r6, r0)
            r0 = 0
            r7.visitVarInsn(r6, r0)
            java.lang.String r0 = r34.className
            java.lang.String r6 = r9.fieldName(r4)
            r12 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r12, r0, r6, r13)
            java.lang.String r0 = JSONLexerBase
            r6 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r6, r0, r5, r14)
            int r0 = r9.var_asm(r4)
            r4 = 54
            r7.visitVarInsn(r4, r0)
            goto L_0x0416
        L_0x0456:
            java.lang.Class<java.lang.Byte> r12 = java.lang.Byte.class
            r29 = 5
            r30 = r3
            java.lang.String r3 = "valueOf"
            r31 = r11
            r11 = 184(0xb8, float:2.58E-43)
            if (r6 != r12) goto L_0x04c5
            int r0 = r9.var(r15)
            r6 = 25
            r7.visitVarInsn(r6, r0)
            r0 = 0
            r7.visitVarInsn(r6, r0)
            java.lang.String r0 = r34.className
            java.lang.String r6 = r9.fieldName(r4)
            r12 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r12, r0, r6, r13)
            java.lang.String r0 = JSONLexerBase
            r6 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r6, r0, r5, r14)
            java.lang.String r5 = "java/lang/Byte"
            java.lang.String r6 = "(B)Ljava/lang/Byte;"
            r7.visitMethodInsn(r11, r5, r3, r6)
            int r3 = r9.var_asm(r4)
            r5 = 58
            r7.visitVarInsn(r5, r3)
            com.alibaba.fastjson.asm.Label r3 = new com.alibaba.fastjson.asm.Label
            r3.<init>()
            int r6 = r9.var(r15)
            r11 = 25
            r7.visitVarInsn(r11, r6)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r2, r1)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r29)
            r7.visitLdcInsn(r0)
            r0 = 160(0xa0, float:2.24E-43)
            r7.visitJumpInsn(r0, r3)
            r0 = 1
            r7.visitInsn(r0)
            int r0 = r9.var_asm(r4)
            r7.visitVarInsn(r5, r0)
            r7.visitLabel(r3)
            goto L_0x041a
        L_0x04c5:
            java.lang.Class r12 = java.lang.Short.TYPE
            if (r6 != r12) goto L_0x04f5
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            r0 = 0
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r3, r13)
            java.lang.String r0 = JSONLexerBase
            r3 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r3, r0, r5, r14)
            int r0 = r9.var_asm(r4)
            r3 = 54
            r7.visitVarInsn(r3, r0)
            goto L_0x041a
        L_0x04f5:
            java.lang.Class<java.lang.Short> r12 = java.lang.Short.class
            if (r6 != r12) goto L_0x0559
            int r0 = r9.var(r15)
            r6 = 25
            r7.visitVarInsn(r6, r0)
            r0 = 0
            r7.visitVarInsn(r6, r0)
            java.lang.String r0 = r34.className
            java.lang.String r6 = r9.fieldName(r4)
            r12 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r12, r0, r6, r13)
            java.lang.String r0 = JSONLexerBase
            r6 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r6, r0, r5, r14)
            java.lang.String r5 = "java/lang/Short"
            java.lang.String r6 = "(S)Ljava/lang/Short;"
            r7.visitMethodInsn(r11, r5, r3, r6)
            int r3 = r9.var_asm(r4)
            r5 = 58
            r7.visitVarInsn(r5, r3)
            com.alibaba.fastjson.asm.Label r3 = new com.alibaba.fastjson.asm.Label
            r3.<init>()
            int r6 = r9.var(r15)
            r11 = 25
            r7.visitVarInsn(r11, r6)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r2, r1)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r29)
            r7.visitLdcInsn(r0)
            r0 = 160(0xa0, float:2.24E-43)
            r7.visitJumpInsn(r0, r3)
            r0 = 1
            r7.visitInsn(r0)
            int r0 = r9.var_asm(r4)
            r7.visitVarInsn(r5, r0)
            r7.visitLabel(r3)
            goto L_0x041a
        L_0x0559:
            java.lang.Class r12 = java.lang.Integer.TYPE
            if (r6 != r12) goto L_0x0589
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            r0 = 0
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r3, r13)
            java.lang.String r0 = JSONLexerBase
            r3 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r3, r0, r5, r14)
            int r0 = r9.var_asm(r4)
            r3 = 54
            r7.visitVarInsn(r3, r0)
            goto L_0x041a
        L_0x0589:
            java.lang.Class<java.lang.Integer> r12 = java.lang.Integer.class
            if (r6 != r12) goto L_0x05ed
            int r0 = r9.var(r15)
            r6 = 25
            r7.visitVarInsn(r6, r0)
            r0 = 0
            r7.visitVarInsn(r6, r0)
            java.lang.String r0 = r34.className
            java.lang.String r6 = r9.fieldName(r4)
            r12 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r12, r0, r6, r13)
            java.lang.String r0 = JSONLexerBase
            r6 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r6, r0, r5, r14)
            java.lang.String r5 = "java/lang/Integer"
            java.lang.String r6 = "(I)Ljava/lang/Integer;"
            r7.visitMethodInsn(r11, r5, r3, r6)
            int r3 = r9.var_asm(r4)
            r5 = 58
            r7.visitVarInsn(r5, r3)
            com.alibaba.fastjson.asm.Label r3 = new com.alibaba.fastjson.asm.Label
            r3.<init>()
            int r6 = r9.var(r15)
            r11 = 25
            r7.visitVarInsn(r11, r6)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r2, r1)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r29)
            r7.visitLdcInsn(r0)
            r0 = 160(0xa0, float:2.24E-43)
            r7.visitJumpInsn(r0, r3)
            r0 = 1
            r7.visitInsn(r0)
            int r0 = r9.var_asm(r4)
            r7.visitVarInsn(r5, r0)
            r7.visitLabel(r3)
            goto L_0x041a
        L_0x05ed:
            java.lang.Class r5 = java.lang.Long.TYPE
            if (r6 != r5) goto L_0x0622
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            r0 = 0
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r5 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r5, r0, r3, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r3 = "scanFieldLong"
            java.lang.String r5 = "([C)J"
            r6 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r6, r0, r3, r5)
            r0 = 55
            r3 = 2
            int r4 = r9.var_asm(r4, r3)
            r7.visitVarInsn(r0, r4)
            goto L_0x041a
        L_0x0622:
            java.lang.Class<java.lang.Long> r5 = java.lang.Long.class
            if (r6 != r5) goto L_0x068a
            int r0 = r9.var(r15)
            r5 = 25
            r7.visitVarInsn(r5, r0)
            r0 = 0
            r7.visitVarInsn(r5, r0)
            java.lang.String r0 = r34.className
            java.lang.String r5 = r9.fieldName(r4)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r5, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r5 = "scanFieldLong"
            java.lang.String r6 = "([C)J"
            r12 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r12, r0, r5, r6)
            java.lang.String r5 = "java/lang/Long"
            java.lang.String r6 = "(J)Ljava/lang/Long;"
            r7.visitMethodInsn(r11, r5, r3, r6)
            int r3 = r9.var_asm(r4)
            r5 = 58
            r7.visitVarInsn(r5, r3)
            com.alibaba.fastjson.asm.Label r3 = new com.alibaba.fastjson.asm.Label
            r3.<init>()
            int r6 = r9.var(r15)
            r11 = 25
            r7.visitVarInsn(r11, r6)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r2, r1)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r29)
            r7.visitLdcInsn(r0)
            r0 = 160(0xa0, float:2.24E-43)
            r7.visitJumpInsn(r0, r3)
            r0 = 1
            r7.visitInsn(r0)
            int r0 = r9.var_asm(r4)
            r7.visitVarInsn(r5, r0)
            r7.visitLabel(r3)
            goto L_0x041a
        L_0x068a:
            java.lang.Class r5 = java.lang.Float.TYPE
            if (r6 != r5) goto L_0x06be
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            r0 = 0
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r5 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r5, r0, r3, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r3 = "scanFieldFloat"
            java.lang.String r5 = "([C)F"
            r6 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r6, r0, r3, r5)
            r0 = 56
            int r3 = r9.var_asm(r4)
            r7.visitVarInsn(r0, r3)
            goto L_0x041a
        L_0x06be:
            java.lang.Class<java.lang.Float> r5 = java.lang.Float.class
            if (r6 != r5) goto L_0x0726
            int r0 = r9.var(r15)
            r5 = 25
            r7.visitVarInsn(r5, r0)
            r0 = 0
            r7.visitVarInsn(r5, r0)
            java.lang.String r0 = r34.className
            java.lang.String r5 = r9.fieldName(r4)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r5, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r5 = "scanFieldFloat"
            java.lang.String r6 = "([C)F"
            r12 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r12, r0, r5, r6)
            java.lang.String r5 = "java/lang/Float"
            java.lang.String r6 = "(F)Ljava/lang/Float;"
            r7.visitMethodInsn(r11, r5, r3, r6)
            int r3 = r9.var_asm(r4)
            r5 = 58
            r7.visitVarInsn(r5, r3)
            com.alibaba.fastjson.asm.Label r3 = new com.alibaba.fastjson.asm.Label
            r3.<init>()
            int r6 = r9.var(r15)
            r11 = 25
            r7.visitVarInsn(r11, r6)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r2, r1)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r29)
            r7.visitLdcInsn(r0)
            r0 = 160(0xa0, float:2.24E-43)
            r7.visitJumpInsn(r0, r3)
            r0 = 1
            r7.visitInsn(r0)
            int r0 = r9.var_asm(r4)
            r7.visitVarInsn(r5, r0)
            r7.visitLabel(r3)
            goto L_0x041a
        L_0x0726:
            java.lang.Class r5 = java.lang.Double.TYPE
            if (r6 != r5) goto L_0x075b
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            r0 = 0
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r5 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r5, r0, r3, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r3 = "scanFieldDouble"
            java.lang.String r5 = "([C)D"
            r6 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r6, r0, r3, r5)
            r0 = 57
            r5 = 2
            int r3 = r9.var_asm(r4, r5)
            r7.visitVarInsn(r0, r3)
            goto L_0x041a
        L_0x075b:
            r5 = 2
            java.lang.Class<java.lang.Double> r12 = java.lang.Double.class
            if (r6 != r12) goto L_0x07c8
            int r0 = r9.var(r15)
            r6 = 25
            r7.visitVarInsn(r6, r0)
            r0 = 0
            r7.visitVarInsn(r6, r0)
            java.lang.String r0 = r34.className
            java.lang.String r6 = r9.fieldName(r4)
            r12 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r12, r0, r6, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r6 = "scanFieldDouble"
            java.lang.String r12 = "([C)D"
            r13 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r13, r0, r6, r12)
            java.lang.String r6 = "java/lang/Double"
            java.lang.String r12 = "(D)Ljava/lang/Double;"
            r7.visitMethodInsn(r11, r6, r3, r12)
            int r3 = r9.var_asm(r4)
            r6 = 58
            r7.visitVarInsn(r6, r3)
            com.alibaba.fastjson.asm.Label r3 = new com.alibaba.fastjson.asm.Label
            r3.<init>()
            int r11 = r9.var(r15)
            r12 = 25
            r7.visitVarInsn(r12, r11)
            r11 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r11, r0, r2, r1)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r29)
            r7.visitLdcInsn(r0)
            r0 = 160(0xa0, float:2.24E-43)
            r7.visitJumpInsn(r0, r3)
            r11 = 1
            r7.visitInsn(r11)
            int r0 = r9.var_asm(r4)
            r7.visitVarInsn(r6, r0)
            r7.visitLabel(r3)
        L_0x07c2:
            r12 = r28
        L_0x07c4:
            r5 = 58
            goto L_0x041f
        L_0x07c8:
            r11 = 1
            java.lang.Class<java.lang.String> r3 = java.lang.String.class
            if (r6 != r3) goto L_0x07fc
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            r0 = 0
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r3, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r3 = "scanFieldString"
            java.lang.String r6 = "([C)Ljava/lang/String;"
            r12 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r12, r0, r3, r6)
            int r0 = r9.var_asm(r4)
            r3 = 58
            r7.visitVarInsn(r3, r0)
            goto L_0x07c2
        L_0x07fc:
            java.lang.Class<java.util.Date> r3 = java.util.Date.class
            if (r6 != r3) goto L_0x082f
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            r0 = 0
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r3, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r3 = "scanFieldDate"
            java.lang.String r6 = "([C)Ljava/util/Date;"
            r12 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r12, r0, r3, r6)
            int r0 = r9.var_asm(r4)
            r3 = 58
            r7.visitVarInsn(r3, r0)
            goto L_0x07c2
        L_0x082f:
            java.lang.Class<java.util.UUID> r3 = java.util.UUID.class
            if (r6 != r3) goto L_0x0863
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            r0 = 0
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r3, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r3 = "scanFieldUUID"
            java.lang.String r6 = "([C)Ljava/util/UUID;"
            r12 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r12, r0, r3, r6)
            int r0 = r9.var_asm(r4)
            r3 = 58
            r7.visitVarInsn(r3, r0)
            goto L_0x07c2
        L_0x0863:
            java.lang.Class<java.math.BigDecimal> r3 = java.math.BigDecimal.class
            if (r6 != r3) goto L_0x0897
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            r0 = 0
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r3, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r3 = "scanFieldDecimal"
            java.lang.String r6 = "([C)Ljava/math/BigDecimal;"
            r12 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r12, r0, r3, r6)
            int r0 = r9.var_asm(r4)
            r3 = 58
            r7.visitVarInsn(r3, r0)
            goto L_0x07c2
        L_0x0897:
            java.lang.Class<java.math.BigInteger> r3 = java.math.BigInteger.class
            if (r6 != r3) goto L_0x08cb
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            r0 = 0
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r3, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r3 = "scanFieldBigInteger"
            java.lang.String r6 = "([C)Ljava/math/BigInteger;"
            r12 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r12, r0, r3, r6)
            int r0 = r9.var_asm(r4)
            r3 = 58
            r7.visitVarInsn(r3, r0)
            goto L_0x07c2
        L_0x08cb:
            java.lang.Class<int[]> r3 = int[].class
            if (r6 != r3) goto L_0x08ff
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            r0 = 0
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r3, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r3 = "scanFieldIntArray"
            java.lang.String r6 = "([C)[I"
            r12 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r12, r0, r3, r6)
            int r0 = r9.var_asm(r4)
            r3 = 58
            r7.visitVarInsn(r3, r0)
            goto L_0x07c2
        L_0x08ff:
            java.lang.Class<float[]> r3 = float[].class
            if (r6 != r3) goto L_0x0933
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            r0 = 0
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r3, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r3 = "scanFieldFloatArray"
            java.lang.String r6 = "([C)[F"
            r12 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r12, r0, r3, r6)
            int r0 = r9.var_asm(r4)
            r3 = 58
            r7.visitVarInsn(r3, r0)
            goto L_0x07c2
        L_0x0933:
            java.lang.Class<float[][]> r3 = float[][].class
            if (r6 != r3) goto L_0x0967
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            r0 = 0
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r6 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r6, r0, r3, r13)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r3 = "scanFieldFloatArray2"
            java.lang.String r6 = "([C)[[F"
            r12 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r12, r0, r3, r6)
            int r0 = r9.var_asm(r4)
            r3 = 58
            r7.visitVarInsn(r3, r0)
            goto L_0x07c2
        L_0x0967:
            boolean r3 = r6.isEnum()
            if (r3 == 0) goto L_0x09d4
            r3 = 0
            r12 = 25
            r7.visitVarInsn(r12, r3)
            int r0 = r9.var(r15)
            r7.visitVarInsn(r12, r0)
            r7.visitVarInsn(r12, r3)
            java.lang.String r0 = r34.className
            java.lang.String r3 = r9.fieldName(r4)
            r12 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r12, r0, r3, r13)
            r8._getFieldDeser(r9, r7, r4)
            java.lang.Class<com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer> r0 = com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.class
            java.lang.String r0 = com.alibaba.fastjson.util.ASMUtils.type(r0)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r12 = r28
            r3.append(r12)
            java.lang.String r13 = JSONLexerBase
            r3.append(r13)
            java.lang.String r13 = ";[C"
            r3.append(r13)
            java.lang.Class<com.alibaba.fastjson.parser.deserializer.ObjectDeserializer> r13 = com.alibaba.fastjson.parser.deserializer.ObjectDeserializer.class
            java.lang.String r13 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r13)
            r3.append(r13)
            java.lang.String r13 = ")Ljava/lang/Enum;"
            r3.append(r13)
            java.lang.String r3 = r3.toString()
            java.lang.String r13 = "scanEnum"
            r14 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r14, r0, r13, r3)
            r0 = 192(0xc0, float:2.69E-43)
            java.lang.String r3 = com.alibaba.fastjson.util.ASMUtils.type(r6)
            r7.visitTypeInsn(r0, r3)
            int r0 = r9.var_asm(r4)
            r3 = 58
            r7.visitVarInsn(r3, r0)
            goto L_0x07c4
        L_0x09d4:
            r12 = r28
            java.lang.Class<java.util.Collection> r3 = java.util.Collection.class
            boolean r3 = r3.isAssignableFrom(r6)
            if (r3 == 0) goto L_0x0b2d
            int r3 = r9.var(r15)
            r14 = 25
            r7.visitVarInsn(r14, r3)
            r3 = 0
            r7.visitVarInsn(r14, r3)
            java.lang.String r3 = r34.className
            java.lang.String r14 = r9.fieldName(r4)
            r5 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r5, r3, r14, r13)
            java.lang.Class r13 = com.alibaba.fastjson.util.TypeUtils.getCollectionItemClass(r0)
            java.lang.Class<java.lang.String> r0 = java.lang.String.class
            if (r13 != r0) goto L_0x0afa
            java.lang.String r0 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r6)
            com.alibaba.fastjson.asm.Type r0 = com.alibaba.fastjson.asm.Type.getType(r0)
            r7.visitLdcInsn(r0)
            java.lang.String r0 = JSONLexerBase
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "([CLjava/lang/Class;)"
            r3.append(r5)
            java.lang.Class<java.util.Collection> r5 = java.util.Collection.class
            java.lang.String r5 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r5)
            r3.append(r5)
            java.lang.String r3 = r3.toString()
            java.lang.String r5 = "scanFieldStringArray"
            r14 = 182(0xb6, float:2.55E-43)
            r7.visitMethodInsn(r14, r0, r5, r3)
            int r0 = r9.var_asm(r4)
            r5 = 58
            r7.visitVarInsn(r5, r0)
        L_0x0a34:
            int r0 = r9.var(r15)
            r3 = 25
            r7.visitVarInsn(r3, r0)
            java.lang.String r0 = JSONLexerBase
            r4 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r4, r0, r2, r1)
            com.alibaba.fastjson.asm.Label r6 = new com.alibaba.fastjson.asm.Label
            r6.<init>()
            r13 = 158(0x9e, float:2.21E-43)
            r7.visitJumpInsn(r13, r6)
            r8._setFlag(r7, r9, r10)
            r7.visitLabel(r6)
            int r6 = r9.var(r15)
            r7.visitVarInsn(r3, r6)
            r7.visitFieldInsn(r4, r0, r2, r1)
            r3 = 89
            r7.visitInsn(r3)
            int r3 = r9.var(r2)
            r4 = 54
            r7.visitVarInsn(r4, r3)
            r3 = -1
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r7.visitLdcInsn(r3)
            r3 = 159(0x9f, float:2.23E-43)
            r4 = r27
            r7.visitJumpInsn(r3, r4)
            int r3 = r9.var(r15)
            r6 = 25
            r7.visitVarInsn(r6, r3)
            r3 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r3, r0, r2, r1)
            r3 = 158(0x9e, float:2.21E-43)
            r6 = r31
            r7.visitJumpInsn(r3, r6)
            java.lang.String r3 = "matchedCount"
            int r3 = r9.var(r3)
            r13 = 21
            r7.visitVarInsn(r13, r3)
            r3 = 4
            r7.visitInsn(r3)
            r5 = 96
            r7.visitInsn(r5)
            java.lang.String r5 = "matchedCount"
            int r5 = r9.var(r5)
            r11 = 54
            r7.visitVarInsn(r11, r5)
            int r5 = r9.var(r15)
            r11 = 25
            r7.visitVarInsn(r11, r5)
            r5 = 180(0xb4, float:2.52E-43)
            r7.visitFieldInsn(r5, r0, r2, r1)
            java.lang.Integer r13 = java.lang.Integer.valueOf(r3)
            r7.visitLdcInsn(r13)
            r13 = 159(0x9f, float:2.23E-43)
            r14 = r26
            r7.visitJumpInsn(r13, r14)
            r7.visitLabel(r6)
            int r6 = r30 + -1
            if (r10 != r6) goto L_0x0ae8
            int r6 = r9.var(r15)
            r7.visitVarInsn(r11, r6)
            r7.visitFieldInsn(r5, r0, r2, r1)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r3)
            r7.visitLdcInsn(r0)
            r3 = 160(0xa0, float:2.24E-43)
            r7.visitJumpInsn(r3, r4)
        L_0x0ae8:
            r19 = r1
            r20 = r2
            r13 = r7
            r21 = r30
            r17 = 54
            r18 = 180(0xb4, float:2.52E-43)
            r22 = 160(0xa0, float:2.24E-43)
            r23 = 58
            r7 = r4
            goto L_0x0b54
        L_0x0afa:
            r14 = r26
            r3 = 160(0xa0, float:2.24E-43)
            r5 = 180(0xb4, float:2.52E-43)
            r11 = 2
            r17 = 21
            r0 = r32
            r19 = r1
            r1 = r34
            r20 = r2
            r2 = r7
            r21 = r30
            r22 = 160(0xa0, float:2.24E-43)
            r3 = r27
            r5 = 182(0xb6, float:2.55E-43)
            r18 = 180(0xb4, float:2.52E-43)
            r11 = 25
            r17 = 54
            r23 = 58
            r5 = r6
            r6 = r13
            r13 = r7
            r7 = r10
            r0._deserialze_list_obj(r1, r2, r3, r4, r5, r6, r7)
            int r3 = r21 + -1
            r7 = r27
            if (r10 != r3) goto L_0x0b54
            r8._deserialize_endCheck(r9, r13, r7)
            goto L_0x0b54
        L_0x0b2d:
            r19 = r1
            r20 = r2
            r13 = r7
            r14 = r26
            r7 = r27
            r21 = r30
            r11 = 25
            r17 = 54
            r18 = 180(0xb4, float:2.52E-43)
            r22 = 160(0xa0, float:2.24E-43)
            r23 = 58
            r0 = r32
            r1 = r34
            r2 = r13
            r3 = r7
            r5 = r6
            r6 = r10
            r0._deserialze_obj(r1, r2, r3, r4, r5, r6)
            int r3 = r21 + -1
            if (r10 != r3) goto L_0x0b54
            r8._deserialize_endCheck(r9, r13, r7)
        L_0x0b54:
            int r10 = r10 + 1
            r27 = r7
            r28 = r12
            r7 = r13
            r26 = r14
            r1 = r19
            r2 = r20
            r3 = r21
            r5 = 3
            goto L_0x03d1
        L_0x0b66:
            r21 = r3
            r13 = r7
            r14 = r26
            r7 = r27
            r12 = r28
            r11 = 25
            r13.visitLabel(r14)
            java.lang.Class r0 = r34.clazz
            boolean r0 = r0.isInterface()
            if (r0 != 0) goto L_0x0b8f
            java.lang.Class r0 = r34.clazz
            int r0 = r0.getModifiers()
            boolean r0 = java.lang.reflect.Modifier.isAbstract(r0)
            if (r0 != 0) goto L_0x0b8f
            r8._batchSet(r9, r13)
        L_0x0b8f:
            r0 = r25
            r13.visitLabel(r0)
            r8._setContext(r9, r13)
            java.lang.String r0 = "instance"
            int r0 = r9.var(r0)
            r13.visitVarInsn(r11, r0)
            com.alibaba.fastjson.util.JavaBeanInfo r0 = r34.beanInfo
            java.lang.reflect.Method r0 = r0.buildMethod
            if (r0 == 0) goto L_0x0bd2
            java.lang.Class r1 = r34.getInstClass()
            java.lang.String r1 = com.alibaba.fastjson.util.ASMUtils.type(r1)
            java.lang.String r2 = r0.getName()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "()"
            r3.append(r4)
            java.lang.Class r0 = r0.getReturnType()
            java.lang.String r0 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r0)
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r3 = 182(0xb6, float:2.55E-43)
            r13.visitMethodInsn(r3, r1, r2, r0)
        L_0x0bd2:
            r0 = 176(0xb0, float:2.47E-43)
            r13.visitInsn(r0)
            r13.visitLabel(r7)
            r8._batchSet(r9, r13)
            r0 = 0
            r13.visitVarInsn(r11, r0)
            r0 = 1
            r13.visitVarInsn(r11, r0)
            r0 = 2
            r13.visitVarInsn(r11, r0)
            r0 = 3
            r13.visitVarInsn(r11, r0)
            java.lang.String r1 = "instance"
            int r1 = r9.var(r1)
            r13.visitVarInsn(r11, r1)
            r1 = 21
            r2 = 4
            r13.visitVarInsn(r1, r2)
            int r3 = r21 / 32
            if (r21 == 0) goto L_0x0c06
            int r2 = r21 % 32
            if (r2 == 0) goto L_0x0c06
            int r3 = r3 + 1
        L_0x0c06:
            r2 = 1
            if (r3 != r2) goto L_0x0c0e
            r2 = 4
            r13.visitInsn(r2)
            goto L_0x0c13
        L_0x0c0e:
            r2 = 16
            r13.visitIntInsn(r2, r3)
        L_0x0c13:
            r2 = 188(0xbc, float:2.63E-43)
            r4 = 10
            r13.visitIntInsn(r2, r4)
            r2 = 0
        L_0x0c1b:
            if (r2 >= r3) goto L_0x0c55
            r4 = 89
            r13.visitInsn(r4)
            if (r2 != 0) goto L_0x0c28
            r13.visitInsn(r0)
            goto L_0x0c35
        L_0x0c28:
            r4 = 1
            if (r2 != r4) goto L_0x0c30
            r4 = 4
            r13.visitInsn(r4)
            goto L_0x0c35
        L_0x0c30:
            r4 = 16
            r13.visitIntInsn(r4, r2)
        L_0x0c35:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "_asm_flag_"
            r4.append(r5)
            r4.append(r2)
            java.lang.String r4 = r4.toString()
            int r4 = r9.var(r4)
            r13.visitVarInsn(r1, r4)
            r4 = 79
            r13.visitInsn(r4)
            int r2 = r2 + 1
            goto L_0x0c1b
        L_0x0c55:
            java.lang.Class<com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer> r2 = com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.class
            java.lang.String r2 = com.alibaba.fastjson.util.ASMUtils.type(r2)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r12)
            java.lang.String r4 = DefaultJSONParser
            r3.append(r4)
            java.lang.String r5 = ";Ljava/lang/reflect/Type;Ljava/lang/Object;Ljava/lang/Object;I[I)Ljava/lang/Object;"
            r3.append(r5)
            java.lang.String r3 = r3.toString()
            java.lang.String r5 = "parseRest"
            r6 = 182(0xb6, float:2.55E-43)
            r13.visitMethodInsn(r6, r2, r5, r3)
            r2 = 192(0xc0, float:2.69E-43)
            java.lang.Class r3 = r34.clazz
            java.lang.String r3 = com.alibaba.fastjson.util.ASMUtils.type(r3)
            r13.visitTypeInsn(r2, r3)
            r2 = 176(0xb0, float:2.47E-43)
            r13.visitInsn(r2)
            r2 = r24
            r13.visitLabel(r2)
            r2 = 0
            r13.visitVarInsn(r11, r2)
            r2 = 1
            r13.visitVarInsn(r11, r2)
            r2 = 2
            r13.visitVarInsn(r11, r2)
            r13.visitVarInsn(r11, r0)
            r0 = 4
            r13.visitVarInsn(r1, r0)
            r0 = 183(0xb7, float:2.56E-43)
            java.lang.Class<com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer> r1 = com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.class
            java.lang.String r1 = com.alibaba.fastjson.util.ASMUtils.type(r1)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r12)
            r2.append(r4)
            java.lang.String r3 = ";Ljava/lang/reflect/Type;Ljava/lang/Object;I)Ljava/lang/Object;"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "deserialze"
            r13.visitMethodInsn(r0, r1, r3, r2)
            r0 = 176(0xb0, float:2.47E-43)
            r13.visitInsn(r0)
            r0 = 10
            int r1 = r34.variantIndex
            r13.visitMaxs(r0, r1)
            r13.visitEnd()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory._deserialze(com.alibaba.fastjson.asm.ClassWriter, com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory$Context):void");
    }

    private void defineVarLexer(Context context, MethodVisitor methodVisitor) {
        methodVisitor.visitVarInsn(25, 1);
        methodVisitor.visitFieldInsn(180, DefaultJSONParser, "lexer", ASMUtils.desc((Class<?>) JSONLexer.class));
        methodVisitor.visitTypeInsn(192, JSONLexerBase);
        methodVisitor.visitVarInsn(58, context.var("lexer"));
    }

    private void _createInstance(Context context, MethodVisitor methodVisitor) {
        Constructor<?> constructor = context.beanInfo.defaultConstructor;
        if (Modifier.isPublic(constructor.getModifiers())) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, ASMUtils.type(context.getInstClass()));
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(constructor.getDeclaringClass()), "<init>", "()V");
        } else {
            methodVisitor.visitVarInsn(25, 0);
            methodVisitor.visitVarInsn(25, 1);
            methodVisitor.visitVarInsn(25, 0);
            methodVisitor.visitFieldInsn(180, ASMUtils.type(JavaBeanDeserializer.class), "clazz", "Ljava/lang/Class;");
            String type = ASMUtils.type(JavaBeanDeserializer.class);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, type, WXBridgeManager.METHOD_CREATE_INSTANCE, "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;)Ljava/lang/Object;");
            methodVisitor.visitTypeInsn(192, ASMUtils.type(context.getInstClass()));
        }
        methodVisitor.visitVarInsn(58, context.var("instance"));
    }

    private void _batchSet(Context context, MethodVisitor methodVisitor) {
        _batchSet(context, methodVisitor, true);
    }

    private void _batchSet(Context context, MethodVisitor methodVisitor, boolean z) {
        int length = context.fieldInfoList.length;
        for (int i = 0; i < length; i++) {
            Label label = new Label();
            if (z) {
                _isFlag(methodVisitor, context, i, label);
            }
            _loadAndSet(context, methodVisitor, context.fieldInfoList[i]);
            if (z) {
                methodVisitor.visitLabel(label);
            }
        }
    }

    private void _loadAndSet(Context context, MethodVisitor methodVisitor, FieldInfo fieldInfo) {
        Class<?> cls = fieldInfo.fieldClass;
        Type type = fieldInfo.fieldType;
        if (cls == Boolean.TYPE) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(21, context.var_asm(fieldInfo));
            _set(context, methodVisitor, fieldInfo);
        } else if (cls == Byte.TYPE || cls == Short.TYPE || cls == Integer.TYPE || cls == Character.TYPE) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(21, context.var_asm(fieldInfo));
            _set(context, methodVisitor, fieldInfo);
        } else if (cls == Long.TYPE) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(22, context.var_asm(fieldInfo, 2));
            if (fieldInfo.method != null) {
                methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(context.getInstClass()), fieldInfo.method.getName(), ASMUtils.desc(fieldInfo.method));
                if (!fieldInfo.method.getReturnType().equals(Void.TYPE)) {
                    methodVisitor.visitInsn(87);
                    return;
                }
                return;
            }
            methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, ASMUtils.type(fieldInfo.declaringClass), fieldInfo.field.getName(), ASMUtils.desc(fieldInfo.fieldClass));
        } else if (cls == Float.TYPE) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(23, context.var_asm(fieldInfo));
            _set(context, methodVisitor, fieldInfo);
        } else if (cls == Double.TYPE) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(24, context.var_asm(fieldInfo, 2));
            _set(context, methodVisitor, fieldInfo);
        } else if (cls == String.class) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(25, context.var_asm(fieldInfo));
            _set(context, methodVisitor, fieldInfo);
        } else if (cls.isEnum()) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(25, context.var_asm(fieldInfo));
            _set(context, methodVisitor, fieldInfo);
        } else if (Collection.class.isAssignableFrom(cls)) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            if (TypeUtils.getCollectionItemClass(type) == String.class) {
                methodVisitor.visitVarInsn(25, context.var_asm(fieldInfo));
                methodVisitor.visitTypeInsn(192, ASMUtils.type(cls));
            } else {
                methodVisitor.visitVarInsn(25, context.var_asm(fieldInfo));
            }
            _set(context, methodVisitor, fieldInfo);
        } else {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(25, context.var_asm(fieldInfo));
            _set(context, methodVisitor, fieldInfo);
        }
    }

    private void _set(Context context, MethodVisitor methodVisitor, FieldInfo fieldInfo) {
        Method method = fieldInfo.method;
        if (method != null) {
            methodVisitor.visitMethodInsn(method.getDeclaringClass().isInterface() ? Opcodes.INVOKEINTERFACE : Opcodes.INVOKEVIRTUAL, ASMUtils.type(fieldInfo.declaringClass), method.getName(), ASMUtils.desc(method));
            if (!fieldInfo.method.getReturnType().equals(Void.TYPE)) {
                methodVisitor.visitInsn(87);
                return;
            }
            return;
        }
        methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, ASMUtils.type(fieldInfo.declaringClass), fieldInfo.field.getName(), ASMUtils.desc(fieldInfo.fieldClass));
    }

    private void _setContext(Context context, MethodVisitor methodVisitor) {
        methodVisitor.visitVarInsn(25, 1);
        methodVisitor.visitVarInsn(25, context.var("context"));
        String str = DefaultJSONParser;
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "setContext", Operators.BRACKET_START_STR + ASMUtils.desc((Class<?>) ParseContext.class) + ")V");
        Label label = new Label();
        methodVisitor.visitVarInsn(25, context.var("childContext"));
        methodVisitor.visitJumpInsn(Opcodes.IFNULL, label);
        methodVisitor.visitVarInsn(25, context.var("childContext"));
        methodVisitor.visitVarInsn(25, context.var("instance"));
        methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, ASMUtils.type(ParseContext.class), "object", "Ljava/lang/Object;");
        methodVisitor.visitLabel(label);
    }

    private void _deserialize_endCheck(Context context, MethodVisitor methodVisitor, Label label) {
        methodVisitor.visitIntInsn(21, context.var("matchedCount"));
        methodVisitor.visitJumpInsn(Opcodes.IFLE, label);
        methodVisitor.visitVarInsn(25, context.var("lexer"));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, BindingXConstants.KEY_TOKEN, "()I");
        methodVisitor.visitLdcInsn(13);
        methodVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, label);
        _quickNextTokenComma(context, methodVisitor);
    }

    private void _deserialze_list_obj(Context context, MethodVisitor methodVisitor, Label label, FieldInfo fieldInfo, Class<?> cls, Class<?> cls2, int i) {
        String str;
        String str2;
        String str3;
        int i2;
        int i3;
        int i4;
        Context context2 = context;
        MethodVisitor methodVisitor2 = methodVisitor;
        Label label2 = label;
        FieldInfo fieldInfo2 = fieldInfo;
        Class<?> cls3 = cls;
        Class<?> cls4 = cls2;
        int i5 = i;
        Label label3 = new Label();
        String str4 = JSONLexerBase;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str4, "matchField", "([C)Z");
        methodVisitor2.visitJumpInsn(Opcodes.IFEQ, label3);
        _setFlag(methodVisitor2, context2, i5);
        Label label4 = new Label();
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str4, BindingXConstants.KEY_TOKEN, "()I");
        methodVisitor2.visitLdcInsn(8);
        methodVisitor2.visitJumpInsn(Opcodes.IF_ICMPNE, label4);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitLdcInsn(16);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str4, "nextToken", "(I)V");
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label3);
        methodVisitor2.visitLabel(label4);
        Label label5 = new Label();
        Label label6 = new Label();
        Label label7 = label3;
        Label label8 = new Label();
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str4, BindingXConstants.KEY_TOKEN, "()I");
        methodVisitor2.visitLdcInsn(21);
        methodVisitor2.visitJumpInsn(Opcodes.IF_ICMPNE, label6);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitLdcInsn(14);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str4, "nextToken", "(I)V");
        _newCollection(methodVisitor2, cls3, i5, true);
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label5);
        methodVisitor2.visitLabel(label6);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str4, BindingXConstants.KEY_TOKEN, "()I");
        methodVisitor2.visitLdcInsn(14);
        methodVisitor2.visitJumpInsn(159, label8);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str4, BindingXConstants.KEY_TOKEN, "()I");
        methodVisitor2.visitLdcInsn(12);
        methodVisitor2.visitJumpInsn(Opcodes.IF_ICMPNE, label);
        _newCollection(methodVisitor2, cls3, i5, false);
        FieldInfo fieldInfo3 = fieldInfo;
        int var_asm = context2.var_asm(fieldInfo3);
        String str5 = BindingXConstants.KEY_TOKEN;
        methodVisitor2.visitVarInsn(58, var_asm);
        Class<?> cls5 = cls2;
        _getCollectionFieldItemDeser(context2, methodVisitor2, fieldInfo3, cls5);
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(cls2)));
        methodVisitor2.visitInsn(3);
        String str6 = str4;
        String str7 = "nextToken";
        methodVisitor2.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        String type = ASMUtils.type(ObjectDeserializer.class);
        StringBuilder sb = new StringBuilder();
        sb.append("(L");
        String str8 = DefaultJSONParser;
        sb.append(str8);
        sb.append(";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        String str9 = str8;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, type, "deserialze", sb.toString());
        methodVisitor2.visitVarInsn(58, context2.var("list_item_value"));
        methodVisitor2.visitVarInsn(25, context2.var_asm(fieldInfo3));
        methodVisitor2.visitVarInsn(25, context2.var("list_item_value"));
        if (cls.isInterface()) {
            str = "list_item_value";
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, ASMUtils.type(cls), "add", "(Ljava/lang/Object;)Z");
        } else {
            str = "list_item_value";
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(cls), "add", "(Ljava/lang/Object;)Z");
        }
        methodVisitor2.visitInsn(87);
        Label label9 = label7;
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label9);
        methodVisitor2.visitLabel(label8);
        _newCollection(methodVisitor2, cls3, i5, false);
        methodVisitor2.visitLabel(label5);
        methodVisitor2.visitVarInsn(58, context2.var_asm(fieldInfo3));
        boolean isPrimitive2 = ParserConfig.isPrimitive2(fieldInfo3.fieldClass);
        _getCollectionFieldItemDeser(context2, methodVisitor2, fieldInfo3, cls5);
        if (isPrimitive2) {
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, ASMUtils.type(ObjectDeserializer.class), "getFastMatchToken", "()I");
            methodVisitor2.visitVarInsn(54, context2.var("fastMatchToken"));
            methodVisitor2.visitVarInsn(25, context2.var("lexer"));
            methodVisitor2.visitVarInsn(21, context2.var("fastMatchToken"));
            str3 = str6;
            str2 = str7;
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str3, str2, "(I)V");
        } else {
            str3 = str6;
            str2 = str7;
            methodVisitor2.visitInsn(87);
            methodVisitor2.visitLdcInsn(12);
            methodVisitor2.visitVarInsn(54, context2.var("fastMatchToken"));
            _quickNextToken(context2, methodVisitor2, 12);
        }
        methodVisitor2.visitVarInsn(25, 1);
        Label label10 = label9;
        String str10 = str9;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str10, "getContext", "()" + ASMUtils.desc((Class<?>) ParseContext.class));
        methodVisitor2.visitVarInsn(58, context2.var("listContext"));
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitVarInsn(25, context2.var_asm(fieldInfo3));
        methodVisitor2.visitLdcInsn(fieldInfo3.name);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str10, "setContext", "(Ljava/lang/Object;Ljava/lang/Object;)" + ASMUtils.desc((Class<?>) ParseContext.class));
        methodVisitor2.visitInsn(87);
        Label label11 = new Label();
        Label label12 = new Label();
        methodVisitor2.visitInsn(3);
        String str11 = str2;
        String str12 = "(I)V";
        methodVisitor2.visitVarInsn(54, context2.var(ContextChain.TAG_INFRA));
        methodVisitor2.visitLabel(label11);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        String str13 = str5;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str3, str13, "()I");
        methodVisitor2.visitLdcInsn(15);
        methodVisitor2.visitJumpInsn(159, label12);
        Label label13 = label12;
        methodVisitor2.visitVarInsn(25, 0);
        String access$300 = context.className;
        StringBuilder sb2 = new StringBuilder();
        String str14 = "fastMatchToken";
        sb2.append(fieldInfo3.name);
        sb2.append("_asm_list_item_deser__");
        boolean z = isPrimitive2;
        methodVisitor2.visitFieldInsn(180, access$300, sb2.toString(), ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(cls2)));
        methodVisitor2.visitVarInsn(21, context2.var(ContextChain.TAG_INFRA));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        String type2 = ASMUtils.type(ObjectDeserializer.class);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, type2, "deserialze", "(L" + str10 + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        String str15 = str;
        methodVisitor2.visitVarInsn(58, context2.var(str15));
        methodVisitor2.visitIincInsn(context2.var(ContextChain.TAG_INFRA), 1);
        methodVisitor2.visitVarInsn(25, context2.var_asm(fieldInfo3));
        methodVisitor2.visitVarInsn(25, context2.var(str15));
        if (cls.isInterface()) {
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, ASMUtils.type(cls), "add", "(Ljava/lang/Object;)Z");
            i3 = 87;
            i2 = Opcodes.INVOKEVIRTUAL;
        } else {
            String type3 = ASMUtils.type(cls);
            i2 = Opcodes.INVOKEVIRTUAL;
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type3, "add", "(Ljava/lang/Object;)Z");
            i3 = 87;
        }
        methodVisitor2.visitInsn(i3);
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitVarInsn(25, context2.var_asm(fieldInfo3));
        methodVisitor2.visitMethodInsn(i2, str10, "checkListResolve", "(Ljava/util/Collection;)V");
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(i2, str3, str13, "()I");
        methodVisitor2.visitLdcInsn(16);
        methodVisitor2.visitJumpInsn(Opcodes.IF_ICMPNE, label11);
        if (z) {
            methodVisitor2.visitVarInsn(25, context2.var("lexer"));
            methodVisitor2.visitVarInsn(21, context2.var(str14));
            methodVisitor2.visitMethodInsn(i2, str3, str11, str12);
            i4 = Opcodes.GOTO;
        } else {
            _quickNextToken(context2, methodVisitor2, 12);
            i4 = Opcodes.GOTO;
        }
        methodVisitor2.visitJumpInsn(i4, label11);
        methodVisitor2.visitLabel(label13);
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitVarInsn(25, context2.var("listContext"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str10, "setContext", Operators.BRACKET_START_STR + ASMUtils.desc((Class<?>) ParseContext.class) + ")V");
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str3, str13, "()I");
        methodVisitor2.visitLdcInsn(15);
        methodVisitor2.visitJumpInsn(Opcodes.IF_ICMPNE, label);
        _quickNextTokenComma(context, methodVisitor);
        methodVisitor2.visitLabel(label10);
    }

    private void _quickNextToken(Context context, MethodVisitor methodVisitor, int i) {
        Label label = new Label();
        Label label2 = new Label();
        methodVisitor.visitVarInsn(25, context.var("lexer"));
        String str = JSONLexerBase;
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "getCurrent", "()C");
        if (i == 12) {
            methodVisitor.visitVarInsn(16, 123);
        } else if (i == 14) {
            methodVisitor.visitVarInsn(16, 91);
        } else {
            throw new IllegalStateException();
        }
        methodVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, label);
        methodVisitor.visitVarInsn(25, context.var("lexer"));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "next", "()C");
        methodVisitor.visitInsn(87);
        methodVisitor.visitVarInsn(25, context.var("lexer"));
        methodVisitor.visitLdcInsn(Integer.valueOf(i));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "setToken", "(I)V");
        methodVisitor.visitJumpInsn(Opcodes.GOTO, label2);
        methodVisitor.visitLabel(label);
        methodVisitor.visitVarInsn(25, context.var("lexer"));
        methodVisitor.visitLdcInsn(Integer.valueOf(i));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "nextToken", "(I)V");
        methodVisitor.visitLabel(label2);
    }

    private void _quickNextTokenComma(Context context, MethodVisitor methodVisitor) {
        Context context2 = context;
        MethodVisitor methodVisitor2 = methodVisitor;
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        Label label4 = new Label();
        Label label5 = new Label();
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        String str = JSONLexerBase;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "getCurrent", "()C");
        methodVisitor2.visitInsn(89);
        methodVisitor2.visitVarInsn(54, context2.var("ch"));
        methodVisitor2.visitVarInsn(16, 44);
        methodVisitor2.visitJumpInsn(Opcodes.IF_ICMPNE, label2);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "next", "()C");
        methodVisitor2.visitInsn(87);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitLdcInsn(16);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "setToken", "(I)V");
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label5);
        methodVisitor2.visitLabel(label2);
        methodVisitor2.visitVarInsn(21, context2.var("ch"));
        methodVisitor2.visitVarInsn(16, 125);
        methodVisitor2.visitJumpInsn(Opcodes.IF_ICMPNE, label3);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "next", "()C");
        methodVisitor2.visitInsn(87);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitLdcInsn(13);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "setToken", "(I)V");
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label5);
        methodVisitor2.visitLabel(label3);
        methodVisitor2.visitVarInsn(21, context2.var("ch"));
        methodVisitor2.visitVarInsn(16, 93);
        methodVisitor2.visitJumpInsn(Opcodes.IF_ICMPNE, label4);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "next", "()C");
        methodVisitor2.visitInsn(87);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitLdcInsn(15);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "setToken", "(I)V");
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label5);
        methodVisitor2.visitLabel(label4);
        methodVisitor2.visitVarInsn(21, context2.var("ch"));
        methodVisitor2.visitVarInsn(16, 26);
        methodVisitor2.visitJumpInsn(Opcodes.IF_ICMPNE, label);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitLdcInsn(20);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "setToken", "(I)V");
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label5);
        methodVisitor2.visitLabel(label);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "nextToken", "()V");
        methodVisitor2.visitLabel(label5);
    }

    private void _getCollectionFieldItemDeser(Context context, MethodVisitor methodVisitor, FieldInfo fieldInfo, Class<?> cls) {
        Label label = new Label();
        methodVisitor.visitVarInsn(25, 0);
        String access$300 = context.className;
        methodVisitor.visitFieldInsn(180, access$300, fieldInfo.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        methodVisitor.visitJumpInsn(Opcodes.IFNONNULL, label);
        methodVisitor.visitVarInsn(25, 0);
        methodVisitor.visitVarInsn(25, 1);
        String str = DefaultJSONParser;
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "getConfig", "()" + ASMUtils.desc((Class<?>) ParserConfig.class));
        methodVisitor.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(cls)));
        String type = ASMUtils.type(ParserConfig.class);
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type, "getDeserializer", "(Ljava/lang/reflect/Type;)" + ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        String access$3002 = context.className;
        methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, access$3002, fieldInfo.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        methodVisitor.visitLabel(label);
        methodVisitor.visitVarInsn(25, 0);
        String access$3003 = context.className;
        methodVisitor.visitFieldInsn(180, access$3003, fieldInfo.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
    }

    private void _newCollection(MethodVisitor methodVisitor, Class<?> cls, int i, boolean z) {
        if (cls.isAssignableFrom(ArrayList.class) && !z) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, "java/util/ArrayList");
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V");
        } else if (cls.isAssignableFrom(LinkedList.class) && !z) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, ASMUtils.type(LinkedList.class));
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(LinkedList.class), "<init>", "()V");
        } else if (cls.isAssignableFrom(HashSet.class)) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, ASMUtils.type(HashSet.class));
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(HashSet.class), "<init>", "()V");
        } else if (cls.isAssignableFrom(TreeSet.class)) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, ASMUtils.type(TreeSet.class));
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(TreeSet.class), "<init>", "()V");
        } else if (cls.isAssignableFrom(LinkedHashSet.class)) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, ASMUtils.type(LinkedHashSet.class));
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(LinkedHashSet.class), "<init>", "()V");
        } else if (z) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, ASMUtils.type(HashSet.class));
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(HashSet.class), "<init>", "()V");
        } else {
            methodVisitor.visitVarInsn(25, 0);
            methodVisitor.visitLdcInsn(Integer.valueOf(i));
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(JavaBeanDeserializer.class), "getFieldType", "(I)Ljava/lang/reflect/Type;");
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, ASMUtils.type(TypeUtils.class), "createCollection", "(Ljava/lang/reflect/Type;)Ljava/util/Collection;");
        }
        methodVisitor.visitTypeInsn(192, ASMUtils.type(cls));
    }

    private void _deserialze_obj(Context context, MethodVisitor methodVisitor, Label label, FieldInfo fieldInfo, Class<?> cls, int i) {
        Context context2 = context;
        MethodVisitor methodVisitor2 = methodVisitor;
        FieldInfo fieldInfo2 = fieldInfo;
        Label label2 = new Label();
        Label label3 = new Label();
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitVarInsn(25, 0);
        methodVisitor2.visitFieldInsn(180, context.className, context2.fieldName(fieldInfo2), "[C");
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "matchField", "([C)Z");
        methodVisitor2.visitJumpInsn(Opcodes.IFNE, label2);
        methodVisitor2.visitInsn(1);
        methodVisitor2.visitVarInsn(58, context2.var_asm(fieldInfo2));
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label3);
        methodVisitor2.visitLabel(label2);
        int i2 = i;
        _setFlag(methodVisitor2, context2, i2);
        methodVisitor2.visitVarInsn(21, context2.var("matchedCount"));
        methodVisitor2.visitInsn(4);
        methodVisitor2.visitInsn(96);
        methodVisitor2.visitVarInsn(54, context2.var("matchedCount"));
        _deserObject(context, methodVisitor, fieldInfo, cls, i2);
        methodVisitor2.visitVarInsn(25, 1);
        String str = DefaultJSONParser;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "getResolveStatus", "()I");
        methodVisitor2.visitLdcInsn(1);
        methodVisitor2.visitJumpInsn(Opcodes.IF_ICMPNE, label3);
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "getLastResolveTask", "()" + ASMUtils.desc((Class<?>) DefaultJSONParser.ResolveTask.class));
        methodVisitor2.visitVarInsn(58, context2.var("resolveTask"));
        methodVisitor2.visitVarInsn(25, context2.var("resolveTask"));
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "getContext", "()" + ASMUtils.desc((Class<?>) ParseContext.class));
        methodVisitor2.visitFieldInsn(Opcodes.PUTFIELD, ASMUtils.type(DefaultJSONParser.ResolveTask.class), "ownerContext", ASMUtils.desc((Class<?>) ParseContext.class));
        methodVisitor2.visitVarInsn(25, context2.var("resolveTask"));
        methodVisitor2.visitVarInsn(25, 0);
        methodVisitor2.visitLdcInsn(fieldInfo2.name);
        String type = ASMUtils.type(JavaBeanDeserializer.class);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type, "getFieldDeserializer", "(Ljava/lang/String;)" + ASMUtils.desc((Class<?>) FieldDeserializer.class));
        methodVisitor2.visitFieldInsn(Opcodes.PUTFIELD, ASMUtils.type(DefaultJSONParser.ResolveTask.class), "fieldDeserializer", ASMUtils.desc((Class<?>) FieldDeserializer.class));
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitLdcInsn(0);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "setResolveStatus", "(I)V");
        methodVisitor2.visitLabel(label3);
    }

    private void _deserObject(Context context, MethodVisitor methodVisitor, FieldInfo fieldInfo, Class<?> cls, int i) {
        Context context2 = context;
        MethodVisitor methodVisitor2 = methodVisitor;
        FieldInfo fieldInfo2 = fieldInfo;
        _getFieldDeser(context, methodVisitor, fieldInfo);
        Label label = new Label();
        Label label2 = new Label();
        if ((fieldInfo2.parserFeatures & Feature.SupportArrayToBean.mask) != 0) {
            methodVisitor2.visitInsn(89);
            methodVisitor2.visitTypeInsn(Opcodes.INSTANCEOF, ASMUtils.type(JavaBeanDeserializer.class));
            methodVisitor2.visitJumpInsn(Opcodes.IFEQ, label);
            methodVisitor2.visitTypeInsn(192, ASMUtils.type(JavaBeanDeserializer.class));
            methodVisitor2.visitVarInsn(25, 1);
            if (fieldInfo2.fieldType instanceof Class) {
                methodVisitor2.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(fieldInfo2.fieldClass)));
            } else {
                methodVisitor2.visitVarInsn(25, 0);
                methodVisitor2.visitLdcInsn(Integer.valueOf(i));
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(JavaBeanDeserializer.class), "getFieldType", "(I)Ljava/lang/reflect/Type;");
            }
            methodVisitor2.visitLdcInsn(fieldInfo2.name);
            methodVisitor2.visitLdcInsn(Integer.valueOf(fieldInfo2.parserFeatures));
            String type = ASMUtils.type(JavaBeanDeserializer.class);
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type, "deserialze", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;I)Ljava/lang/Object;");
            methodVisitor2.visitTypeInsn(192, ASMUtils.type(cls));
            methodVisitor2.visitVarInsn(58, context2.var_asm(fieldInfo2));
            methodVisitor2.visitJumpInsn(Opcodes.GOTO, label2);
            methodVisitor2.visitLabel(label);
        }
        methodVisitor2.visitVarInsn(25, 1);
        if (fieldInfo2.fieldType instanceof Class) {
            methodVisitor2.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(fieldInfo2.fieldClass)));
        } else {
            methodVisitor2.visitVarInsn(25, 0);
            methodVisitor2.visitLdcInsn(Integer.valueOf(i));
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(JavaBeanDeserializer.class), "getFieldType", "(I)Ljava/lang/reflect/Type;");
        }
        methodVisitor2.visitLdcInsn(fieldInfo2.name);
        String type2 = ASMUtils.type(ObjectDeserializer.class);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, type2, "deserialze", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        methodVisitor2.visitTypeInsn(192, ASMUtils.type(cls));
        methodVisitor2.visitVarInsn(58, context2.var_asm(fieldInfo2));
        methodVisitor2.visitLabel(label2);
    }

    private void _getFieldDeser(Context context, MethodVisitor methodVisitor, FieldInfo fieldInfo) {
        Label label = new Label();
        methodVisitor.visitVarInsn(25, 0);
        methodVisitor.visitFieldInsn(180, context.className, context.fieldDeserName(fieldInfo), ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        methodVisitor.visitJumpInsn(Opcodes.IFNONNULL, label);
        methodVisitor.visitVarInsn(25, 0);
        methodVisitor.visitVarInsn(25, 1);
        String str = DefaultJSONParser;
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "getConfig", "()" + ASMUtils.desc((Class<?>) ParserConfig.class));
        methodVisitor.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(fieldInfo.fieldClass)));
        String type = ASMUtils.type(ParserConfig.class);
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type, "getDeserializer", "(Ljava/lang/reflect/Type;)" + ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, context.className, context.fieldDeserName(fieldInfo), ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        methodVisitor.visitLabel(label);
        methodVisitor.visitVarInsn(25, 0);
        methodVisitor.visitFieldInsn(180, context.className, context.fieldDeserName(fieldInfo), ASMUtils.desc((Class<?>) ObjectDeserializer.class));
    }

    static class Context {
        static final int fieldName = 3;
        static final int parser = 1;
        static final int type = 2;
        /* access modifiers changed from: private */
        public final JavaBeanInfo beanInfo;
        /* access modifiers changed from: private */
        public final String className;
        /* access modifiers changed from: private */
        public final Class<?> clazz;
        /* access modifiers changed from: private */
        public FieldInfo[] fieldInfoList;
        /* access modifiers changed from: private */
        public int variantIndex = -1;
        private final Map<String, Integer> variants = new HashMap();

        public Context(String str, ParserConfig parserConfig, JavaBeanInfo javaBeanInfo, int i) {
            this.className = str;
            this.clazz = javaBeanInfo.clazz;
            this.variantIndex = i;
            this.beanInfo = javaBeanInfo;
            this.fieldInfoList = javaBeanInfo.fields;
        }

        public Class<?> getInstClass() {
            Class<?> cls = this.beanInfo.builderClass;
            return cls == null ? this.clazz : cls;
        }

        public int var(String str, int i) {
            if (this.variants.get(str) == null) {
                this.variants.put(str, Integer.valueOf(this.variantIndex));
                this.variantIndex += i;
            }
            return this.variants.get(str).intValue();
        }

        public int var(String str) {
            if (this.variants.get(str) == null) {
                Map<String, Integer> map = this.variants;
                int i = this.variantIndex;
                this.variantIndex = i + 1;
                map.put(str, Integer.valueOf(i));
            }
            return this.variants.get(str).intValue();
        }

        public int var_asm(FieldInfo fieldInfo) {
            return var(fieldInfo.name + "_asm");
        }

        public int var_asm(FieldInfo fieldInfo, int i) {
            return var(fieldInfo.name + "_asm", i);
        }

        public String fieldName(FieldInfo fieldInfo) {
            if (validIdent(fieldInfo.name)) {
                return fieldInfo.name + "_asm_prefix__";
            }
            return "asm_field_" + TypeUtils.fnv1a_64_extract(fieldInfo.name);
        }

        public String fieldDeserName(FieldInfo fieldInfo) {
            if (validIdent(fieldInfo.name)) {
                return fieldInfo.name + "_asm_deser__";
            }
            return "_asm_deser__" + TypeUtils.fnv1a_64_extract(fieldInfo.name);
        }

        /* access modifiers changed from: package-private */
        public boolean validIdent(String str) {
            for (int i = 0; i < str.length(); i++) {
                char charAt = str.charAt(i);
                if (charAt == 0) {
                    if (!IOUtils.firstIdentifier(charAt)) {
                        return false;
                    }
                } else if (!IOUtils.isIdent(charAt)) {
                    return false;
                }
            }
            return true;
        }
    }

    private void _init(ClassWriter classWriter, Context context) {
        ClassWriter classWriter2 = classWriter;
        Context context2 = context;
        for (FieldInfo fieldName : context.fieldInfoList) {
            new FieldWriter(classWriter, 1, context2.fieldName(fieldName), "[C").visitEnd();
        }
        for (FieldInfo fieldInfo : context.fieldInfoList) {
            Class<?> cls = fieldInfo.fieldClass;
            if (!cls.isPrimitive()) {
                if (Collection.class.isAssignableFrom(cls)) {
                    new FieldWriter(classWriter, 1, fieldInfo.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class)).visitEnd();
                } else {
                    new FieldWriter(classWriter, 1, context2.fieldDeserName(fieldInfo), ASMUtils.desc((Class<?>) ObjectDeserializer.class)).visitEnd();
                }
            }
        }
        MethodWriter methodWriter = new MethodWriter(classWriter, 1, "<init>", Operators.BRACKET_START_STR + ASMUtils.desc((Class<?>) ParserConfig.class) + ASMUtils.desc((Class<?>) JavaBeanInfo.class) + ")V", (String) null, (String[]) null);
        methodWriter.visitVarInsn(25, 0);
        methodWriter.visitVarInsn(25, 1);
        methodWriter.visitVarInsn(25, 2);
        methodWriter.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(JavaBeanDeserializer.class), "<init>", Operators.BRACKET_START_STR + ASMUtils.desc((Class<?>) ParserConfig.class) + ASMUtils.desc((Class<?>) JavaBeanInfo.class) + ")V");
        for (FieldInfo fieldInfo2 : context.fieldInfoList) {
            methodWriter.visitVarInsn(25, 0);
            methodWriter.visitLdcInsn(JSUtil.QUOTE + fieldInfo2.name + "\":");
            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "toCharArray", "()[C");
            methodWriter.visitFieldInsn(Opcodes.PUTFIELD, context.className, context2.fieldName(fieldInfo2), "[C");
        }
        methodWriter.visitInsn(Opcodes.RETURN);
        methodWriter.visitMaxs(4, 4);
        methodWriter.visitEnd();
    }

    private void _createInstance(ClassWriter classWriter, Context context) {
        if (Modifier.isPublic(context.beanInfo.defaultConstructor.getModifiers())) {
            MethodWriter methodWriter = new MethodWriter(classWriter, 1, WXBridgeManager.METHOD_CREATE_INSTANCE, "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;)Ljava/lang/Object;", (String) null, (String[]) null);
            methodWriter.visitTypeInsn(Opcodes.NEW, ASMUtils.type(context.getInstClass()));
            methodWriter.visitInsn(89);
            methodWriter.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(context.getInstClass()), "<init>", "()V");
            methodWriter.visitInsn(176);
            methodWriter.visitMaxs(3, 3);
            methodWriter.visitEnd();
        }
    }
}
