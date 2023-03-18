package com.alibaba.fastjson.parser.deserializer;

public class SqlDateDeserializer extends AbstractDateDeserializer implements ObjectDeserializer {
    public static final SqlDateDeserializer instance = new SqlDateDeserializer();
    public static final SqlDateDeserializer instance_timestamp = new SqlDateDeserializer(true);
    private boolean timestamp;

    public int getFastMatchToken() {
        return 2;
    }

    public SqlDateDeserializer() {
        this.timestamp = false;
    }

    public SqlDateDeserializer(boolean z) {
        this.timestamp = false;
        this.timestamp = true;
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:31|32) */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        r5 = java.lang.Long.parseLong(r6);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0073 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T cast(com.alibaba.fastjson.parser.DefaultJSONParser r3, java.lang.reflect.Type r4, java.lang.Object r5, java.lang.Object r6) {
        /*
            r2 = this;
            boolean r0 = r2.timestamp
            if (r0 == 0) goto L_0x0009
            java.lang.Object r3 = r2.castTimestamp(r3, r4, r5, r6)
            return r3
        L_0x0009:
            r4 = 0
            if (r6 != 0) goto L_0x000d
            return r4
        L_0x000d:
            boolean r5 = r6 instanceof java.util.Date
            if (r5 == 0) goto L_0x001d
            java.sql.Date r3 = new java.sql.Date
            java.util.Date r6 = (java.util.Date) r6
            long r4 = r6.getTime()
            r3.<init>(r4)
            goto L_0x003c
        L_0x001d:
            boolean r5 = r6 instanceof java.math.BigDecimal
            if (r5 == 0) goto L_0x002d
            java.sql.Date r3 = new java.sql.Date
            java.math.BigDecimal r6 = (java.math.BigDecimal) r6
            long r4 = com.alibaba.fastjson.util.TypeUtils.longValue(r6)
            r3.<init>(r4)
            goto L_0x003c
        L_0x002d:
            boolean r5 = r6 instanceof java.lang.Number
            if (r5 == 0) goto L_0x003d
            java.sql.Date r3 = new java.sql.Date
            java.lang.Number r6 = (java.lang.Number) r6
            long r4 = r6.longValue()
            r3.<init>(r4)
        L_0x003c:
            return r3
        L_0x003d:
            boolean r5 = r6 instanceof java.lang.String
            if (r5 == 0) goto L_0x0085
            java.lang.String r6 = (java.lang.String) r6
            int r5 = r6.length()
            if (r5 != 0) goto L_0x004a
            return r4
        L_0x004a:
            com.alibaba.fastjson.parser.JSONScanner r4 = new com.alibaba.fastjson.parser.JSONScanner
            r4.<init>(r6)
            boolean r5 = r4.scanISO8601DateIfMatch()     // Catch:{ all -> 0x0080 }
            if (r5 == 0) goto L_0x005e
            java.util.Calendar r3 = r4.getCalendar()     // Catch:{ all -> 0x0080 }
            long r5 = r3.getTimeInMillis()     // Catch:{ all -> 0x0080 }
            goto L_0x0077
        L_0x005e:
            java.text.DateFormat r3 = r3.getDateFormat()     // Catch:{ all -> 0x0080 }
            java.util.Date r3 = r3.parse(r6)     // Catch:{ ParseException -> 0x0073 }
            java.sql.Date r5 = new java.sql.Date     // Catch:{ ParseException -> 0x0073 }
            long r0 = r3.getTime()     // Catch:{ ParseException -> 0x0073 }
            r5.<init>(r0)     // Catch:{ ParseException -> 0x0073 }
            r4.close()
            return r5
        L_0x0073:
            long r5 = java.lang.Long.parseLong(r6)     // Catch:{ all -> 0x0080 }
        L_0x0077:
            r4.close()
            java.sql.Date r3 = new java.sql.Date
            r3.<init>(r5)
            return r3
        L_0x0080:
            r3 = move-exception
            r4.close()
            throw r3
        L_0x0085:
            com.alibaba.fastjson.JSONException r3 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "parse error : "
            r4.append(r5)
            r4.append(r6)
            java.lang.String r4 = r4.toString()
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.SqlDateDeserializer.cast(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object, java.lang.Object):java.lang.Object");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:53|54) */
    /* JADX WARNING: Code restructure failed: missing block: B:54:?, code lost:
        r5 = java.lang.Long.parseLong(r6);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x00c1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T castTimestamp(com.alibaba.fastjson.parser.DefaultJSONParser r3, java.lang.reflect.Type r4, java.lang.Object r5, java.lang.Object r6) {
        /*
            r2 = this;
            r4 = 0
            if (r6 != 0) goto L_0x0004
            return r4
        L_0x0004:
            boolean r5 = r6 instanceof java.util.Date
            if (r5 == 0) goto L_0x0014
            java.sql.Timestamp r3 = new java.sql.Timestamp
            java.util.Date r6 = (java.util.Date) r6
            long r4 = r6.getTime()
            r3.<init>(r4)
            return r3
        L_0x0014:
            boolean r5 = r6 instanceof java.math.BigDecimal
            if (r5 == 0) goto L_0x0024
            java.sql.Timestamp r3 = new java.sql.Timestamp
            java.math.BigDecimal r6 = (java.math.BigDecimal) r6
            long r4 = com.alibaba.fastjson.util.TypeUtils.longValue(r6)
            r3.<init>(r4)
            return r3
        L_0x0024:
            boolean r5 = r6 instanceof java.lang.Number
            if (r5 == 0) goto L_0x0034
            java.sql.Timestamp r3 = new java.sql.Timestamp
            java.lang.Number r6 = (java.lang.Number) r6
            long r4 = r6.longValue()
            r3.<init>(r4)
            return r3
        L_0x0034:
            boolean r5 = r6 instanceof java.lang.String
            if (r5 == 0) goto L_0x00d3
            java.lang.String r6 = (java.lang.String) r6
            int r5 = r6.length()
            if (r5 != 0) goto L_0x0041
            return r4
        L_0x0041:
            com.alibaba.fastjson.parser.JSONScanner r4 = new com.alibaba.fastjson.parser.JSONScanner
            r4.<init>(r6)
            int r5 = r6.length()     // Catch:{ all -> 0x00ce }
            r0 = 19
            if (r5 <= r0) goto L_0x009c
            r5 = 4
            char r5 = r6.charAt(r5)     // Catch:{ all -> 0x00ce }
            r1 = 45
            if (r5 != r1) goto L_0x009c
            r5 = 7
            char r5 = r6.charAt(r5)     // Catch:{ all -> 0x00ce }
            if (r5 != r1) goto L_0x009c
            r5 = 10
            char r5 = r6.charAt(r5)     // Catch:{ all -> 0x00ce }
            r1 = 32
            if (r5 != r1) goto L_0x009c
            r5 = 13
            char r5 = r6.charAt(r5)     // Catch:{ all -> 0x00ce }
            r1 = 58
            if (r5 != r1) goto L_0x009c
            r5 = 16
            char r5 = r6.charAt(r5)     // Catch:{ all -> 0x00ce }
            if (r5 != r1) goto L_0x009c
            char r5 = r6.charAt(r0)     // Catch:{ all -> 0x00ce }
            r0 = 46
            if (r5 != r0) goto L_0x009c
            java.lang.String r5 = r3.getDateFomartPattern()     // Catch:{ all -> 0x00ce }
            int r0 = r5.length()     // Catch:{ all -> 0x00ce }
            int r1 = r6.length()     // Catch:{ all -> 0x00ce }
            if (r0 == r1) goto L_0x009c
            java.lang.String r0 = com.alibaba.fastjson.JSON.DEFFAULT_DATE_FORMAT     // Catch:{ all -> 0x00ce }
            if (r5 != r0) goto L_0x009c
            java.sql.Timestamp r3 = java.sql.Timestamp.valueOf(r6)     // Catch:{ all -> 0x00ce }
            r4.close()
            return r3
        L_0x009c:
            r5 = 0
            boolean r5 = r4.scanISO8601DateIfMatch(r5)     // Catch:{ all -> 0x00ce }
            if (r5 == 0) goto L_0x00ac
            java.util.Calendar r3 = r4.getCalendar()     // Catch:{ all -> 0x00ce }
            long r5 = r3.getTimeInMillis()     // Catch:{ all -> 0x00ce }
            goto L_0x00c5
        L_0x00ac:
            java.text.DateFormat r3 = r3.getDateFormat()     // Catch:{ all -> 0x00ce }
            java.util.Date r3 = r3.parse(r6)     // Catch:{ ParseException -> 0x00c1 }
            java.sql.Timestamp r5 = new java.sql.Timestamp     // Catch:{ ParseException -> 0x00c1 }
            long r0 = r3.getTime()     // Catch:{ ParseException -> 0x00c1 }
            r5.<init>(r0)     // Catch:{ ParseException -> 0x00c1 }
            r4.close()
            return r5
        L_0x00c1:
            long r5 = java.lang.Long.parseLong(r6)     // Catch:{ all -> 0x00ce }
        L_0x00c5:
            r4.close()
            java.sql.Timestamp r3 = new java.sql.Timestamp
            r3.<init>(r5)
            return r3
        L_0x00ce:
            r3 = move-exception
            r4.close()
            throw r3
        L_0x00d3:
            com.alibaba.fastjson.JSONException r3 = new com.alibaba.fastjson.JSONException
            java.lang.String r4 = "parse error"
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.SqlDateDeserializer.castTimestamp(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object, java.lang.Object):java.lang.Object");
    }
}
