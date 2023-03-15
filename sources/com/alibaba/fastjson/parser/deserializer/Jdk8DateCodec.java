package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.ContextObjectSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.TypeUtils;
import io.dcloud.common.adapter.util.Logger;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.TimeZone;

public class Jdk8DateCodec extends ContextObjectDeserializer implements ObjectSerializer, ContextObjectSerializer, ObjectDeserializer {
    private static final DateTimeFormatter ISO_FIXED_FORMAT = DateTimeFormatter.ofPattern(defaultPatttern).withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern(defaultPatttern);
    private static final DateTimeFormatter defaultFormatter_23 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String defaultPatttern = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter formatter_d10_cn = DateTimeFormatter.ofPattern("yyyy年M月d日");
    private static final DateTimeFormatter formatter_d10_de = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter formatter_d10_eur = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatter_d10_in = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter formatter_d10_kr = DateTimeFormatter.ofPattern("yyyy년M월d일");
    private static final DateTimeFormatter formatter_d10_tw = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter formatter_d10_us = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter formatter_d8 = DateTimeFormatter.ofPattern(Logger.TIMESTAMP_YYYY_MM_DD);
    private static final DateTimeFormatter formatter_dt19_cn = DateTimeFormatter.ofPattern("yyyy年M月d日 HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_cn_1 = DateTimeFormatter.ofPattern("yyyy年M月d日 H时m分s秒");
    private static final DateTimeFormatter formatter_dt19_de = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_eur = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_in = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_kr = DateTimeFormatter.ofPattern("yyyy년M월d일 HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_tw = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_us = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_iso8601 = DateTimeFormatter.ofPattern(formatter_iso8601_pattern);
    private static final String formatter_iso8601_pattern = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String formatter_iso8601_pattern_23 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String formatter_iso8601_pattern_29 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
    public static final Jdk8DateCodec instance = new Jdk8DateCodec();

    public int getFastMatchToken() {
        return 4;
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj, String str, int i) {
        Long l;
        DateTimeFormatter dateTimeFormatter;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 8) {
            jSONLexer.nextToken();
            return null;
        }
        if (jSONLexer.token() == 4) {
            String stringVal = jSONLexer.stringVal();
            jSONLexer.nextToken();
            if (str != null) {
                dateTimeFormatter = defaultPatttern.equals(str) ? defaultFormatter : DateTimeFormatter.ofPattern(str);
            } else {
                dateTimeFormatter = null;
            }
            if ("".equals(stringVal)) {
                return null;
            }
            if (type == LocalDateTime.class) {
                if (stringVal.length() == 10 || stringVal.length() == 8) {
                    return LocalDateTime.of(parseLocalDate(stringVal, str, dateTimeFormatter), LocalTime.MIN);
                }
                return parseDateTime(stringVal, dateTimeFormatter);
            } else if (type != LocalDate.class) {
                boolean z = true;
                if (type == LocalTime.class) {
                    if (stringVal.length() == 23) {
                        LocalDateTime parse = LocalDateTime.parse(stringVal);
                        return LocalTime.of(parse.getHour(), parse.getMinute(), parse.getSecond(), parse.getNano());
                    }
                    int i2 = 0;
                    while (true) {
                        if (i2 >= stringVal.length()) {
                            break;
                        }
                        char charAt = stringVal.charAt(i2);
                        if (charAt < '0' || charAt > '9') {
                            z = false;
                        } else {
                            i2++;
                        }
                    }
                    z = false;
                    if (!z || stringVal.length() <= 8 || stringVal.length() >= 19) {
                        return LocalTime.parse(stringVal);
                    }
                    return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(stringVal)), JSON.defaultTimeZone.toZoneId()).toLocalTime();
                } else if (type == ZonedDateTime.class) {
                    if (dateTimeFormatter == defaultFormatter) {
                        dateTimeFormatter = ISO_FIXED_FORMAT;
                    }
                    if (dateTimeFormatter == null && stringVal.length() <= 19) {
                        JSONScanner jSONScanner = new JSONScanner(stringVal);
                        TimeZone timeZone = defaultJSONParser.lexer.getTimeZone();
                        jSONScanner.setTimeZone(timeZone);
                        if (jSONScanner.scanISO8601DateIfMatch(false)) {
                            return ZonedDateTime.ofInstant(jSONScanner.getCalendar().getTime().toInstant(), timeZone.toZoneId());
                        }
                    }
                    return parseZonedDateTime(stringVal, dateTimeFormatter);
                } else if (type == OffsetDateTime.class) {
                    return OffsetDateTime.parse(stringVal);
                } else {
                    if (type == OffsetTime.class) {
                        return OffsetTime.parse(stringVal);
                    }
                    if (type == ZoneId.class) {
                        return ZoneId.of(stringVal);
                    }
                    if (type == Period.class) {
                        return Period.parse(stringVal);
                    }
                    if (type == Duration.class) {
                        return Duration.parse(stringVal);
                    }
                    if (type == Instant.class) {
                        int i3 = 0;
                        while (true) {
                            if (i3 >= stringVal.length()) {
                                break;
                            }
                            char charAt2 = stringVal.charAt(i3);
                            if (charAt2 < '0' || charAt2 > '9') {
                                z = false;
                            } else {
                                i3++;
                            }
                        }
                        z = false;
                        if (!z || stringVal.length() <= 8 || stringVal.length() >= 19) {
                            return Instant.parse(stringVal);
                        }
                        return Instant.ofEpochMilli(Long.parseLong(stringVal));
                    }
                }
            } else if (stringVal.length() != 23) {
                return parseLocalDate(stringVal, str, dateTimeFormatter);
            } else {
                LocalDateTime parse2 = LocalDateTime.parse(stringVal);
                return LocalDate.of(parse2.getYear(), parse2.getMonthValue(), parse2.getDayOfMonth());
            }
        } else if (jSONLexer.token() == 2) {
            long longValue = jSONLexer.longValue();
            jSONLexer.nextToken();
            if ("unixtime".equals(str)) {
                longValue *= 1000;
            } else if ("yyyyMMddHHmmss".equals(str)) {
                int i4 = (int) (longValue / 10000000000L);
                int i5 = (int) ((longValue / 100000000) % 100);
                int i6 = (int) ((longValue / 1000000) % 100);
                int i7 = (int) ((longValue / 10000) % 100);
                int i8 = (int) ((longValue / 100) % 100);
                int i9 = (int) (longValue % 100);
                if (type == LocalDateTime.class) {
                    return LocalDateTime.of(i4, i5, i6, i7, i8, i9);
                }
            }
            if (type == LocalDateTime.class) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(longValue), JSON.defaultTimeZone.toZoneId());
            }
            if (type == LocalDate.class) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(longValue), JSON.defaultTimeZone.toZoneId()).toLocalDate();
            }
            if (type == LocalTime.class) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(longValue), JSON.defaultTimeZone.toZoneId()).toLocalTime();
            }
            if (type == ZonedDateTime.class) {
                return ZonedDateTime.ofInstant(Instant.ofEpochMilli(longValue), JSON.defaultTimeZone.toZoneId());
            }
            if (type == Instant.class) {
                return Instant.ofEpochMilli(longValue);
            }
            throw new UnsupportedOperationException();
        } else if (jSONLexer.token() == 12) {
            JSONObject parseObject = defaultJSONParser.parseObject();
            if (type == Instant.class) {
                Object obj2 = parseObject.get("epochSecond");
                Object obj3 = parseObject.get("nano");
                boolean z2 = obj2 instanceof Number;
                if (z2 && (obj3 instanceof Number)) {
                    return Instant.ofEpochSecond(TypeUtils.longExtractValue((Number) obj2), TypeUtils.longExtractValue((Number) obj3));
                }
                if (z2) {
                    return Instant.ofEpochSecond(TypeUtils.longExtractValue((Number) obj2));
                }
            } else if (type == Duration.class && (l = parseObject.getLong("seconds")) != null) {
                return Duration.ofSeconds(l.longValue(), parseObject.getLongValue("nano"));
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00f4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.time.LocalDateTime parseDateTime(java.lang.String r17, java.time.format.DateTimeFormatter r18) {
        /*
            r16 = this;
            r0 = r17
            r1 = 19
            r2 = 1
            r3 = 0
            r4 = 48
            if (r18 != 0) goto L_0x0117
            int r5 = r17.length()
            r6 = 32
            r7 = 16
            r8 = 13
            r9 = 7
            r10 = 46
            r11 = 4
            r12 = 58
            r13 = 10
            r14 = 45
            if (r5 != r1) goto L_0x00bb
            char r5 = r0.charAt(r11)
            char r9 = r0.charAt(r9)
            char r15 = r0.charAt(r13)
            char r8 = r0.charAt(r8)
            char r7 = r0.charAt(r7)
            if (r8 != r12) goto L_0x00ea
            if (r7 != r12) goto L_0x00ea
            if (r5 != r14) goto L_0x004a
            if (r9 != r14) goto L_0x004a
            r5 = 84
            if (r15 != r5) goto L_0x0044
            java.time.format.DateTimeFormatter r5 = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
            goto L_0x00ec
        L_0x0044:
            if (r15 != r6) goto L_0x00ea
            java.time.format.DateTimeFormatter r5 = defaultFormatter
            goto L_0x00ec
        L_0x004a:
            r6 = 47
            if (r5 != r6) goto L_0x0054
            if (r9 != r6) goto L_0x0054
            java.time.format.DateTimeFormatter r5 = formatter_dt19_tw
            goto L_0x00ec
        L_0x0054:
            char r7 = r0.charAt(r3)
            char r8 = r0.charAt(r2)
            r9 = 2
            char r9 = r0.charAt(r9)
            r12 = 3
            char r12 = r0.charAt(r12)
            r15 = 5
            char r15 = r0.charAt(r15)
            if (r9 != r6) goto L_0x00ad
            if (r15 != r6) goto L_0x00ad
            int r7 = r7 - r4
            int r7 = r7 * 10
            int r8 = r8 - r4
            int r7 = r7 + r8
            int r12 = r12 - r4
            int r12 = r12 * 10
            int r5 = r5 - r4
            int r12 = r12 + r5
            r5 = 12
            if (r7 <= r5) goto L_0x0081
            java.time.format.DateTimeFormatter r5 = formatter_dt19_eur
            goto L_0x00ec
        L_0x0081:
            if (r12 <= r5) goto L_0x0087
            java.time.format.DateTimeFormatter r5 = formatter_dt19_us
            goto L_0x00ec
        L_0x0087:
            java.util.Locale r5 = java.util.Locale.getDefault()
            java.lang.String r5 = r5.getCountry()
            java.lang.String r6 = "US"
            boolean r6 = r5.equals(r6)
            if (r6 == 0) goto L_0x009a
            java.time.format.DateTimeFormatter r5 = formatter_dt19_us
            goto L_0x00ec
        L_0x009a:
            java.lang.String r6 = "BR"
            boolean r6 = r5.equals(r6)
            if (r6 != 0) goto L_0x00aa
            java.lang.String r6 = "AU"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x00ea
        L_0x00aa:
            java.time.format.DateTimeFormatter r5 = formatter_dt19_eur
            goto L_0x00ec
        L_0x00ad:
            if (r9 != r10) goto L_0x00b4
            if (r15 != r10) goto L_0x00b4
            java.time.format.DateTimeFormatter r5 = formatter_dt19_de
            goto L_0x00ec
        L_0x00b4:
            if (r9 != r14) goto L_0x00ea
            if (r15 != r14) goto L_0x00ea
            java.time.format.DateTimeFormatter r5 = formatter_dt19_in
            goto L_0x00ec
        L_0x00bb:
            int r5 = r17.length()
            r15 = 23
            if (r5 != r15) goto L_0x00ea
            char r5 = r0.charAt(r11)
            char r9 = r0.charAt(r9)
            char r13 = r0.charAt(r13)
            char r8 = r0.charAt(r8)
            char r7 = r0.charAt(r7)
            char r15 = r0.charAt(r1)
            if (r8 != r12) goto L_0x00ea
            if (r7 != r12) goto L_0x00ea
            if (r5 != r14) goto L_0x00ea
            if (r9 != r14) goto L_0x00ea
            if (r13 != r6) goto L_0x00ea
            if (r15 != r10) goto L_0x00ea
            java.time.format.DateTimeFormatter r5 = defaultFormatter_23
            goto L_0x00ec
        L_0x00ea:
            r5 = r18
        L_0x00ec:
            int r6 = r17.length()
            r7 = 17
            if (r6 < r7) goto L_0x0119
            char r6 = r0.charAt(r11)
            r7 = 24180(0x5e74, float:3.3883E-41)
            if (r6 != r7) goto L_0x010f
            int r5 = r17.length()
            int r5 = r5 - r2
            char r5 = r0.charAt(r5)
            r6 = 31186(0x79d2, float:4.3701E-41)
            if (r5 != r6) goto L_0x010c
            java.time.format.DateTimeFormatter r5 = formatter_dt19_cn_1
            goto L_0x0119
        L_0x010c:
            java.time.format.DateTimeFormatter r5 = formatter_dt19_cn
            goto L_0x0119
        L_0x010f:
            r7 = 45380(0xb144, float:6.3591E-41)
            if (r6 != r7) goto L_0x0119
            java.time.format.DateTimeFormatter r5 = formatter_dt19_kr
            goto L_0x0119
        L_0x0117:
            r5 = r18
        L_0x0119:
            if (r5 != 0) goto L_0x0170
            com.alibaba.fastjson.parser.JSONScanner r6 = new com.alibaba.fastjson.parser.JSONScanner
            r6.<init>(r0)
            boolean r7 = r6.scanISO8601DateIfMatch(r3)
            if (r7 == 0) goto L_0x0137
            java.util.Calendar r0 = r6.getCalendar()
            java.time.Instant r0 = r0.toInstant()
            java.time.ZoneId r1 = java.time.ZoneId.systemDefault()
            java.time.LocalDateTime r0 = java.time.LocalDateTime.ofInstant(r0, r1)
            return r0
        L_0x0137:
            r6 = 0
        L_0x0138:
            int r7 = r17.length()
            if (r6 >= r7) goto L_0x014d
            char r7 = r0.charAt(r6)
            if (r7 < r4) goto L_0x014c
            r8 = 57
            if (r7 <= r8) goto L_0x0149
            goto L_0x014c
        L_0x0149:
            int r6 = r6 + 1
            goto L_0x0138
        L_0x014c:
            r2 = 0
        L_0x014d:
            if (r2 == 0) goto L_0x0170
            int r2 = r17.length()
            r3 = 8
            if (r2 <= r3) goto L_0x0170
            int r2 = r17.length()
            if (r2 >= r1) goto L_0x0170
            long r0 = java.lang.Long.parseLong(r17)
            java.time.Instant r0 = java.time.Instant.ofEpochMilli(r0)
            java.util.TimeZone r1 = com.alibaba.fastjson.JSON.defaultTimeZone
            java.time.ZoneId r1 = r1.toZoneId()
            java.time.LocalDateTime r0 = java.time.LocalDateTime.ofInstant(r0, r1)
            return r0
        L_0x0170:
            if (r5 != 0) goto L_0x0177
            java.time.LocalDateTime r0 = java.time.LocalDateTime.parse(r17)
            goto L_0x017b
        L_0x0177:
            java.time.LocalDateTime r0 = java.time.LocalDateTime.parse(r0, r5)
        L_0x017b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec.parseDateTime(java.lang.String, java.time.format.DateTimeFormatter):java.time.LocalDateTime");
    }

    /* access modifiers changed from: protected */
    public LocalDate parseLocalDate(String str, String str2, DateTimeFormatter dateTimeFormatter) {
        DateTimeFormatter dateTimeFormatter2;
        DateTimeFormatter dateTimeFormatter3;
        if (dateTimeFormatter == null) {
            if (str.length() == 8) {
                dateTimeFormatter = formatter_d8;
            }
            boolean z = false;
            if (str.length() == 10) {
                char charAt = str.charAt(4);
                char charAt2 = str.charAt(7);
                if (charAt == '/' && charAt2 == '/') {
                    dateTimeFormatter = formatter_d10_tw;
                }
                char charAt3 = str.charAt(0);
                char charAt4 = str.charAt(1);
                char charAt5 = str.charAt(2);
                char charAt6 = str.charAt(3);
                char charAt7 = str.charAt(5);
                if (charAt5 == '/' && charAt7 == '/') {
                    int i = ((charAt6 - '0') * 10) + (charAt - '0');
                    if (((charAt3 - '0') * 10) + (charAt4 - '0') > 12) {
                        dateTimeFormatter3 = formatter_d10_eur;
                    } else if (i > 12) {
                        dateTimeFormatter3 = formatter_d10_us;
                    } else {
                        String country = Locale.getDefault().getCountry();
                        if (country.equals("US")) {
                            dateTimeFormatter3 = formatter_d10_us;
                        } else if (country.equals("BR") || country.equals("AU")) {
                            dateTimeFormatter3 = formatter_d10_eur;
                        }
                    }
                    dateTimeFormatter = dateTimeFormatter3;
                } else if (charAt5 == '.' && charAt7 == '.') {
                    dateTimeFormatter = formatter_d10_de;
                } else if (charAt5 == '-' && charAt7 == '-') {
                    dateTimeFormatter = formatter_d10_in;
                }
            }
            if (str.length() >= 9) {
                char charAt8 = str.charAt(4);
                if (charAt8 == 24180) {
                    dateTimeFormatter2 = formatter_d10_cn;
                } else if (charAt8 == 45380) {
                    dateTimeFormatter2 = formatter_d10_kr;
                }
                dateTimeFormatter = dateTimeFormatter2;
            }
            int i2 = 0;
            while (true) {
                if (i2 >= str.length()) {
                    z = true;
                    break;
                }
                char charAt9 = str.charAt(i2);
                if (charAt9 < '0' || charAt9 > '9') {
                    break;
                }
                i2++;
            }
            if (z && str.length() > 8 && str.length() < 19) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(str)), JSON.defaultTimeZone.toZoneId()).toLocalDate();
            }
        }
        if (dateTimeFormatter == null) {
            return LocalDate.parse(str);
        }
        return LocalDate.parse(str, dateTimeFormatter);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x00fa A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.time.ZonedDateTime parseZonedDateTime(java.lang.String r16, java.time.format.DateTimeFormatter r17) {
        /*
            r15 = this;
            r0 = r16
            if (r17 != 0) goto L_0x011e
            int r1 = r16.length()
            r2 = 4
            r3 = 19
            r4 = 0
            r5 = 1
            r6 = 48
            if (r1 != r3) goto L_0x00b9
            char r1 = r0.charAt(r2)
            r7 = 7
            char r7 = r0.charAt(r7)
            r8 = 10
            char r9 = r0.charAt(r8)
            r10 = 13
            char r10 = r0.charAt(r10)
            r11 = 16
            char r11 = r0.charAt(r11)
            r12 = 58
            if (r10 != r12) goto L_0x00b9
            if (r11 != r12) goto L_0x00b9
            r10 = 45
            if (r1 != r10) goto L_0x0048
            if (r7 != r10) goto L_0x0048
            r1 = 84
            if (r9 != r1) goto L_0x0040
            java.time.format.DateTimeFormatter r1 = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
            goto L_0x00bb
        L_0x0040:
            r1 = 32
            if (r9 != r1) goto L_0x00b9
            java.time.format.DateTimeFormatter r1 = defaultFormatter
            goto L_0x00bb
        L_0x0048:
            r9 = 47
            if (r1 != r9) goto L_0x0052
            if (r7 != r9) goto L_0x0052
            java.time.format.DateTimeFormatter r1 = formatter_dt19_tw
            goto L_0x00bb
        L_0x0052:
            char r7 = r0.charAt(r4)
            char r11 = r0.charAt(r5)
            r12 = 2
            char r12 = r0.charAt(r12)
            r13 = 3
            char r13 = r0.charAt(r13)
            r14 = 5
            char r14 = r0.charAt(r14)
            if (r12 != r9) goto L_0x00a9
            if (r14 != r9) goto L_0x00a9
            int r7 = r7 - r6
            int r7 = r7 * 10
            int r11 = r11 - r6
            int r7 = r7 + r11
            int r13 = r13 - r6
            int r13 = r13 * 10
            int r1 = r1 - r6
            int r13 = r13 + r1
            r1 = 12
            if (r7 <= r1) goto L_0x007e
            java.time.format.DateTimeFormatter r1 = formatter_dt19_eur
            goto L_0x00bb
        L_0x007e:
            if (r13 <= r1) goto L_0x0083
            java.time.format.DateTimeFormatter r1 = formatter_dt19_us
            goto L_0x00bb
        L_0x0083:
            java.util.Locale r1 = java.util.Locale.getDefault()
            java.lang.String r1 = r1.getCountry()
            java.lang.String r7 = "US"
            boolean r7 = r1.equals(r7)
            if (r7 == 0) goto L_0x0096
            java.time.format.DateTimeFormatter r1 = formatter_dt19_us
            goto L_0x00bb
        L_0x0096:
            java.lang.String r7 = "BR"
            boolean r7 = r1.equals(r7)
            if (r7 != 0) goto L_0x00a6
            java.lang.String r7 = "AU"
            boolean r1 = r1.equals(r7)
            if (r1 == 0) goto L_0x00b9
        L_0x00a6:
            java.time.format.DateTimeFormatter r1 = formatter_dt19_eur
            goto L_0x00bb
        L_0x00a9:
            r1 = 46
            if (r12 != r1) goto L_0x00b2
            if (r14 != r1) goto L_0x00b2
            java.time.format.DateTimeFormatter r1 = formatter_dt19_de
            goto L_0x00bb
        L_0x00b2:
            if (r12 != r10) goto L_0x00b9
            if (r14 != r10) goto L_0x00b9
            java.time.format.DateTimeFormatter r1 = formatter_dt19_in
            goto L_0x00bb
        L_0x00b9:
            r1 = r17
        L_0x00bb:
            int r7 = r16.length()
            r8 = 17
            if (r7 < r8) goto L_0x00e5
            char r2 = r0.charAt(r2)
            r7 = 24180(0x5e74, float:3.3883E-41)
            if (r2 != r7) goto L_0x00de
            int r1 = r16.length()
            int r1 = r1 - r5
            char r1 = r0.charAt(r1)
            r2 = 31186(0x79d2, float:4.3701E-41)
            if (r1 != r2) goto L_0x00db
            java.time.format.DateTimeFormatter r1 = formatter_dt19_cn_1
            goto L_0x00e5
        L_0x00db:
            java.time.format.DateTimeFormatter r1 = formatter_dt19_cn
            goto L_0x00e5
        L_0x00de:
            r7 = 45380(0xb144, float:6.3591E-41)
            if (r2 != r7) goto L_0x00e5
            java.time.format.DateTimeFormatter r1 = formatter_dt19_kr
        L_0x00e5:
            r2 = 0
        L_0x00e6:
            int r7 = r16.length()
            if (r2 >= r7) goto L_0x00fa
            char r7 = r0.charAt(r2)
            if (r7 < r6) goto L_0x00fb
            r8 = 57
            if (r7 <= r8) goto L_0x00f7
            goto L_0x00fb
        L_0x00f7:
            int r2 = r2 + 1
            goto L_0x00e6
        L_0x00fa:
            r4 = 1
        L_0x00fb:
            if (r4 == 0) goto L_0x0120
            int r2 = r16.length()
            r4 = 8
            if (r2 <= r4) goto L_0x0120
            int r2 = r16.length()
            if (r2 >= r3) goto L_0x0120
            long r0 = java.lang.Long.parseLong(r16)
            java.time.Instant r0 = java.time.Instant.ofEpochMilli(r0)
            java.util.TimeZone r1 = com.alibaba.fastjson.JSON.defaultTimeZone
            java.time.ZoneId r1 = r1.toZoneId()
            java.time.ZonedDateTime r0 = java.time.ZonedDateTime.ofInstant(r0, r1)
            return r0
        L_0x011e:
            r1 = r17
        L_0x0120:
            if (r1 != 0) goto L_0x0127
            java.time.ZonedDateTime r0 = java.time.ZonedDateTime.parse(r16)
            goto L_0x012b
        L_0x0127:
            java.time.ZonedDateTime r0 = java.time.ZonedDateTime.parse(r0, r1)
        L_0x012b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec.parseZonedDateTime(java.lang.String, java.time.format.DateTimeFormatter):java.time.ZonedDateTime");
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj == null) {
            serializeWriter.writeNull();
            return;
        }
        if (type == null) {
            type = obj.getClass();
        }
        if (type == LocalDateTime.class) {
            int mask = SerializerFeature.UseISO8601DateFormat.getMask();
            LocalDateTime localDateTime = (LocalDateTime) obj;
            String dateFormatPattern = jSONSerializer.getDateFormatPattern();
            if (dateFormatPattern == null) {
                if ((mask & i) == 0 && !jSONSerializer.isEnabled(SerializerFeature.UseISO8601DateFormat)) {
                    if (jSONSerializer.isEnabled(SerializerFeature.WriteDateUseDateFormat)) {
                        dateFormatPattern = (jSONSerializer.getFastJsonConfigDateFormatPattern() == null || jSONSerializer.getFastJsonConfigDateFormatPattern().length() <= 0) ? JSON.DEFFAULT_DATE_FORMAT : jSONSerializer.getFastJsonConfigDateFormatPattern();
                    } else {
                        int nano = localDateTime.getNano();
                        if (nano != 0) {
                            dateFormatPattern = nano % 1000000 == 0 ? formatter_iso8601_pattern_23 : formatter_iso8601_pattern_29;
                        }
                    }
                }
                dateFormatPattern = formatter_iso8601_pattern;
            }
            if (dateFormatPattern != null) {
                write(serializeWriter, (TemporalAccessor) localDateTime, dateFormatPattern);
            } else {
                serializeWriter.writeLong(localDateTime.atZone(JSON.defaultTimeZone.toZoneId()).toInstant().toEpochMilli());
            }
        } else {
            serializeWriter.writeString(obj.toString());
        }
    }

    public void write(JSONSerializer jSONSerializer, Object obj, BeanContext beanContext) throws IOException {
        write(jSONSerializer.out, (TemporalAccessor) obj, beanContext.getFormat());
    }

    private void write(SerializeWriter serializeWriter, TemporalAccessor temporalAccessor, String str) {
        DateTimeFormatter dateTimeFormatter;
        if ("unixtime".equals(str)) {
            if (temporalAccessor instanceof ChronoZonedDateTime) {
                serializeWriter.writeInt((int) ((ChronoZonedDateTime) temporalAccessor).toEpochSecond());
                return;
            } else if (temporalAccessor instanceof LocalDateTime) {
                serializeWriter.writeInt((int) ((LocalDateTime) temporalAccessor).atZone(JSON.defaultTimeZone.toZoneId()).toEpochSecond());
                return;
            }
        }
        if ("millis".equals(str)) {
            Instant instant = null;
            if (temporalAccessor instanceof ChronoZonedDateTime) {
                instant = ((ChronoZonedDateTime) temporalAccessor).toInstant();
            } else if (temporalAccessor instanceof LocalDateTime) {
                instant = ((LocalDateTime) temporalAccessor).atZone(JSON.defaultTimeZone.toZoneId()).toInstant();
            }
            if (instant != null) {
                serializeWriter.writeLong(instant.toEpochMilli());
                return;
            }
        }
        if (str == formatter_iso8601_pattern) {
            dateTimeFormatter = formatter_iso8601;
        } else {
            dateTimeFormatter = DateTimeFormatter.ofPattern(str);
        }
        serializeWriter.writeString(dateTimeFormatter.format(temporalAccessor));
    }

    public static Object castToLocalDateTime(Object obj, String str) {
        if (obj == null) {
            return null;
        }
        if (str == null) {
            str = defaultPatttern;
        }
        return LocalDateTime.parse(obj.toString(), DateTimeFormatter.ofPattern(str));
    }
}
