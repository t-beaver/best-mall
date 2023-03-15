package com.dcloud.zxing2.pdf417.decoder.ec;

import com.dcloud.zxing2.ChecksumException;

public final class ErrorCorrection {
    private final ModulusGF field = ModulusGF.PDF417_GF;

    private int[] findErrorLocations(ModulusPoly modulusPoly) throws ChecksumException {
        int degree = modulusPoly.getDegree();
        int[] iArr = new int[degree];
        int i = 0;
        for (int i2 = 1; i2 < this.field.getSize() && i < degree; i2++) {
            if (modulusPoly.evaluateAt(i2) == 0) {
                iArr[i] = this.field.inverse(i2);
                i++;
            }
        }
        if (i == degree) {
            return iArr;
        }
        throw ChecksumException.getChecksumInstance();
    }

    private int[] findErrorMagnitudes(ModulusPoly modulusPoly, ModulusPoly modulusPoly2, int[] iArr) {
        int degree = modulusPoly2.getDegree();
        int[] iArr2 = new int[degree];
        for (int i = 1; i <= degree; i++) {
            iArr2[degree - i] = this.field.multiply(i, modulusPoly2.getCoefficient(i));
        }
        ModulusPoly modulusPoly3 = new ModulusPoly(this.field, iArr2);
        int length = iArr.length;
        int[] iArr3 = new int[length];
        for (int i2 = 0; i2 < length; i2++) {
            int inverse = this.field.inverse(iArr[i2]);
            iArr3[i2] = this.field.multiply(this.field.subtract(0, modulusPoly.evaluateAt(inverse)), this.field.inverse(modulusPoly3.evaluateAt(inverse)));
        }
        return iArr3;
    }

    private ModulusPoly[] runEuclideanAlgorithm(ModulusPoly modulusPoly, ModulusPoly modulusPoly2, int i) throws ChecksumException {
        if (modulusPoly.getDegree() >= modulusPoly2.getDegree()) {
            ModulusPoly modulusPoly3 = modulusPoly2;
            modulusPoly2 = modulusPoly;
            modulusPoly = modulusPoly3;
        }
        ModulusPoly zero = this.field.getZero();
        ModulusPoly modulusPoly4 = zero;
        ModulusPoly one = this.field.getOne();
        while (modulusPoly.getDegree() >= i / 2) {
            if (!modulusPoly.isZero()) {
                ModulusPoly zero2 = this.field.getZero();
                int inverse = this.field.inverse(modulusPoly.getCoefficient(modulusPoly.getDegree()));
                while (modulusPoly2.getDegree() >= modulusPoly.getDegree() && !modulusPoly2.isZero()) {
                    int degree = modulusPoly2.getDegree() - modulusPoly.getDegree();
                    int multiply = this.field.multiply(modulusPoly2.getCoefficient(modulusPoly2.getDegree()), inverse);
                    zero2 = zero2.add(this.field.buildMonomial(degree, multiply));
                    modulusPoly2 = modulusPoly2.subtract(modulusPoly.multiplyByMonomial(degree, multiply));
                }
                ModulusPoly modulusPoly5 = modulusPoly2;
                modulusPoly2 = modulusPoly;
                modulusPoly = modulusPoly5;
                ModulusPoly negative = zero2.multiply(one).subtract(modulusPoly4).negative();
                modulusPoly4 = one;
                one = negative;
            } else {
                throw ChecksumException.getChecksumInstance();
            }
        }
        int coefficient = one.getCoefficient(0);
        if (coefficient != 0) {
            int inverse2 = this.field.inverse(coefficient);
            return new ModulusPoly[]{one.multiply(inverse2), modulusPoly.multiply(inverse2)};
        }
        throw ChecksumException.getChecksumInstance();
    }

    public int decode(int[] iArr, int i, int[] iArr2) throws ChecksumException {
        ModulusPoly modulusPoly = new ModulusPoly(this.field, iArr);
        int[] iArr3 = new int[i];
        int i2 = 0;
        boolean z = false;
        for (int i3 = i; i3 > 0; i3--) {
            int evaluateAt = modulusPoly.evaluateAt(this.field.exp(i3));
            iArr3[i - i3] = evaluateAt;
            if (evaluateAt != 0) {
                z = true;
            }
        }
        if (!z) {
            return 0;
        }
        ModulusPoly one = this.field.getOne();
        if (iArr2 != null) {
            for (int length : iArr2) {
                int exp = this.field.exp((iArr.length - 1) - length);
                ModulusGF modulusGF = this.field;
                one = one.multiply(new ModulusPoly(modulusGF, new int[]{modulusGF.subtract(0, exp), 1}));
            }
        }
        ModulusPoly[] runEuclideanAlgorithm = runEuclideanAlgorithm(this.field.buildMonomial(i, 1), new ModulusPoly(this.field, iArr3), i);
        ModulusPoly modulusPoly2 = runEuclideanAlgorithm[0];
        ModulusPoly modulusPoly3 = runEuclideanAlgorithm[1];
        int[] findErrorLocations = findErrorLocations(modulusPoly2);
        int[] findErrorMagnitudes = findErrorMagnitudes(modulusPoly3, modulusPoly2, findErrorLocations);
        while (i2 < findErrorLocations.length) {
            int length2 = (iArr.length - 1) - this.field.log(findErrorLocations[i2]);
            if (length2 >= 0) {
                iArr[length2] = this.field.subtract(iArr[length2], findErrorMagnitudes[i2]);
                i2++;
            } else {
                throw ChecksumException.getChecksumInstance();
            }
        }
        return findErrorLocations.length;
    }
}
